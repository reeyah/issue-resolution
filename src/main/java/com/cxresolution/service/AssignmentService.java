package main.java.com.cxresolution.service;

import main.java.com.cxresolution.entity.Agent;
import main.java.com.cxresolution.entity.Issue;
import main.java.com.cxresolution.enums.IssueType;
import main.java.com.cxresolution.utils.assignment.AssignmentStrategy;

import java.util.Collection;
import java.util.Optional;

public class AssignmentService {
    private final AssignmentStrategy assignmentStrategy;
    private final AgentDomainService agentService;

    public AssignmentService(AssignmentStrategy assignmentStrategy,
                             AgentDomainService agentService) {
        this.assignmentStrategy = assignmentStrategy;
        this.agentService = agentService;
    }

    public AssignmentResult assignIssue(Issue issue) {
        Collection<Agent> availableAgents = agentService.getAllAgents();
        Optional<Agent> selectedAgent = assignmentStrategy
                .selectAgent(issue.getIssueType(), availableAgents);

        if (selectedAgent.isPresent()) {
            Agent agent = selectedAgent.get();

            agent.assignIssue(issue);
            issue.assignToAgent(agent.getAgentId());

            return new AssignmentResult(true, agent,
                    agent.isFree() ? "Assigned immediately" : "Added to agent's waitlist");
        }

        return new AssignmentResult(false, null, "No suitable agent available");
    }

    public static class AssignmentResult {
        private final boolean successful;
        private final Agent assignedAgent;
        private final String message;

        public AssignmentResult(boolean successful, Agent assignedAgent, String message) {
            this.successful = successful;
            this.assignedAgent = assignedAgent;
            this.message = message;
        }

        public boolean isSuccessful() { return successful; }
        public Optional<Agent> getAssignedAgent() { return Optional.ofNullable(assignedAgent); }
        public String getMessage() { return message; }
    }
}
