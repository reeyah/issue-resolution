package main.java.com.cxresolution.entity;

import main.java.com.cxresolution.enums.IssueType;
import main.java.com.cxresolution.enums.IssueStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;


public class Agent {
    private final String agentId;
    private final String name;
    private final String email;
    private final Set<IssueType> expertise;

    private Optional<Issue> currentIssue;
    private final Queue<Issue> waitingQueue;
    private final List<Issue> workHistory;

    public Agent(String agentId, String name, String email, List<IssueType> expertiseList) {
        this.agentId = agentId;
        this.name = name;
        this.email = email;
        this.expertise = new HashSet<>(expertiseList);
        this.currentIssue = Optional.empty();
        this.waitingQueue = new LinkedList<>();
        this.workHistory = new ArrayList<>();
    }

    public String getAgentId() { return agentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public Set<IssueType> getExpertise() {
        return Collections.unmodifiableSet(expertise);
    }

    public void markFree() {
        this.currentIssue = Optional.empty();
        if (!waitingQueue.isEmpty()) {
            assignIssue(waitingQueue.poll());
        }
    }

    public boolean isFree() {
        return currentIssue.isEmpty();
    }

    public void assignIssue(Issue issue) {
        if (isFree()) {
            currentIssue = Optional.of(issue);
            issue.assignToAgent(agentId);
            workHistory.add(issue);
        } else {
            waitingQueue.offer(issue);
        }
    }

    public void resolveCurrentIssue(String resolution) {
        currentIssue.ifPresent(issue -> {
            issue.updateStatus(IssueStatus.RESOLVED, resolution);
            currentIssue = Optional.empty();

            if (!waitingQueue.isEmpty()) {
                assignIssue(waitingQueue.poll());
            }
        });
    }

    public List<Issue> getWorkHistory() {
        return Collections.unmodifiableList(workHistory);
    }

}
