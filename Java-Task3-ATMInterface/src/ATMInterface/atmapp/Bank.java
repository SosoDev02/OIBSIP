package ATMInterface.atmapp;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    private final Map<String, Account> accounts = new HashMap<>();

    public Bank() {
        // Seed accounts. Replace with real persistence (DB/file) as needed.
        accounts.put("1001", new Account("1001", "1234", 5000.00));
        accounts.put("1002", new Account("1002", "4321", 2500.00));
        accounts.put("1003", new Account("1003", "1111", 10000.00));
    }

    public Account authenticate(String accountId, String pin) {
        Account account = accounts.get(accountId);
        return (account != null && account.checkPin(pin)) ? account : null;
    }

    public Account findAccount(String accountId) {
        return accounts.get(accountId);
    }
}
