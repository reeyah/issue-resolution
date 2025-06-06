package main.java.com.cxresolution.utils.assignment;

import main.java.com.cxresolution.entity.Agent;
import main.java.com.cxresolution.enums.IssueType;

import java.util.Collection;
import java.util.Optional;

public interface AssignmentStrategy {
    Optional<Agent> assignAgent(IssueType issueType, Collection<Agent> agents);
}