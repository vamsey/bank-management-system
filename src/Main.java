import java.sql.SQLOutput;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        AccountDAO accountDAO = new AccountDAO();

        System.out.println("====Welcome to Amalapuram Bank====");
        System.out.println("1.Register");
        System.out.println("2.Login");
        int option=sc.nextInt();
        sc.nextLine();

        String username;
        String password;
        int currentUserId=-1;

        if(option==1)
        {
            System.out.println("Enter username: ");
            username=sc.nextLine();
            System.out.println("Enter Password:  ");
            password=sc.nextLine();

            if(userDAO.registerUser(username,password))
            {
                System.out.println("✅ Registration successful!");
            }
            else{
                System.out.println("❌ Registration failed!");
                return;
            }
        }
        else {
            System.out.println("Enter UserName: ");
            username = sc.nextLine();
            System.out.print("Enter password: ");
            password = sc.nextLine();
            if (!userDAO.loginUser(username, password)) {
                System.out.println("❌ Invalid credentials!");
                return;
            } else {
                System.out.println("✅ Login successful!");
                // For simplicity, get user_id
                currentUserId = UserUtil.getUserIdByUsername(username); // helper function
            }
        }


        while(true)
        {
            System.out.println("-------------Menu-----------");
            System.out.println("1.Add Account");
            System.out.println("2.Deposit");
            System.out.println("3.withdraw");
            System.out.println("4.Transfer");
            System.out.println("5.check Balance");
            System.out.println("6.View My Accounts");
            System.out.println("7.EXIT");

            int choice=sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Account Type (Savings/Current): ");
                    String type = sc.nextLine();
                    System.out.print("Enter Initial Balance: ");
                    double bal = sc.nextDouble();
                    int userId = UserUtil.getUserIdByUsername(username);
                    int accId = accountDAO.createAccount(userId, type, bal);
                    if (accId != -1) {
                        System.out.println("✅ Account created successfully! Your Account ID: " + accId);
                    } else {
                        System.out.println("❌ Failed to create account!");
                    }
                }
                case 2 -> {
                    System.out.print("Enter Account ID: ");
                    int accId = sc.nextInt();
                    System.out.print("Enter Amount: ");
                    double amt = sc.nextDouble();
                    accountDAO.deposit(accId, amt);
                }
                case 3 -> {
                    System.out.print("Enter Account ID: ");
                    int accId = sc.nextInt();
                    System.out.print("Enter Amount: ");
                    double amt = sc.nextDouble();
                    accountDAO.withdraw(accId, amt);
                }
                case 4 -> {
                    System.out.print("Enter From Account ID: ");
                    int from = sc.nextInt();
                    System.out.print("Enter To Account ID: ");
                    int to = sc.nextInt();
                    System.out.print("Enter Amount: ");
                    double amt = sc.nextDouble();
                    accountDAO.transferAmount(from, to, amt);
                }
                case 5 -> {
                    System.out.print("Enter Account ID: ");
                    int accId = sc.nextInt();
                    accountDAO.checkBalance(accId);
                }
                case 6 -> {
                    accountDAO.getAccountsByUsername(username, password);
                }
                case 7 -> {
                    System.out.println("Exiting... Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }

    }
}