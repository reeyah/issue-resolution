package main.java.com.cxresolution.service;

import main.java.com.cxresolution.entity.Agent;
import main.java.com.cxresolution.entity.Issue;
import main.java.com.cxresolution.enums.IssueType;
import main.java.com.cxresolution.enums.IssueStatus;
import main.java.com.cxresolution.service.AssignmentService.AssignmentResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IssueResolutionSystem {

    private final AgentDomainService agentService;
    private final IssueDomainService issueService;
    private final AssignmentService assignmentService;

    public IssueResolutionSystem(AgentDomainService agentService,
                                 IssueDomainService issueService,
                                 AssignmentService assignmentService) {
        this.agentService = agentService;
        this.issueService = issueService;
        this.assignmentService = assignmentService;
    }

    public void createIssue(String transactionId, String issueTypeStr, String subject, String description, String email) {
        try {
            IssueType issueType = IssueType.fromDisplayName(issueTypeStr);
            Issue issue = issueService.createIssue(transactionId, issueType, subject, description, email);
            System.out.println(">>> Issue " + issue.getIssueId() + " created against transaction \"" + transactionId + "\"");
        } catch (IllegalArgumentException e) {
            System.out.println(">>> Error: Invalid issue type - " + issueTypeStr);
        }
    }

    public void addAgent(String agentEmail, String agentName, List<String> expertiseStrings) {
        try {
            List<IssueType> expertise = expertiseStrings.stream()
                    .map(IssueType::fromDisplayName)
                    .toList();
            Agent agent = agentService.createAgent(agentEmail, agentName, expertise);
            System.out.println(">>> Agent " + agent.getAgentId() + " created");
        } catch (IllegalArgumentException e) {
            System.out.println(">>> Error: Invalid expertise type in list");
        }
    }

    public void assignIssue(String issueId) {
        Optional<Issue> issueOpt = issueService.getIssueById(issueId);
        if (issueOpt.isEmpty()) {
            System.out.println(">>> Issue not found: " + issueId);
            return;
        }

        Issue issue = issueOpt.get();

        if (issue.getAssignedAgentId().isPresent()) {
            System.out.println(">>> Issue " + issueId + " is already assigned to agent " +
                    issue.getAssignedAgentId().get());
            return;
        }

        AssignmentResult result = assignmentService.assignIssue(issue);

        if (result.isSuccessful()) {
            String agentId = result.getAssignedAgent().get().getAgentId();
            if (result.getMessage().contains("waitlist")) {
                System.out.println(">>> Issue " + issueId + " added to waitlist of Agent " + agentId);
            } else {
                System.out.println(">>> Issue " + issueId + " assigned to agent " + agentId);
            }
        } else {
            System.out.println(">>> No available agent found for issue type: " + issue.getIssueType().getDisplayName());
        }
    }

    public void resolveIssue(String issueId, String resolution) {
        Optional<Issue> issueOpt = issueService.getIssueById(issueId);
        if (issueOpt.isEmpty()) {
            System.out.println(">>> Issue not found: " + issueId);
            return;
        }

        Issue issue = issueOpt.get();

        issue.updateStatus(IssueStatus.RESOLVED, resolution);

        issue.getAssignedAgentId()
                .flatMap(agentService::getAgentById)
                .ifPresent(agent -> agent.resolveCurrentIssue(resolution));

        System.out.println(">>> " + issueId + " issue marked resolved");
    }

    public void getIssues(Map<String, String> filters) {
        List<Issue> filteredIssues = issueService.getFilteredIssues(filters);
        if (filteredIssues.isEmpty()) {
            System.out.println(">>> No issues found for the given filters.");
        } else {
            filteredIssues.forEach(issue -> {
                System.out.println(issue.toString());
            });
        }
    }

    public void updateIssue(String issueId, String status, String resolution) {
        Optional<Issue> issueOpt = issueService.getIssueById(issueId);
        if (issueOpt.isEmpty()) {
            System.out.println(">>> Issue not found: " + issueId);
            return;
        }

        try {
            IssueStatus parsedStatus = IssueStatus.valueOf(status.toUpperCase().replace(" ", "_"));
            Issue issue = issueOpt.get();
            issue.updateStatus(parsedStatus, resolution);
            System.out.println(">>> " + issueId + " status updated to " + parsedStatus);
        } catch (IllegalArgumentException e) {
            System.out.println(">>> Invalid status provided: " + status);
        }
    }

    public void viewAgentsWorkHistory() {
        Collection<Agent> allAgents = agentService.getAllAgents();
        System.out.println(">>> Agent Work Histories:");

        if (allAgents.isEmpty()) {
            System.out.println("No agents found.");
            return;
        }

        allAgents.forEach(agent -> {
            System.out.print(agent.getAgentId() + " -> {");
            String workHistory = agent.getWorkHistory().stream()
                    .map(Issue::getIssueId)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            System.out.println(workHistory + "}");
        });
    }
}
