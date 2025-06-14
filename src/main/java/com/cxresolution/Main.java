package main.java.com.cxresolution;

import main.java.com.cxresolution.enums.IssueType;
import main.java.com.cxresolution.service.*;
import main.java.com.cxresolution.store.*;
import main.java.com.cxresolution.utils.assignment.*;
import main.java.com.cxresolution.utils.assignment.impl.FirstAvailableAgentStrategy;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AgentStore agentStore = new AgentStore();
        IssueStore issueStore = new IssueStore();
        AgentDomainService agentService = new AgentDomainService(agentStore);
        IssueDomainService issueService = new IssueDomainService(issueStore);
        FirstAvailableAgentStrategy strategy = new FirstAvailableAgentStrategy();
        AssignmentService assignmentService = new AssignmentService(strategy, agentService);
        IssueResolutionSystem system = new IssueResolutionSystem(agentService, issueService, assignmentService);

        system.createIssue("T1", "Payment Related", "Payment Failed", "My payment failed but money is debited", "testUser1@test.com");
        system.createIssue("T2", "Mutual Fund Related", "Purchase Failed", "Unable to purchase Mutual Fund", "testUser2@test.com");
        system.createIssue("T3", "Payment Related", "Payment Failed", "My payment failed but money is debited", "testUser2@test.com");

        system.addAgent("agent1@test.com", "Agent 1", Arrays.asList("Payment Related", "Gold Related"));
        system.addAgent("agent2@test.com", "Agent 2", Arrays.asList("Mutual Fund Related"));

        system.assignIssue("I1");
        system.assignIssue("I2");
        system.assignIssue("I3");

        while (true) {
            System.out.println("\n==== Issue Resolution System ====");
            System.out.println("1. Add Agent");
            System.out.println("2. Create Issue");
            System.out.println("3. Assign Issue");
            System.out.println("4. View Issues with Filters");
            System.out.println("5. Resolve Issue");
            System.out.println("6. Update Issue");
            System.out.println("7. View Agents Work History");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Agent Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Agent Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Expertise (comma separated): ");
                    List<String> expertise = Arrays.stream(scanner.nextLine().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();
                    system.addAgent(email, name, expertise);
                    break;

                case "2":
                    System.out.print("Transaction ID: ");
                    String txnId = scanner.nextLine();

                    System.out.print("Issue Type (e.g., PAYMENT_RELATED, MUTUAL_FUND_RELATED): ");
                    String issueType = scanner.nextLine();

                    System.out.print("Subject: ");
                    String subject = scanner.nextLine();
                    System.out.print("Description: ");
                    String desc = scanner.nextLine();
                    System.out.print("Customer Email: ");
                    String custEmail = scanner.nextLine();

                    system.createIssue(txnId, issueType, subject, desc, custEmail);
                    break;


                case "3":
                    System.out.print("Issue ID to assign: ");
                    String assignId = scanner.nextLine();
                    system.assignIssue(assignId);
                    break;

                case "4":
                    Map<String, String> filters = new HashMap<>();
                    System.out.print("Filter by email (optional): ");
                    String filterEmail = scanner.nextLine();
                    if (!filterEmail.isBlank()) filters.put("email", filterEmail);

                    System.out.print("Filter by type (optional): ");
                    String filterType = scanner.nextLine();
                    if (!filterType.isBlank()) filters.put("type", filterType);

                    system.getIssues(filters);
                    break;

                case "5":
                    System.out.print("Issue ID to resolve: ");
                    String resolveId = scanner.nextLine();
                    System.out.print("Resolution text: ");
                    String resolution = scanner.nextLine();
                    system.resolveIssue(resolveId, resolution);
                    break;

                case "6":
                    System.out.print("Issue ID to update: ");
                    String updateId = scanner.nextLine();
                    System.out.print("New Status (OPEN, IN_PROGRESS, RESOLVED): ");
                    String status = scanner.nextLine();
                    System.out.print("Resolution (if any): ");
                    String updateResolution = scanner.nextLine();
                    system.updateIssue(updateId, status, updateResolution);
                    break;

                case "7":
                    system.viewAgentsWorkHistory();
                    break;

                case "0":
                    System.out.println("Exiting. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
