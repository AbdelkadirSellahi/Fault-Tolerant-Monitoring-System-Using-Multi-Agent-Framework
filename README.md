# Fault-Tolerant Monitoring System Using Multi-Agent Framework

This project develops a fault-tolerant monitoring system employing a sophisticated multi-agent system (MAS) architecture, utilizing the JADE platform. Designed to simulate a comprehensive environmental monitoring system, it adeptly identifies faults and devises strategies for their resolution. The system features a variety of agents—Sensor Agents, System Agents, Learning Agents, and Help Agents—each playing a pivotal role in the system's overarching functionality and resilience.

## System Architecture and Agent Behaviors

### Agents Overview

- **Sensor Agents**: These agents simulate environmental sensors, generating and dispatching data representing environmental conditions to System Agents. The data, crafted through random number generation, mimics diverse sensor outputs, such as temperature or humidity levels, providing a dynamic simulation environment.

- **System Agents**: Tasked with data analysis, these agents scrutinize the information received from Sensor Agents to detect anomalies or faults, marking the first line of defense against system malfunctions.

- **Learning Agents**: Harnessing the power of the Q-Learning algorithm, Learning Agents address faults identified by System Agents. Through interaction and reinforcement, they refine their decision-making capabilities, enhancing their response efficacy over time.

- **Help Agents**: When Learning Agents face unsolvable challenges, Help Agents step in, offering solutions drawn from a broader knowledge base or external expertise. This collaboration underscores the system's adaptive and cooperative nature.

## Enhancements and Tools

### Graphical Representation of Response Times

One of the unique features of this project is the graphical representation of the Learning Agent's response times to detected failures. This visualization serves several critical purposes:

- **Performance Analysis**: It allows developers and system analysts to visually assess how quickly the Learning Agent can react to and process different types of faults over time, providing immediate feedback on the system's efficiency.

- **Learning Efficiency**: By observing the trend in response times, stakeholders can gauge the effectiveness of the Q-Learning algorithm in improving the agent's decision-making process, as a decreasing trend would indicate successful learning and adaptation.

- **Debugging and Optimization**: Identifying outliers or spikes in response times can help in pinpointing potential bottlenecks or inefficiencies in the Learning Agent's logic or the Q-Learning implementation, guiding targeted optimizations.

This feature employs JFreeChart for generating real-time XY line charts, offering an intuitive and interactive means to monitor the Learning Agent's operational dynamics.

### Sniffer for Agent Communication Monitoring

To ensure transparency and facilitate debugging in the multi-agent system, the project utilizes JADE's Sniffer Agent. This tool provides a real-time visual representation of the messages exchanged between agents, covering aspects such as:

- **Message Content**: Displaying the details of the messages sent and received by the agents, including performative (e.g., REQUEST, INFORM), content, and conversation IDs.

- **Communication Flow**: Visualizing the sequence of message exchanges, which helps in understanding the interaction patterns and dependencies among agents.

- **Performance Monitoring**: Assisting in identifying delays or bottlenecks in message handling, which could affect the system's overall performance.

The Sniffer Agent is invaluable for debugging, optimizing agent interactions, and ensuring the integrity and reliability of communications within the system.

## Configuration Table

The following table outlines the primary configuration parameters of the Fault-Tolerant Monitoring System, including details about the agents, their behaviors, the Q-Learning algorithm, and messaging protocols. This information is crucial for system setup, customization, and optimization.

| Parameter | Description | Values/Types |
|-----------|-------------|--------------|
| Agent Type | Defines the role of each agent in the system. | Sensor Agent, System Agent, Learning Agent, Help Agent |
| Behaviors | Specifies the type of behavior each agent exhibits. | CyclicBehaviour, (Optional: OneShotBehaviour, WakerBehaviour) |
| Message Types | The types of ACL messages used for inter-agent communication. | INFORM, REQUEST, REPLY |
| Q-Learning Alpha (α) | Learning rate in Q-Learning, influencing how new information affects learned values. | 0.1 (default), range: 0-1 |
| Q-Learning Gamma (γ) | Discount factor in Q-Learning, determining the importance of future rewards. | 0.9 (default), range: 0-1 |
| Random Number Usage | Utilization of random numbers in the simulation. | Generating sensor data, decision-making process |
| Fault Detection Thresholds | Predefined values that determine what constitutes a fault. | Specific to sensor data type (e.g., temperature, humidity) |
| Response Time Logging | Interval at which the Learning Agent's response times are logged and displayed. | Every 10 fault detections (default) |
| Sniffer Configuration | Settings for the Sniffer Agent to monitor and visualize agent communication. | Enabled/Disabled, Filter by agent/interaction |

