package com.crmrealm.leadtracker;
public class LeadTrackerTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        priorityBoundaries();
        validLeadIsAdded();
        invalidLeadsAreRejected();
        leadsKeepInsertionOrderAndAreReadOnly();

        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void priorityBoundaries() {
        check("0 -> COLD", LeadTracker.calculatePriority(0) == Priority.COLD);
        check("49 -> COLD", LeadTracker.calculatePriority(49) == Priority.COLD);
        check("50 -> WARM", LeadTracker.calculatePriority(50) == Priority.WARM);
        check("79 -> WARM", LeadTracker.calculatePriority(79) == Priority.WARM);
        check("80 -> HOT", LeadTracker.calculatePriority(80) == Priority.HOT);
        check("100 -> HOT", LeadTracker.calculatePriority(100) == Priority.HOT);
        check("priority of -1 throws", throwsIae(() -> LeadTracker.calculatePriority(-1)));
        check("priority of 101 throws", throwsIae(() -> LeadTracker.calculatePriority(101)));
    }

    private static void validLeadIsAdded() {
        LeadTracker tracker = new LeadTracker();
        tracker.addLead("  Ada Lovelace ", " ada@example.com ", 100);
        tracker.addLead("Zero Score", "zero@example.com", 0);
        check("two leads stored", tracker.getLeads().size() == 2);
        check("name is trimmed", tracker.getLeads().get(0).name().equals("Ada Lovelace"));
        check("email is trimmed", tracker.getLeads().get(0).email().equals("ada@example.com"));
        check("boundary scores 0 and 100 accepted", tracker.getLeads().get(1).score() == 0);
    }

    private static void invalidLeadsAreRejected() {
        LeadTracker tracker = new LeadTracker();
        check("empty name rejected", throwsIae(() -> tracker.addLead("", "a@b.com", 50)));
        check("blank name rejected", throwsIae(() -> tracker.addLead("   ", "a@b.com", 50)));
        check("null name rejected", throwsIae(() -> tracker.addLead(null, "a@b.com", 50)));
        check("empty email rejected", throwsIae(() -> tracker.addLead("Ann", "", 50)));
        check("blank email rejected", throwsIae(() -> tracker.addLead("Ann", "  ", 50)));
        check("score -1 rejected", throwsIae(() -> tracker.addLead("Ann", "a@b.com", -1)));
        check("score 101 rejected", throwsIae(() -> tracker.addLead("Ann", "a@b.com", 101)));
        check("nothing stored after rejections", tracker.getLeads().isEmpty());
    }

    private static void leadsKeepInsertionOrderAndAreReadOnly() {
        LeadTracker tracker = new LeadTracker();
        tracker.addLead("First", "1@x.com", 10);
        tracker.addLead("Second", "2@x.com", 90);
        check("insertion order kept", tracker.getLeads().get(0).name().equals("First"));
        boolean unmodifiable = false;
        try {
            tracker.getLeads().clear();
        } catch (UnsupportedOperationException e) {
            unmodifiable = true;
        }
        check("returned list is read-only", unmodifiable);
    }

    private static boolean throwsIae(Runnable action) {
        try {
            action.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static void check(String label, boolean condition) {
        if (condition) {
            passed++;
        } else {
            failed++;
            System.out.println("FAIL: " + label);
        }
    }
}
