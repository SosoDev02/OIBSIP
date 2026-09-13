package ATMInterface.atmapp;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private final String accountId;
    private final String pin;
    private double balance;
    private final List<Transaction> transactionHistory = new ArrayList<>();

    public Account(String accountId, String pin, double initialBalance) {
        this.accountId = accountId;
        this.pin = pin;
        this.balance = initialBalance;
    }

    public String getAccountId() { return accountId; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }

    public boolean checkPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public boolean hasSufficientFunds(double amount) {
        return balance >= amount;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
}