## Behavioral Dynamics

- **CyclicBehaviour**: A core component of our agents, this behavior allows for continuous listening and processing of messages, ensuring that agents remain responsive to new information or requests at all times.

- **OneShotBehaviour and WakerBehaviour (not explicitly mentioned but integral for potential extensions)**: These behaviors could be utilized for single-execution tasks or time-delayed actions, providing a framework for complex, time-sensitive operations within the system.

## Utilization of Random Numbers

Random numbers play a crucial role in simulating environmental data and system responses. They introduce variability and unpredictability into the simulation, mirroring the dynamic and often uncertain nature of real-world environments. In fault detection, random numbers help delineate between normal operating conditions and fault states, enabling a more robust testing of the system's resilience and adaptability.

## Features

- **Sensor Simulation**: Simulates environmental sensors (e.g., temperature, humidity) that send data to a central monitoring system.
  
- **Fault Detection**: System agents monitor sensor data for anomalies or faults, triggering alerts when necessary.
  
- **Adaptive Learning**: Learning agents use a Q-Learning algorithm to adaptively respond to detected faults, improving over time.
  
- **Assistance Mechanism**: Help agents provide solutions to learning agents, enhancing the system's ability to handle new or complex faults.
  
- **Visual Analytics**: Utilizes JFreeChart to visualize response times, demonstrating the system's performance and efficiency.

## Fault Detection and Resolution

- **Fault Reading**: Determined through the analysis of sensor data, fault readings indicate deviations from expected environmental conditions. System Agents are designed to recognize these anomalies, signaling potential issues within the monitored environment.

- **Normal vs. Fault Conditions**: The system distinguishes between normal and fault conditions based on predefined thresholds. Randomly generated sensor data that falls outside these thresholds triggers fault detection mechanisms, prompting further action from Learning and Help Agents.

- **Solutions and Decision Making**: Learning Agents, powered by Q-Learning, make informed decisions to address detected faults. When a solution is not readily apparent, they seek the assistance of Help Agents, ensuring a comprehensive strategy for fault resolution.

## Q-Learning Algorithm

Q-Learning is a model-free reinforcement learning algorithm used by the Learning Agents to make decisions. It enables agents to learn the best actions to take in various states based on rewards. The algorithm maintains a Q-table, which is updated as follows:

- **State**: Represents the current situation or condition the agent observes.
- **Action**: A set of possible steps the agent can take in response to the current
state.
- **Reward**: Feedback received after performing an action, guiding the agent toward beneficial outcomes.

The Q-Learning formula for updating the Q-table is:

```
Q(state, action) = Q(state, action) + α * (reward + γ * max(Q(next state, all actions)) - Q(state, action))
```

Where:

- **α (alpha)**: is the learning rate, determining to what extent the newly acquired information will override the old information.
- **γ (gamma)**: is the discount factor, used to balance the importance of immediate and future rewards.

Over time, the Learning Agent improves its policy, choosing actions that maximize the cumulative reward.

## Communication and Messaging

The system uses the ACL (Agent Communication Language) messages for communication between agents, adhering to FIPA (Foundation for Intelligent Physical Agents) standards. The types of messages include:

- **INFORM**: Used by Sensor Agents to send data to System Agents and by Help Agents to send solutions to Learning Agents.
- **REQUEST**: Sent by Learning Agents to Help Agents when assistance is needed for unresolved faults.
- **REPLY**: Responses from Help Agents to Learning Agents, containing solutions.

This messaging system ensures robust and flexible communication within the multi-agent system, facilitating efficient problem-solving and learning.

## Getting Started

Follow these instructions to set up and explore the system's capabilities, including prerequisites, installation steps, and execution guidelines.

### Prerequisites

List any software requirements, libraries, or tools that need to be installed to run the project, including Java and JADE.
- Java JDK 8+
- JADE (Java Agent DEvelopment Framework): Provides the environment and functionalities for creating and managing agents.
- JFreeChart: Used for generating visual analytics of the system's performance.

### Installation and Running

1. Install Java JDK and configure environment variables.
2. Download and set up JADE.
3. Clone the repository and compile the agents using provided scripts.

```bash
java jade.Boot -gui HelpAgent:HelpAgent;LearningAgent:LearningAgent;SystemAgent:SystemAgent;SensorAgent:SensorAgent;
```

## Contributing

We welcome contributions to enhance functionality, improve efficiency, or extend system capabilities.

## Authors
- [**ABDELKADIR Sellahi**](https://github.com/AbdelkadirSellahi)
