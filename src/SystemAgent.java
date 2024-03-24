import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class SystemAgent extends Agent {
    private boolean waitingForLearningAgent = false; // Flag to indicate waiting state for LearningAgent's response

    @Override
    protected void setup() {
        // Adding a cyclic behaviour to continuously check for and process messages
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                if (!waitingForLearningAgent) {
                    // Not currently waiting for LearningAgent, ready to process SensorAgent messages
                    ACLMessage msg = myAgent.receive();
                    if (msg != null) {
                        System.out.println("SystemAgent: Received the data from Sensor Agent: " + msg.getContent());
                        int receivedNumber = Integer.parseInt(msg.getContent());
                        // Check if the received number indicates a fault
                        if (receivedNumber > 50) {
                            // Send message to LearningAgent to handle the fault
                            ACLMessage faultMsg = new ACLMessage(ACLMessage.INFORM);
                            faultMsg.addReceiver(new jade.core.AID("learningAgent", jade.core.AID.ISLOCALNAME));
                            faultMsg.setContent("Check for fault: Value is " + receivedNumber);
                            System.out.println("SystemAgent: Send the fault reading to LearningAgent and wait for response");
                            send(faultMsg);
                            waitingForLearningAgent = true; // Start waiting for LearningAgent's response
                        } else {
                            // If no fault is detected, acknowledge receipt to SensorAgent
                            ACLMessage reply = msg.createReply();
                            reply.setContent("Received");
                            send(reply);
                        }
                    } else {
                        block(); // Wait for a message if none is received
                    }
                } else {
                    // Waiting for LearningAgent's response
                    ACLMessage learningAgentResponse = myAgent.receive();
                    if (learningAgentResponse != null) {
                        System.out.println("SystemAgent: receive Response from LearningAgent: " + learningAgentResponse.getContent());
                        // Acknowledge fault processing to SensorAgent
                        ACLMessage sensorReply = new ACLMessage(ACLMessage.INFORM);
                        sensorReply.addReceiver(new jade.core.AID("sensorAgent", jade.core.AID.ISLOCALNAME));
                        sensorReply.setContent("Fault processed");
                        System.out.println("SystemAgent: Send Solution to SensorAgent");
                        send(sensorReply);
                        waitingForLearningAgent = false; // Reset waiting flag
                    } else {
                        block(); // Wait for a response if none is received
                    }
                }
            }
        });
    }
}
