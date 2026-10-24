package com.crmrealm.leadtracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeadTracker {

    private final List<Lead> leads = new ArrayList<>();
    public Lead addLead(String name, String email, int score) {
        Lead lead = new Lead(name, email, score);
        leads.add(lead);
        return lead;
    }

    public List<Lead> getLeads() {
        return Collections.unmodifiableList(leads);
    }
    public static Priority calculatePriority(int score) {
        if (score < Lead.MIN_SCORE || score > Lead.MAX_SCORE) {
            throw new IllegalArgumentException(
                    "Score must be between " + Lead.MIN_SCORE + " and " + Lead.MAX_SCORE + " (got " + score + ").");
        }
        if (score >= 80) {
            return Priority.HOT;
        }
        if (score >= 50) {
            return Priority.WARM;
        }
        return Priority.COLD;
    }
}
