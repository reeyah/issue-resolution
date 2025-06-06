package main.java.com.cxresolution.store;

import main.java.com.cxresolution.entity.Issue;

import java.util.*;
import java.util.stream.Collectors;


public class IssueStore {
    private final Map<String, Issue> issuesById = new HashMap<>();

    public void addIssue(Issue issue) {
        issuesById.put(issue.getIssueId(), issue);
    }

    public Optional<Issue> getIssueById(String issueId) {
        return Optional.ofNullable(issuesById.get(issueId));
    }

    public Collection<Issue> getAllIssues() {
        return Collections.unmodifiableCollection(issuesById.values());
    }
}