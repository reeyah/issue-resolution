package main.java.com.cxresolution.service;

import main.java.com.cxresolution.entity.Issue;
import main.java.com.cxresolution.enums.IssueStatus;
import main.java.com.cxresolution.enums.IssueType;
import main.java.com.cxresolution.store.IssueStore;
import main.java.com.cxresolution.utils.helper.IdGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class IssueDomainService {
    private final IssueStore issueStore;
    private final IdGenerator idGenerator = new IdGenerator("I");

    public IssueDomainService(IssueStore issueStore) {
        this.issueStore = issueStore;
    }

    public Issue createIssue(String transactionId, IssueType issueType, String subject, String description, String email) {
        String id = idGenerator.generateId();
        Issue issue = new Issue(id, transactionId, issueType, subject, description, email);
        issueStore.addIssue(issue);
        return issue;
    }

    public Optional<Issue> getIssueById(String id) {
        return issueStore.getIssueById(id);
    }

    public List<Issue> getFilteredIssues(Map<String, String> filters) {
        return issueStore.getAllIssues().stream()
                .filter(issue -> filters.entrySet().stream().allMatch(entry -> {
                    //todo: extract filter for o/c
                    switch (entry.getKey().toLowerCase()) {
                        case "email": return issue.getCustomerEmail().equalsIgnoreCase(entry.getValue());
                        case "type": return issue.getIssueType().toString().equalsIgnoreCase(entry.getValue());
                        default: return true;
                    }
                }))
                .collect(Collectors.toList());
    }

    public boolean updateIssue(String issueId, IssueStatus status, String resolution) {
        Optional<Issue> issueOpt = issueStore.getIssueById(issueId);
        issueOpt.ifPresent(issue -> issue.updateStatus(status, resolution));
        return issueOpt.isPresent();
    }

    public boolean resolveIssue(String issueId, String resolution) {
        Optional<Issue> issueOpt = issueStore.getIssueById(issueId);
        issueOpt.ifPresent(issue -> issue.updateStatus(IssueStatus.RESOLVED, resolution));
        return issueOpt.isPresent();
    }
}

