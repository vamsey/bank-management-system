import java.sql.*;

public class AccountDAO {
    // Create new account
    public int createAccount(int userId, String type, double initialBalance) {
        String query = "INSERT INTO accounts(user_id, account_type, balance) VALUES (?, ?, ?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, type);
            ps.setDouble(3, initialBalance);
            int rows=ps.executeUpdate();
            if(rows>0)
            {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // return the auto-generated account_id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();;
        }
        return -1;
    }

    // Deposit money
    public void deposit(int accountId, double amount) {
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_id=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);
            ps.executeUpdate();
            System.out.println("Deposit successful!");
        } catch (SQLException e) {
            System.out.println("Error depositing: " + e.getMessage());
        }
    }

    // Withdraw money
    public void withdraw(int accountId, double amount) {
        String query = "UPDATE accounts SET balance = balance - ? WHERE account_id=? AND balance >= ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);
            ps.setDouble(3, amount);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Insufficient balance!");
            }
        } catch (SQLException e) {
            System.out.println("Error withdrawing: " + e.getMessage());
        }
    }

    // Check balance
    public void checkBalance(int accountId) {
        String query = "SELECT balance FROM accounts WHERE account_id=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Balance: " + rs.getDouble("balance"));
            } else {
                System.out.println("Account not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error checking balance: " + e.getMessage());
        }
    }

    public void getAccountsByUsername(String username, String password) {
        String userQuery = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        String accQuery = "SELECT account_id, balance FROM accounts WHERE user_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(userQuery)) {

            // First verify username + password
            userStmt.setString(1, username);
            userStmt.setString(2, password);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                int userId = userRs.getInt("user_id");

                try (PreparedStatement accStmt = conn.prepareStatement(accQuery)) {
                    accStmt.setInt(1, userId);
                    ResultSet accRs = accStmt.executeQuery();

                    System.out.println("Your Accounts:");
                    boolean found = false;
                    while (accRs.next()) {
                        int accId = accRs.getInt("account_id");
                        double bal = accRs.getDouble("balance");
                        System.out.println(" - Account ID: " + accId + " | Balance: " + bal);
                        found = true;
                    }
                    if (!found) {
                        System.out.println("❌ No accounts found for this user!");
                    }
                }
            } else {
                System.out.println("❌ Invalid username or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transferAmount(int fromAccId, int toAccId, double amount) {
        String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_id = ?";
        String withdrawQuery = "UPDATE accounts SET balance = balance - ? WHERE account_id = ? AND balance >= ?";
        String depositQuery = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            conn.setAutoCommit(false); // start transaction

            // 1️⃣ Check if fromAccId has enough balance
            double fromBalance = 0;
            try (PreparedStatement ps = conn.prepareStatement(checkBalanceQuery)) {
                ps.setInt(1, fromAccId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    fromBalance = rs.getDouble("balance");
                } else {
                    System.out.println("❌ From Account not found!");
                    return;
                }
            }

            if (fromBalance < amount) {
                System.out.println("❌ Insufficient balance!");
                return;
            }

            // 2️⃣ Withdraw from source account
            try (PreparedStatement ps = conn.prepareStatement(withdrawQuery)) {
                ps.setDouble(1, amount);
                ps.setInt(2, fromAccId);
                ps.setDouble(3, amount);
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    System.out.println("❌ Withdrawal failed!");
                    conn.rollback();
                    return;
                }
            }

            // 3️⃣ Deposit into target account
            try (PreparedStatement ps = conn.prepareStatement(depositQuery)) {
                ps.setDouble(1, amount);
                ps.setInt(2, toAccId);
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    System.out.println("❌ Deposit failed!");
                    conn.rollback();
                    return;
                }
            }

            conn.commit(); // commit transaction
            System.out.println("✅ Transfer successful! Amount: " + amount);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Transfer failed due to error.");
        }
    }

    public int getAccountIdByUserId(int userId) {
        String query = "SELECT account_id FROM accounts WHERE user_id=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("account_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void transferByMobile(int fromAccId, String targetMobile, double amount) {
        String getTarget = "SELECT account_id FROM accounts WHERE mobile_number=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(getTarget)) {
            ps.setString(1, targetMobile);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int toAccId = rs.getInt("account_id");
                transferAmount(fromAccId, toAccId, amount);
            } else {
                System.out.println("❌ Target mobile not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAccountDetails(int accountId) {
        String sql = "SELECT holder_name, mobile_number, balance, account_number FROM accounts WHERE account_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("------ Account Details ------");
                System.out.println("Holder Name  : " + rs.getString("holder_name"));
                System.out.println("Mobile Number: " + rs.getString("mobile_number"));
                System.out.println("Account No   : " + rs.getLong("account_number"));
                System.out.println("Balance      : " + rs.getDouble("balance"));
                System.out.println("-----------------------------");
            } else {
                System.out.println("⚠️ Account not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
