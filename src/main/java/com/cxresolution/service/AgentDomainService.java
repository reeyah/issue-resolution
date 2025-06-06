package main.java.com.cxresolution.service;

import main.java.com.cxresolution.entity.Agent;
import main.java.com.cxresolution.enums.IssueType;
import main.java.com.cxresolution.store.AgentStore;
import main.java.com.cxresolution.utils.helper.IdGenerator;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class AgentDomainService {
    private final AgentStore agentStore;
    private final IdGenerator agentIdGenerator = new IdGenerator("A");


    public AgentDomainService(AgentStore agentStore) {
        this.agentStore = agentStore;
    }

    public Agent createAgent(String email, String name, List<IssueType> expertise) {
        String agentId = agentIdGenerator.generateId();
        Agent agent = new Agent(agentId, name, email, expertise);
        agentStore.addAgent(agent);
        return agent;
    }

    public Optional<Agent> getAgentById(String agentId) {
        return agentStore.getAgentById(agentId);
    }

    public Optional<Agent> getAgentByEmail(String email) {
        return agentStore.getAgentByEmail(email);
    }

    public Collection<Agent> getAllAgents() {
        return agentStore.getAllAgents();
    }

}
