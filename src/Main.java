import java.sql.SQLOutput;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Bank bank =new Bank();
        Scanner sc=new Scanner(System.in);
        while(true)
        {
            System.out.println("-------------Menu-----------");
            System.out.println("1.Add Account");
            System.out.println("2.Deposit");
            System.out.println("3.withdraw");
            System.out.println("4.Transfer");
            System.out.println("5.check Balance");
            System.out.println("6.Exit");

            int choice=sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Account Number: ");
                    String accNo = sc.nextLine();
                    System.out.print("Enter Account Holder: ");
                    String holder = sc.nextLine();
                    System.out.print("Enter Initial Balance: ");
                    double bal = sc.nextDouble();
                    bank.addAccount(new Account(accNo, holder, bal));
                }
                case 2 -> {
                    System.out.print("Enter Account Number: ");
                    String accNo = sc.nextLine();
                    Account acc = bank.findAccount(accNo);
                    if (acc != null) {
                        System.out.print("Enter Amount: ");
                        double amt = sc.nextDouble();
                        acc.deposit(amt);
                    } else System.out.println("Account not found!");
                }
                case 3 -> {
                    System.out.print("Enter Account Number: ");
                    String accNo = sc.nextLine();
                    Account acc = bank.findAccount(accNo);
                    if (acc != null) {
                        System.out.print("Enter Amount: ");
                        double amt = sc.nextDouble();
                        acc.withdraw(amt);
                    } else System.out.println("Account not found!");
                }
                case 4 -> {
                    System.out.print("Enter From Account: ");
                    String from = sc.nextLine();
                    System.out.print("Enter To Account: ");
                    String to = sc.nextLine();
                    System.out.print("Enter Amount: ");
                    double amt = sc.nextDouble();
                    bank.transferAmount(from, to, amt);
                }
                case 5 -> {
                    System.out.print("Enter Account Number: ");
                    String accNo = sc.nextLine();
                    Account acc = bank.findAccount(accNo);
                    if (acc != null) acc.checkBalance();
                    else System.out.println("Account not found!");
                }
                case 6 -> {
                    System.out.println("Exiting... Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }



    }
}