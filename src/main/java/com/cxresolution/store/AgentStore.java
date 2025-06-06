package main.java.com.cxresolution.store;

import main.java.com.cxresolution.entity.Agent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AgentStore {
    private final Map<String, Agent> agentsById = new HashMap<>();
    private final Map<String, Agent> agentsByEmail = new HashMap<>();

    public void addAgent(Agent agent) {
        agentsById.put(agent.getAgentId(), agent);
        agentsByEmail.put(agent.getEmail(), agent);
    }

    public Optional<Agent> getAgentById(String agentId) {
        return Optional.ofNullable(agentsById.get(agentId));
    }

    public Optional<Agent> getAgentByEmail(String email) {
        return Optional.ofNullable(agentsByEmail.get(email));
    }

    public Collection<Agent> getAllAgents() {
        return Collections.unmodifiableCollection(agentsById.values());
    }
}
