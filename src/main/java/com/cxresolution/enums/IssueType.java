package main.java.com.cxresolution.enums;

public enum IssueType {
    PAYMENT_RELATED("Payment Related"),
    MUTUAL_FUND_RELATED("Mutual Fund Related"),
    GOLD_RELATED("Gold Related"),
    INSURANCE_RELATED("Insurance Related");

    private final String displayName;

    IssueType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    public static IssueType fromDisplayName(String displayName) {
        for (IssueType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown issue type: " + displayName);
    }
}