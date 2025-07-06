import java.util.*;

class BankException extends Exception {
    public BankException(String message) {
        super(message);
    }
}

// Abstract class representing a general Bank Account
abstract class BankAccount {
    private static int nextAccNumber = 1001;
    protected int accountNumber;
    protected String accountHolder;
    protected double balance;

    public BankAccount(String accountHolder, double initialDeposit) throws BankException {
        if (initialDeposit < 0) throw new BankException("Initial deposit cannot be negative!");
        this.accountHolder = accountHolder;
        this.balance = initialDeposit;
        this.accountNumber = nextAccNumber++;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public abstract void deposit(double amount) throws BankException;
    public abstract void withdraw(double amount) throws BankException;

    public void showAccountDetails() {
        System.out.println("Account No : " + accountNumber);
        System.out.println("Name       : " + accountHolder);
        System.out.println("Balance    : " + balance);
        System.out.println("----------------------------");
    }
}

// Concrete implementation of a Savings Account
class SavingsAccount extends BankAccount {

    public SavingsAccount(String accountHolder, double initialDeposit) throws BankException {
        super(accountHolder, initialDeposit);
    }

    @Override
    public void deposit(double amount) throws BankException {
        if (amount <= 0)
            throw new BankException("Deposit amount must be positive.");
        balance += amount;
    }

    @Override
    public void withdraw(double amount) throws BankException {
        if (amount <= 0)
            throw new BankException("Withdrawal amount must be positive.");
        if (amount > balance)
            throw new BankException("Insufficient balance.");
        balance -= amount;
    }
}

// Bank Manager class to handle multiple accounts
class BankManager {
    private Map<Integer, BankAccount> accounts = new HashMap<>();

    public void createAccount(String holder, double initialDeposit) {
        try {
            BankAccount account = new SavingsAccount(holder, initialDeposit);
            accounts.put(account.getAccountNumber(), account);
            System.out.println("Account created! Account Number: " + account.getAccountNumber());
        } catch (BankException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showAccount(int accNumber) {
        BankAccount account = accounts.get(accNumber);
        if (account != null) {
            account.showAccountDetails();
        } else {
            System.out.println("Account not found!");
        }
    }

    public void deposit(int accNumber, double amount) {
        BankAccount account = accounts.get(accNumber);
        if (account != null) {
            try {
                account.deposit(amount);
                System.out.println("Deposit successful. New balance: " + account.getBalance());
            } catch (BankException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    public void withdraw(int accNumber, double amount) {
        BankAccount account = accounts.get(accNumber);
        if (account != null) {
            try {
                account.withdraw(amount);
                System.out.println("Withdrawal successful. New balance: " + account.getBalance());
            } catch (BankException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    public void showAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts to show.");
            return;
        }

        for (BankAccount acc : accounts.values()) {
            acc.showAccountDetails();
        }
    }
}

// Main class to interact with the user
public class Application {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankManager manager = new BankManager();
        int choice;

        do {
            System.out.println("\n=== BANK MANAGEMENT SYSTEM ===");
            System.out.println("1. Create New Account");
            System.out.println("2. Show Account Details");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Show All Accounts");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.next(); // clear invalid input
            }

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter account holder name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter initial deposit: ");
                    double deposit = sc.nextDouble();
                    manager.createAccount(name, deposit);
                    break;

                case 2:
                    System.out.print("Enter account number: ");
                    int accNum = sc.nextInt();
                    manager.showAccount(accNum);
                    break;

                case 3:
                    System.out.print("Enter account number: ");
                    accNum = sc.nextInt();
                    System.out.print("Enter amount to deposit: ");
                    double amt = sc.nextDouble();
                    manager.deposit(accNum, amt);
                    break;

                case 4:
                    System.out.print("Enter account number: ");
                    accNum = sc.nextInt();
                    System.out.print("Enter amount to withdraw: ");
                    amt = sc.nextDouble();
                    manager.withdraw(accNum, amt);
                    break;

                case 5:
                    manager.showAllAccounts();
                    break;

                case 6:
                    System.out.println("Thank you for using our Bank Management System!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 6);
        sc.close();
    }
}
