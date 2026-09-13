package ATMInterface.atmapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String type;
    private final double amount;
    private final String description;
    private final LocalDateTime timestamp;

    public Transaction(String type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %-14s R%10.2f  %s",
                timestamp.format(FORMATTER), type, amount, description);
    }
}
