import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QLearning {
    private Map<String, Map<String, Double>> qTable = new HashMap<>(); // The Q-table for storing the state-action values
    private Random random = new Random();
    private double alpha = 0.1; // Learning rate, determines the extent to which newly acquired information overrides old information
    private double gamma = 0.9; // Discount factor, represents the importance of future rewards

    public QLearning() {
        initializeQTable(); // Initializes the Q-table with predefined states and actions
    }

    private void initializeQTable() {
        // Initialize Q-table with all states and actions, setting initial values to 0
        qTable.put("51-60", new HashMap<>(Map.of("Action1", 0.0, "Action2", 0.0)));
        qTable.put("61-70", new HashMap<>(Map.of("Action1", 0.0, "Action2", 0.0)));
        // States represent ranges of sensor values, and actions represent potential responses to those values
    }

    public String chooseAction(String state) {
        // Choose the best action based on Q-table values or explore a new action, balancing exploration and exploitation
        Map<String, Double> actions = qTable.get(state);
        if (actions == null) {
            return "Action1"; // Default action if the state is not found in the table
        }

        if (random.nextDouble() < 0.8) { // 80% chance to exploit by choosing the best known action
            return actions.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        } else { // 20% chance to explore a new action randomly
            return actions.keySet().stream().skip(random.nextInt(actions.size())).findFirst().get();
        }
    }

    public void updateQTable(String state, String action, double reward, String nextState) {
        // Update the Q-table using the Q-learning formula
        double oldQValue = qTable.get(state).getOrDefault(action, 0.0); // Current Q-value
        double maxNextQ = getMaxQValue(nextState); // Max Q-value for the next state
        double newQValue = oldQValue + alpha * (reward + gamma * maxNextQ - oldQValue); // Calculate new Q-value
        qTable.get(state).put(action, newQValue); // Update the Q-table with the new Q-value
    }

    private double getMaxQValue(String state) {
        // Get the maximum Q-value for a given state from the Q-table
        Map<String, Double> actions = qTable.get(state);
        if (actions == null) {
            return 0.0; // Return 0 if the state is not found
        }
        return actions.values().stream().max(Double::compare).orElse(0.0); // Return the highest Q-value for the state
    }
}