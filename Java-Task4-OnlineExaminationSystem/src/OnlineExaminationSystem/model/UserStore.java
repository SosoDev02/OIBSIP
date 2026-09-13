package OnlineExaminationSystem.model;

import java.util.HashMap;
import java.util.Map;

public class UserStore {

    private static class UserRecord {
        String password;
        String displayName;
        UserRecord(String password, String displayName) {
            this.password = password;
            this.displayName = displayName;
        }
    }

    private final Map<String, UserRecord> users = new HashMap<>();

    public UserStore() {
        // Seed accounts. Replace with real persistence (DB/file) as needed.
        users.put("licebo", new UserRecord("maco123", "Licebo"));
        users.put("soso", new UserRecord("sino0206", "Soso"));
    }

    public boolean authenticate(String username, String password) {
        UserRecord record = users.get(username);
        return record != null && record.password.equals(password);
    }

    public String getDisplayName(String username) {
        UserRecord record = users.get(username);
        return record == null ? username : record.displayName;
    }

    public void updateProfile(String username, String newDisplayName, String newPassword) {
        UserRecord record = users.get(username);
        if (record == null) return;
        if (newDisplayName != null && !newDisplayName.isBlank()) {
            record.displayName = newDisplayName;
        }
        if (newPassword != null && !newPassword.isBlank()) {
            record.password = newPassword;
        }
    }
}
