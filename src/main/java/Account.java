public class Account {
    private String accountId;
    private double balance;
    private boolean isActive;

    public Account(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.isActive = true;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deposit(double amount){
        if(amount>0){
            this.balance += amount;
        }
    }

    public boolean withdraw(double amount){
        if(isActive && amount > 0 && balance>=amount){
            this.balance -=amount;
            return true;
        }
        return false;
    }

    public void deactivate(){
        this.isActive = false;
    }
}