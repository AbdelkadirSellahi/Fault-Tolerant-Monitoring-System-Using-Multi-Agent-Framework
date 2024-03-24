import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SensorAgent extends Agent {
    // Initialize a Random object for generating random numbers.
    private Random random = new Random();
    // A flag to control the first-time message sending to the SystemAgent.
    private boolean test = true;

    @Override
    protected void setup() {
        // Add a cyclic behaviour to continuously listen for messages and respond.
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                if (test) {
                    // Create a new ACLMessage of type INFORM.
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    // Set the receiver of the message to be the SystemAgent.
                    msg.addReceiver(new jade.core.AID("systemAgent", jade.core.AID.ISLOCALNAME));
                    // Log the action of sending data to the SystemAgent.
                    System.out.println("----------------------------------------------------------");
                    System.out.println("SensorAgent : Sending the data to SystemAgent");
                    // Set the content of the message to be a random number.
                    msg.setContent(String.valueOf(random.nextInt(100)));
                    // Send the message.
                    send(msg);
                    // Ensure this block runs only once by setting test to false.
                    test = false;
                } else {
                    // Attempt to receive a response from the SystemAgent.
                    ACLMessage reply = myAgent.receive();
                    if (reply != null) {
                        // Log the receipt of the response from the SystemAgent.
                        System.out.println("SensorAgent : Receiving the response from SystemAgent");
                        // Pause the agent for 5 seconds to simulate processing time.
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Create a follow-up message to send another random number.
                        ACLMessage followUpMsg = new ACLMessage(ACLMessage.INFORM);
                        followUpMsg.addReceiver(new jade.core.AID("systemAgent", jade.core.AID.ISLOCALNAME));
                        followUpMsg.setContent(String.valueOf(random.nextInt(100)));
                        // Log the action of sending another data point to the SystemAgent.
                        System.out.println("----------------------------------------------------------");
                        System.out.println("SensorAgent : Sending the data to SystemAgent");
                        // Send the follow-up message.
                        send(followUpMsg);
                    } else {
                        // If no message is received, put the agent into a wait state.
                        block();
                    }
                }
            }
        });
    }
}
