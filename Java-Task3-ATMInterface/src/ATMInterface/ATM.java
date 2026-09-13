package ATMInterface;

import ATMInterface.atmapp.Account;
import ATMInterface.atmapp.Bank;
import ATMInterface.atmapp.Transaction;

import java.util.Scanner;

public class ATM {

    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private final Bank bank;
    private final Scanner scanner;
    private Account currentAccount;

    public ATM(Bank bank, Scanner scanner) {
        this.bank = bank;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("=== Welcome to the ATM ===");
        if (login()) {
            showMenu();
        } else {
            System.out.println("Too many incorrect attempts. Access denied.");
        }
    }

    private boolean login() {
        int attempts = 0;
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine().trim();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();

            Account account = bank.authenticate(userId, pin);
            if (account != null) {
                currentAccount = account;
                System.out.println("Login successful. Welcome, " + account.getAccountId() + "!");
                return true;
            }

            attempts++;
            System.out.println("Incorrect User ID or PIN. Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - attempts));
        }
        return false;
    }

    private void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== Main Menu =====");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showTransactionHistory();
                case "2" -> withdraw();
                case "3" -> deposit();
                case "4" -> transfer();
                case "5" -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
        System.out.println("Thank you for using the ATM. Goodbye!");
    }

    private void showTransactionHistory() {
        System.out.println();
        System.out.println("===== Transaction History =====");
        if (currentAccount.getTransactionHistory().isEmpty()) {
            System.out.println("No transactions yet this session.");
            return;
        }
        for (Transaction t : currentAccount.getTransactionHistory()) {
            System.out.println(t);
        }
    }

    private void withdraw() {
        double amount = readAmount("Enter amount to withdraw: ");
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        if (!currentAccount.hasSufficientFunds(amount)) {
            System.out.println("Insufficient Funds");
            return;
        }
        currentAccount.withdraw(amount);
        currentAccount.addTransaction(new Transaction("WITHDRAW", amount,
                "New balance: R" + String.format("%.2f", currentAccount.getBalance())));
        System.out.printf("Withdrawal successful. New balance: R%.2f%n", currentAccount.getBalance());
    }

    private void deposit() {
        double amount = readAmount("Enter amount to deposit: ");
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        currentAccount.deposit(amount);
        currentAccount.addTransaction(new Transaction("DEPOSIT", amount,
                "New balance: R" + String.format("%.2f", currentAccount.getBalance())));
        System.out.printf("Deposit successful. New balance: R%.2f%n", currentAccount.getBalance());
    }

    private void transfer() {
        System.out.print("Enter recipient account ID: ");
        String recipientId = scanner.nextLine().trim();

        if (recipientId.equals(currentAccount.getAccountId())) {
            System.out.println("Cannot transfer to your own account.");
            return;
        }

        Account recipient = bank.findAccount(recipientId);
        if (recipient == null) {
            System.out.println("Recipient account not found.");
            return;
        }

        double amount = readAmount("Enter amount to transfer: ");
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        if (!currentAccount.hasSufficientFunds(amount)) {
            System.out.println("Insufficient Funds");
            return;
        }

        currentAccount.withdraw(amount);
        recipient.deposit(amount);

        currentAccount.addTransaction(new Transaction("TRANSFER OUT", amount,
                "To " + recipient.getAccountId() + " | New balance: R" + String.format("%.2f", currentAccount.getBalance())));
        recipient.addTransaction(new Transaction("TRANSFER IN", amount,
                "From " + currentAccount.getAccountId() + " | New balance: R" + String.format("%.2f", recipient.getBalance())));

        System.out.printf("Transfer successful. New balance: R%.2f%n", currentAccount.getBalance());
    }

    private double readAmount(String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
