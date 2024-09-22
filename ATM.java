import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class BankAccount {
    private double balance;
    private ArrayList<String> transactionHistory;
    private String pin;
    private double dailyWithdrawalLimit = 500.0;
    private double amountWithdrawnToday = 0.0;

    
    public BankAccount(double initialBalance, String pin) {
        this.balance = initialBalance;
        this.pin = pin;
        this.transactionHistory = new ArrayList<>();
    }

    // Get current balance
    public double getBalance() {
        return balance;
    }

    // Withdraw money with daily limit
    public boolean withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds.");
            return false;
        } else if (amountWithdrawnToday + amount > dailyWithdrawalLimit) {
            System.out.println("Daily withdrawal limit exceeded.");
            return false;
        } else {
            balance -= amount;
            amountWithdrawnToday += amount;
            transactionHistory.add("Withdrawn: ₹" + amount);
            System.out.println("Withdrawal successful.");
            return true;
        }
    }

    // Deposit money
    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited: ₹" + amount);
    }

    // Display transaction history
    public void showTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            System.out.println("Transaction History:");
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }

    // Change PIN and store it in a file
    public void changePIN(String newPIN) {
        this.pin = newPIN;
        savePinToFile(newPIN);
        System.out.println("PIN successfully changed.");
    }

    // Save the PIN to a file
    private void savePinToFile(String pin) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pin.txt"))) {
            writer.write(pin);
        } catch (IOException e) {
            System.out.println("Error saving PIN: " + e.getMessage());
        }
    }

    // Get PIN for authentication
    public String getPIN() {
        return pin;
    }
}

public class ATM {
    
    public static String readPinFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("pin.txt"))) {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("PIN file not found. Using default PIN '1234'.");
            return "1234";  // default PIN
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Load the PIN from file or use default
        String storedPin = readPinFromFile();
        BankAccount account = new BankAccount(1000, storedPin);  // initial balance and PIN

        // Simple authentication
        System.out.print("Enter your PIN: ");
        String pin = scanner.nextLine();

        if (!pin.equals(account.getPIN())) {
            System.out.println("Incorrect PIN. Exiting...");
            return;
        }

        boolean exit = false;

        // ATM Menu
        while (!exit) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Deposit Money");
            System.out.println("4. View Transaction History");
            System.out.println("5. Change PIN");
            System.out.println("6. Exit");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Current Balance: ₹" + account.getBalance());
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                    System.out.println("Current Balance: ₹" + account.getBalance());
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    System.out.println("Deposit successful. Current Balance: ₹" + account.getBalance());
                    break;
                case 4:
                    account.showTransactionHistory();
                    break;
                case 5:
                    System.out.print("Enter new PIN: ");
                    scanner.nextLine();  // clear buffer
                    String newPIN = scanner.nextLine();
                    account.changePIN(newPIN);
                    break;
                case 6:
                    System.out.println("Are you sure you want to exit? (yes/no): ");
                    scanner.nextLine();  // clear buffer
                    String confirmation = scanner.nextLine();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        exit = true;
                        System.out.println("Thank you for using the ATM. Goodbye!");
                    }
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
