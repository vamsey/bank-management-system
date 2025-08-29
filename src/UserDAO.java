import java.sql.*;

public class UserDAO {
    // Register new user
    public boolean registerUser(String username, String password) {
        String query = "INSERT INTO users(username, password) VALUES (?, ?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username.trim());
            ps.setString(2, password.trim()); // plain text for now (later: hashing!)
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    // Login user
    public boolean loginUser(String username, String password) {
        String query = "SELECT user_id FROM users WHERE username=? AND password=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username.trim());
            ps.setString(2, password.trim());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

