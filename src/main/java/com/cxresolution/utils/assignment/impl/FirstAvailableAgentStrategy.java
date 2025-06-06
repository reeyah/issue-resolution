package main.java.com.cxresolution.utils.assignment.impl;

import main.java.com.cxresolution.entity.Agent;
import main.java.com.cxresolution.enums.IssueType;
import main.java.com.cxresolution.utils.assignment.AssignmentStrategy;

import java.util.Collection;
import java.util.Optional;

public class FirstAvailableAgentStrategy implements AssignmentStrategy {

    @Override
    public Optional<Agent> assignAgent(IssueType issueType, Collection<Agent> agents) {
        return agents.stream()
                .filter(agent -> agent.isFree() && agent.getExpertise().contains(issueType))
                .findFirst();
    }
}
