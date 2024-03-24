import java.util.concurrent.TimeUnit;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class HelpAgent extends Agent {
    @Override
    protected void setup() {
        // Adding a cyclic behavior to continuously listen for help requests
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                // Attempt to receive a message from the LearningAgent
                ACLMessage msg = myAgent.receive();
                // Check if a message was received and if it's a request for help
                if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) {
                    System.out.println("HelpAgent: Request received from LearningAgent: " + msg.getContent());

                    // Simulate the process of finding a solution, which takes some time (5 seconds here)
                    String solution = "Solution for " + msg.getContent(); // Generating a solution based on the request
                    try {
                        System.out.println("HelpAgent: Searching for solution in data-set");
                        System.out.println("HelpAgent: Searching ... ");
                        TimeUnit.SECONDS.sleep(5); // Simulate a delay in response to represent solution search time
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // Catch and print any interruption exceptions during sleep
                    }

                    // Once a solution is "found", create a reply message to send back to the LearningAgent
                    ACLMessage reply = msg.createReply(); // Create a reply to the original request
                    reply.setPerformative(ACLMessage.INFORM); // Set the performative to INFORM, indicating a response
                    reply.setContent(solution); // Set the content of the reply to the generated solution
                    System.out.println("HelpAgent: Send the solution back to LearningAgent");
                    send(reply); // Send the reply message with the solution
                } else {
                    block(); // If no message is received, block and wait for a message to arrive
                }
            }
        });
    }
}