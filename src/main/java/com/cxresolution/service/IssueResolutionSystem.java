package main.java.com.cxresolution.service;

import main.java.com.cxresolution.entity.Agent;
import main.java.com.cxresolution.entity.Issue;
import main.java.com.cxresolution.enums.IssueType;
import main.java.com.cxresolution.enums.IssueStatus;
import main.java.com.cxresolution.utils.assignment.AssignmentStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IssueResolutionSystem {

    private final AgentDomainService agentService;
    private final IssueDomainService issueService;
    private final AssignmentStrategy assignmentStrategy;

    public IssueResolutionSystem(AgentDomainService agentService,
                                 IssueDomainService issueService,
                                 AssignmentStrategy assignmentStrategy) {
        this.agentService = agentService;
        this.issueService = issueService;
        this.assignmentStrategy = assignmentStrategy;
    }

    public void createIssue(String transactionId, IssueType issueType, String subject, String description, String email) {
        Issue issue = issueService.createIssue(transactionId, issueType, subject, description, email);
        System.out.println(">>> Issue " + issue.getIssueId() + " created for transaction \"" + transactionId + "\"");
    }

    public void addAgent(String agentEmail, String agentName, List<IssueType> expertise) {
        Agent agent = agentService.createAgent(agentEmail, agentName, expertise);
        System.out.println(">>> Agent " + agent.getAgentId() + " created");
    }

    public void assignIssue(String issueId) {
        Optional<Issue> issueOpt = issueService.getIssueById(issueId);
        if (issueOpt.isEmpty()) {
            System.out.println("Issue not found: " + issueId);
            return;
        }

        Issue issue = issueOpt.get();
        Optional<Agent> assignedAgent = assignmentStrategy.assignAgent(issue.getIssueType(),
                agentService.getAllAgents());

        if (assignedAgent.isPresent()) {
            //todo: workhistory Updation extraction
            System.out.println(">>> Issue " + issueId + " assigned to agent " + assignedAgent.get().getAgentId());
        } else {
            System.out.println("No available agent found for issue type: " + issue.getIssueType());
        }
    }

    public void getIssues(Map<String, String> filters) {
        List<Issue> filteredIssues = issueService.getFilteredIssues(filters);

        if (filteredIssues.isEmpty()) {
            System.out.println(">>> No issues found for the given filters.");
        } else {
            filteredIssues.forEach(System.out::println);
        }
    }

    public void updateIssue(String issueId, String status, String resolution) {
        Optional<Issue> issueOpt = issueService.getIssueById(issueId);
        if (issueOpt.isEmpty()) {
            System.out.println("Issue not found: " + issueId);
            return;
        }

        IssueStatus parsedStatus;
        try {
            parsedStatus = IssueStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status provided: " + status);
            return;
        }

        Issue issue = issueOpt.get();
        issue.setStatus(parsedStatus);
        issue.setResolution(resolution);

        System.out.println(">>> Issue " + issueId + " updated to " + parsedStatus);
    }

    public void resolveIssue(String issueId, String resolution) {
        Optional<Issue> issueOpt = issueService.getIssueById(issueId);
        if (issueOpt.isEmpty()) {
            System.out.println("Issue not found: " + issueId);
            return;
        }

        Issue issue = issueOpt.get();
        issue.setStatus(IssueStatus.RESOLVED);
        issue.setResolution(resolution);

        issue.getAssignedAgentId()
                .flatMap(agentService::getAgentById)
                .ifPresent(Agent::markFree);

        System.out.println(">>> Issue " + issueId + " resolved.");
    }

    public void viewAgentsWorkHistory() {
        Collection<Agent> allAgents = agentService.getAllAgents();
        System.out.println(">>> Agent Work Histories:");
        for (Agent agent : allAgents) {
            System.out.println("Agent: " + agent.getAgentId() + " (" + agent.getName() + ")");
            agent.getWorkHistory().forEach(issue ->
                    System.out.println(" - Issue: " + issue.getIssueId() + ", Status: " + issue.getStatus()));
        }
    }
}
