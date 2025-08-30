import java.sql.*;

public class UserDAO {
    // Register new user
    public void registerUser(String username, String password, String holderName, String mobileNumber) {
        String insertUser = "INSERT INTO users (username, password) VALUES (?, ?)";
        String insertAccount = "INSERT INTO accounts (user_id, holder_name, mobile_number, balance, account_number) VALUES (?, ?, ?, 0.0, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement psUser = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Insert into users table
            psUser.setString(1, username);
            psUser.setString(2, password);
            psUser.executeUpdate();

            // 2. Get generated user_id
            ResultSet rs = psUser.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            // 3. Generate account number (unique, 10-digit)
            long accountNumber = 1000000000L + userId;

            // 4. Insert into accounts table
            try (PreparedStatement psAcc = conn.prepareStatement(insertAccount)) {
                psAcc.setInt(1, userId);
                psAcc.setString(2, holderName);
                psAcc.setString(3, mobileNumber);
                psAcc.setLong(4, accountNumber);
                psAcc.executeUpdate();
            }

            System.out.println("✅ User registered successfully!");
            System.out.println("👉 Your Account Number: " + accountNumber);

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ Username or Mobile Number already exists. Try again.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Login user

    public Account loginUser(String username, String password) {
        String queryUser = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        String queryAccount = "SELECT account_id, holder_name, mobile_number, balance, account_number " +
                "FROM accounts WHERE user_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement psUser = conn.prepareStatement(queryUser)) {

            // 1. Verify user credentials
            psUser.setString(1, username);
            psUser.setString(2, password);
            ResultSet rsUser = psUser.executeQuery();

            if (rsUser.next()) {
                int userId = rsUser.getInt("user_id");

                // 2. Fetch account info
                try (PreparedStatement psAcc = conn.prepareStatement(queryAccount)) {
                    psAcc.setInt(1, userId);
                    ResultSet rsAcc = psAcc.executeQuery();

                    if (rsAcc.next()) {
                        // Create Account object with details
                        Account acc = new Account();
                        acc.setAccountId(rsAcc.getInt("account_id"));
                        acc.setHolderName(rsAcc.getString("holder_name"));
                        acc.setMobileNumber(rsAcc.getString("mobile_number"));
                        acc.setBalance(rsAcc.getDouble("balance"));
                        acc.setAccountNumber(rsAcc.getLong("account_number"));

                        System.out.println("✅ Login successful! Welcome " + acc.getHolderName());
                        return acc;
                    }
                }
            } else {
                System.out.println("⚠️ Invalid username or password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // login failed
    }

}

