package main.java.com.cxresolution.entity;

import main.java.com.cxresolution.enums.IssueStatus;
import main.java.com.cxresolution.enums.IssueType;

import java.util.Objects;
import java.util.Optional;

public class Issue {
    private final String issueId;
    private final String transactionId;
    private final IssueType issueType;
    private final String subject;
    private final String description;
    private final String customerEmail;

    private IssueStatus status;
    private Optional<String> resolution;
    private Optional<String> assignedAgentId;

    public Issue(String issueId, String transactionId, IssueType issueType, String subject,
                 String description, String customerEmail) {
        this.issueId = issueId;
        this.transactionId = transactionId;
        this.issueType = issueType;
        this.subject = subject;
        this.description = description;
        this.customerEmail = customerEmail;
        this.status = IssueStatus.OPEN;
        this.resolution = Optional.empty();
        this.assignedAgentId = Optional.empty();
    }

    public String getIssueId() { return issueId; }
    public String getTransactionId() { return transactionId; }
    public IssueType getIssueType() { return issueType; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public String getCustomerEmail() { return customerEmail; }

    public IssueStatus getStatus() { return status; }
    public Optional<String> getResolution() { return resolution; }
    public Optional<String> getAssignedAgentId() { return assignedAgentId; }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public void setResolution(String resolution) {
        this.resolution = Optional.ofNullable(resolution);
    }

    public void setAssignedAgent(String assignedAgentId) {
        this.assignedAgentId = Optional.of(assignedAgentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Issue)) return false;
        Issue issue = (Issue) o;
        return Objects.equals(issueId, issue.issueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueId);
    }

    public void assignToAgent(String agentId) {
        this.assignedAgentId = Optional.of(agentId);
    }

    public void updateStatus(IssueStatus status, String resolution) {
        this.status = status;
        this.resolution = Optional.ofNullable(resolution);
    }

    @Override
    public String toString() {
        return String.format("%s {Transaction: %s, Type: %s, Subject: %s, Desc: %s, Email: %s, Status: %s}",
                issueId, transactionId, issueType, subject, description, customerEmail, status);
    }
}