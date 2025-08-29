import java.util.ArrayList;

public class Bank {

    private ArrayList<Account> accounts=new ArrayList<>();

    public void addAccount(Account account)
    {
        if(findAccount(account.getAccountNumber())!=null)
        {
            System.out.println("Account Number already exists");
            return;
        }
        accounts.add(account);
        System.out.println("Account added successfully");
    }

    public Account findAccount(String accountNumber)
    {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null;
    }

    public void transferAmount(String fromAccountNumber,String toAccountNumber,double amount)
    {
        Account fromAccount=findAccount(fromAccountNumber);
        Account toAccount=findAccount(toAccountNumber);
        if(fromAccount==null || toAccount ==null)
        {
            System.out.println("Invald Acc Numbers!!! check once Again");
            return;
        }
        if(fromAccount.getBalance()<amount)
        {
            System.out.println("Insufficient Funds Can't Transfer");
            return;
        }
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        System.out.println(amount+" Transferred successfully from "+fromAccount.getAccountHolder()+" to "+toAccount.getAccountHolder());

    }

}
