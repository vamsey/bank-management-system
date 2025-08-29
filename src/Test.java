
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
    public class Test{
        public static void main(String[] args) {
            String url = "jdbc:mysql://localhost:3306/bankdb1"; // database name
            String user = "root"; // your MySQL username
            String password = "chandravamsi@1221"; // your MySQL password

            try {
                // Load MySQL driver (optional in newer JDBC versions)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Connect to DB
                Connection conn = DriverManager.getConnection(url, user, password);

                if (conn != null) {
                    System.out.println("✅ Connected to the database successfully!");
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("❌ Connection failed! Error: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("❌ MySQL Driver not found!");
            }
        }
    }

