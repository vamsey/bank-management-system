import java.sql.SQLOutput;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        AccountDAO accountDAO = new AccountDAO();

        System.out.println("====Welcome to GOTHAM CITY Bank====");
        System.out.println("1.Register");
        System.out.println("2.Login");
        int option=sc.nextInt();
        sc.nextLine();

        Account currentAccount = null;

        if (option == 1) {
            // --- Registration ---
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            System.out.print("Enter Account Holder Name: ");
            String holderName = sc.nextLine();
            System.out.print("Enter Mobile Number: ");
            String mobile = sc.nextLine();

            userDAO.registerUser(username, password, holderName, mobile);

            // After registration, login directly
            currentAccount = userDAO.loginUser(username, password);

        } else if (option == 2) {
            // --- Login ---
            System.out.print("Enter Username: ");
            String username = sc.nextLine();
            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            currentAccount = userDAO.loginUser(username, password);
        } else {
            System.out.println("❌ Invalid option. Exiting.");
            return;
        }

        if (currentAccount == null) {
            System.out.println("❌ Could not login. Exiting.");
            return;
        }

        // --- Banking Menu ---
        while (true) {
            System.out.println("\n------------- Menu -----------");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer (by Mobile Number)");
            System.out.println("4. View My Account");
            System.out.println("5. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Amount to Deposit: ");
                    double amt = sc.nextDouble();
                    accountDAO.deposit(currentAccount.getAccountId(), amt);
                }
                case 2 -> {
                    System.out.print("Enter Amount to Withdraw: ");
                    double amt = sc.nextDouble();
                    accountDAO.withdraw(currentAccount.getAccountId(), amt);
                }
                case 3 -> {
                    System.out.print("Enter Target Mobile Number: ");
                    String targetMobile = sc.nextLine();
                    System.out.print("Enter Amount: ");
                    double amt = sc.nextDouble();
                    accountDAO.transferByMobile(currentAccount.getAccountId(), targetMobile, amt);
                }
                case 4 -> {
                    accountDAO.viewAccountDetails(currentAccount.getAccountId());
                }
                case 5 -> {
                    System.out.println("👋 Exiting... Thank you for banking with us!");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice! Try again.");
            }
        }
    }
}