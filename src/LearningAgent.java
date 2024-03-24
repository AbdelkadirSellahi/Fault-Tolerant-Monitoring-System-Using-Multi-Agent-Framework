import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.Random;

public class LearningAgent extends Agent {
    // Q-learning algorithm instance for decision making
    private QLearning qLearning = new QLearning();
    // Random generator for action selection and exploration
    private Random random = new Random();
    // Series for logging response times to visualize the agent's performance over time
    private XYSeries responseTimesSeries = new XYSeries("Response Times");
    // Counter to track the number of processed messages, used for periodic chart display
    private int messageCount = 0;

    @Override
    protected void setup() {
        // Adding a cyclic behavior to continuously process incoming messages
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                // Attempt to receive a message from other agents
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    messageCount++; // Increment message count for each received message
                    String content = msg.getContent();
                    // Check for fault indication in the message content
                    if (content.contains("Check for fault")) {
                        System.out.println("LearningAgent: Received fault reading from SystemAgent: " + content);
                        // Determine the state based on the message content
                        String state = determineState(content);
                        // Choose an action based on the current state
                        String action = qLearning.chooseAction(state);
                        // Random decision for action execution or seeking help
                        double rd = random.nextDouble();

                        // If no specific action is chosen or decides to seek help
                        if (action == null || action.isEmpty() || rd < 0.5) {
                            long startTime = System.currentTimeMillis(); // Start timing the response
                            // Handle the situation where the agent needs to seek help
                            handleNoSolution(state, msg);
                            long endTime = System.currentTimeMillis(); // End timing
                            // Log the response time for handling the request
                            logResponseTime(convertStateToNumericValue(state), endTime - startTime);
                        } else {
                            // Perform the selected action and get the associated reward
                            double reward = performActionAndGetReward(action, content);
                            // Assuming the state doesn't change for simplicity
                            String nextState = state;
                            // Update the Q-table based on the action and reward
                            qLearning.updateQTable(state, action, reward, nextState);
                            // Send a response back indicating the action taken
                            ACLMessage response = msg.createReply();
                            response.setContent("LearningAgent: Action taken: " + action);
                            send(response);
                        }

                        // Display the response time chart every 10 messages
                        if (messageCount % 10 == 0) {
                            displayChart();
                        }
                    }
                } else {
                    // If no message is received, block and wait
                    block();
                }
            }

            private String determineState(String content) {
                // Extract the numeric part of the message content to determine the state
                int number = Integer.parseInt(content.replaceAll("[^0-9]", ""));
                if (number > 50 && number <= 60) {
                    return "51-60";
                } else if (number > 60) {
                    return "61-70";
                }
                return "unknown"; // Return "unknown" if the number doesn't fit predefined states
            }

            private double performActionAndGetReward(String action, String content) {
                // Define the reward for each action
                if ("Action1".equals(action)) {
                    return 5.0;
                } else if ("Action2".equals(action)) {
                    return 3.0;
                }
                return 0.0; // Default reward for unspecified actions
            }

            private void handleNoSolution(String state, ACLMessage originalMsg) {
                // Send a help request to the help agent for unresolved states
                ACLMessage helpRequest = new ACLMessage(ACLMessage.REQUEST);
                helpRequest.addReceiver(new jade.core.AID("helpAgent", jade.core.AID.ISLOCALNAME));
                helpRequest.setContent("Need help with state: " + state);
                send(helpRequest);

            // Wait for a response from the help agent
            MessageTemplate mt = MessageTemplate.MatchSender(new jade.core.AID("helpAgent", jade.core.AID.ISLOCALNAME));
            ACLMessage response = blockingReceive(mt); // Blocking receive ensures waiting for a specific reply
            if (response != null) {
                // If a response is received, forward this solution to the original requester
                ACLMessage response2 = originalMsg.createReply(); // Create a reply to the original message
                response2.setContent("LearningAgent: Action taken based on help: " + response.getContent()); // Set content with the solution
                send(response2); // Send the reply with the help agent's solution
            }
        }

        private void logResponseTime(int stateValue, long responseTime) {
            // Logs the response time for a given state's action
            responseTimesSeries.add(stateValue, responseTime); // Add the response time data point to the series
        }

        private int convertStateToNumericValue(String state) {
            // Converts the state into a numeric value for graphing purposes
            switch (state) {
                case "51-60":
                    return 1; // Assign a numeric value to the "51-60" state
                case "61-70":
                    return 2; // Assign a numeric value to the "61-70" state
                default:
                    return 0; // Default case for unknown states
            }
        }

        private void displayChart() {
            // Displays the chart of response times for visual analysis
            XYSeriesCollection dataset = new XYSeriesCollection(); // Create a dataset
            dataset.addSeries(responseTimesSeries); // Add the response times series to the dataset

            // Create the chart using JFreeChart
            JFreeChart xylineChart = ChartFactory.createXYLineChart(
                    "Response Time Chart", // Chart title
                    "State", // X-axis label
                    "Response Time (ms)", // Y-axis label
                    dataset // Dataset
            );

            // Setup and display the chart in a new window
            ApplicationFrame chartFrame = new ApplicationFrame("Learning Agent Response Times"); // Frame title
            ChartPanel chartPanel = new ChartPanel(xylineChart); // Create a chart panel
            chartFrame.setContentPane(chartPanel); // Add the chart panel to the frame
            chartFrame.pack(); // Optimize frame size
            RefineryUtilities.centerFrameOnScreen(chartFrame); // Center the frame on the screen
            chartFrame.setVisible(true); // Make the frame visible
        }
    });
}