package com.crmrealm.leadtracker;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LeadTracker tracker = new LeadTracker();
        try (Scanner scanner = new Scanner(System.in)) {
            run(tracker, scanner);
        }
    }

    static void run(LeadTracker tracker, Scanner scanner) {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine(scanner, "Choose an option: ");
            if (choice == null) { 
                break;
            }
            switch (choice.trim()) {
                case "1" -> handleAddLead(tracker, scanner);
                case "2" -> printLeads(tracker.getLeads());
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Simple Lead Tracker ===");
        System.out.println("1. Add a lead");
        System.out.println("2. View all leads");
        System.out.println("3. Exit");
    }

    private static void handleAddLead(LeadTracker tracker, Scanner scanner) {
        String name = readLine(scanner, "Name: ");
        String email = readLine(scanner, "Email: ");
        String scoreText = readLine(scanner, "Score (0-100): ");
        if (name == null || email == null || scoreText == null) {
            return;
        }

        int score;
        try {
            score = Integer.parseInt(scoreText.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input score must be a whole number between 0 and 100.");
            return;
        }

        try {
            Lead lead = tracker.addLead(name, email, score);
            System.out.println("Lead added: " + lead.name() + " ("
                    + LeadTracker.calculatePriority(lead.score()) + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    static void printLeads(List<Lead> leads) {
        if (leads.isEmpty()) {
            System.out.println("No leads yet");
            return;
        }
        System.out.printf("%-25s %-30s %5s  %s%n", "Name", "Email", "Score", "Priority");
        System.out.println("-".repeat(72));
        for (Lead lead : leads) {
            System.out.printf("%-25s %-30s %5d  %s%n",
                    lead.name(), lead.email(), lead.score(), LeadTracker.calculatePriority(lead.score()));
        }
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }
}
