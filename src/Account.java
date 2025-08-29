public class Account {
    private String accountNumber;
    private String accountHolder;
    protected double balance;

    public Account(String accountNumber,String accountHolder,double balance )
    {
        this.accountNumber=accountNumber;
        this.accountHolder=accountHolder;
        this.balance=balance;
    }

    public void deposit(double amount)
    {
        if(amount<0)
        {
            System.out.println("deposits must be in positive");
            return;
        }
        balance+=amount;
        System.out.println(amount+" added succesfully"+" ACC balance:"+balance);
    }

    public void withdraw(double amount)
    {
        if(amount<0)
        {
            System.out.println("withdrawls must be in positive");
            return;
        }
        if(amount>balance)
        {
            System.out.println("insuffient balance can't withdraw!!");
            return;
        }
        balance-=amount;
        System.out.println(amount+"withdrawed successfully"+"Acc balance:"+balance);
    }

    public void checkBalance()
    {
        System.out.println("current Balance: "+balance);
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public String getAccountHolder()
    {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

}
