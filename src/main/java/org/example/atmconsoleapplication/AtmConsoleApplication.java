package org.example.atmconsoleapplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@SpringBootApplication
public class AtmConsoleApplication implements CommandLineRunner {

    private final CustomerService service;
    private final AdminService adminService;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AtmConsoleApplication(CustomerService service, AdminService adminService) {
        this.service = service;
        this.adminService = adminService;
    }

    public static void main(String[] args) {
        SpringApplication.run(AtmConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n********** Welcome to MADDY BANK **********");
            System.out.println("1. Secure Login");
            System.out.println("2. Open New Account");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit System");
            System.out.print("\nPlease select an option: ");

            int userOption = sc.nextInt();

            if (userOption == 1) {

                System.out.print("Enter Account Number: ");
                long accNum = sc.nextLong();

                if (!service.isAccountExists(accNum)) {
                    System.out.println("AUTHENTICATION FAILED: Account does not exist.");
                } else {

                    Customer customer = service.getAccount(accNum);

                    if (customer.getStatus() == Customer.AccountStatus.BLOCKED) {
                        System.out.println("ACCESS DENIED: This account is currently BLOCKED.");
                    } else {

                        int attemptsLeft = 3;
                        boolean authenticated = false;

                        while (attemptsLeft > 0) {
                            System.out.print("Enter PinCode (" + attemptsLeft + " attempts left): ");
                            String pin = sc.next();

                            if (service.login(accNum, pin)) {
                                authenticated = true;
                                break;
                            } else {
                                attemptsLeft--;
                                if (attemptsLeft > 0) {
                                    System.out.println("WRONG PIN. Please try again.");
                                }
                            }
                        }

                        if (authenticated) {
                            showMenu(sc, accNum);
                        } else {
                            customer.setStatus(Customer.AccountStatus.BLOCKED);
                            service.save(customer);
                            System.out.println("ALERT: Account " + accNum + " is now BLOCKED.");
                        }
                    }
                }

            } else if (userOption == 2) {

                long accNum;

                while (true) {
                    System.out.print("Set AccountNumber (8-11 digits): ");
                    accNum = sc.nextLong();

                    if (service.isAccountExists(accNum)) {
                        System.out.println("ERROR: Account number already taken.");
                    } else {
                        break;
                    }
                }

                while (true) {
                    System.out.print("Set 4-Digit PinCode: ");
                    String pin = sc.next();

                    System.out.print("ReEnter 4-Digit PinCode: ");
                    String reConfirm = sc.next();

                    if (pin.equals(reConfirm)) {
                        service.createNewUser(accNum, pin);

                        System.out.println("\n--- ACCOUNT CREATED SUCCESSFULLY ---");
                        System.out.println("Account Number : " + accNum);
                        System.out.println("Status         : ACTIVE");
                        System.out.println("Date           : " + dtf.format(LocalDateTime.now()));

                        showMenu(sc, accNum);
                        break;
                    } else {
                        System.out.println("PIN doesn't match, try again.");
                    }
                }

            } else if (userOption == 3) {

                sc.nextLine();

                System.out.print("Enter Admin ID: ");
                String adminId = sc.nextLine();

                System.out.print("Enter Admin PIN: ");
                String adminPin = sc.nextLine();

                String fixedId = "admin";
                String fixedPin = "1234";

                if (adminId.equals(fixedId) && adminPin.equals(fixedPin)) {

                    System.out.println("\nAdmin Login Successful\n");

                    while (true) {
                        System.out.println("1. View All Customers");
                        System.out.println("2. View Blocked Customers");
                        System.out.println("3. Unblock Customer");
                        System.out.println("4. Delete Customer");
                        System.out.println("5. Delete all Customers");
                        System.out.println("6. Exit");

                        System.out.print("Enter choice: ");
                        int choice = sc.nextInt();
                        if (choice == 6) {
                            System.out.println("Exiting Admin Menu...");
                            break;
                        } else if (choice == 3) {
                            System.out.print("Enter Account Number to unblock: ");
                            long userAccNo = sc.nextLong();
                            System.out.println(adminService.unBlockUsers(userAccNo));
                            System.out.println();
                        } else if(choice == 1){
                            adminService.viewAllCustomers();
                        } else if(choice == 2){
                            adminService.viewBlockedUsers();
                        }else if(choice == 4){
                            System.out.println("Enter Account Number to delete: ");
                            long userAccNo = sc.nextLong();
                            System.out.print("Are you sure you want to delete this customer? (yes/no): ");
                            String confirmation = sc.next();
                            if(confirmation.equalsIgnoreCase("yes")){
                                System.out.println(adminService.deleteCustomer(userAccNo, true));
                            }else{
                                System.out.println(adminService.deleteCustomer(userAccNo , false));
                            }
                        }else if(choice == 5){
                            System.out.println("\n======================================");
                            System.out.println("⚠️  DANGER ZONE - PERMANENT ACTION ⚠️");
                            System.out.println("======================================");
                            System.out.println("You are about to DELETE ALL customers.");
                            System.out.println("This action CANNOT be undone.");
                            System.out.println("--------------------------------------");

                            System.out.print("Type 'DELETE' to confirm or 'cancel' to abort: ");
                            String confirmation = sc.next();

                            if (confirmation.equalsIgnoreCase("DELETE")) {
                                System.out.println(adminService.deleteAllCustomers(true));
                            } else {
                                System.out.println("Operation cancelled.");
                            }
                        }
                    }

                } else {
                    System.out.println("Invalid Admin ID or PIN");
                }
            } else if (userOption == 4) {
                System.out.println("Thank you for using MADDY BANK. Goodbye!");
                System.exit(0);
            }
        }
    }

    private void showMenu(Scanner sc, long accountNumber) {

        int choice;

        do {
            System.out.println("\n---------- MAIN MENU ----------");
            System.out.println("1. Balance Inquiry");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Change PinCode");
            System.out.println("5. Logout");
            System.out.print("Action: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1 -> System.out.println("Balance: ₹" + service.checkBalance(accountNumber));

                case 2 -> {
                    System.out.print("Amount: ");
                    BigDecimal amt = sc.nextBigDecimal();
                    service.deposit(accountNumber, amt);
                    System.out.println("Deposit Successful.");
                }

                case 3 -> {
                    System.out.print("Amount: ");
                    BigDecimal amt = sc.nextBigDecimal();
                    if (service.withdraw(accountNumber, amt)) {
                        System.out.println("Withdrawal Successful.");
                        System.out.println("Balance Amount after Withdrawal : ₹" + service.checkBalance(accountNumber));
                    } else
                        System.out.println("Insufficient Funds.");
                }

                case 4 -> {
                    System.out.print("Old PIN: ");
                    String old = sc.next();

                    while (true) {
                        if (service.checkPinCode(old, accountNumber)) {
                            System.out.print("New PIN: ");
                            service.updatePinCode(sc.next(), accountNumber);
                            System.out.println("PIN Updated.");
                            break;
                        } else {
                            System.out.println("Incorrect Current PIN.");
                            System.out.print("Old PIN: ");
                            old = sc.next();
                        }
                    }
                }
            }

        } while (choice != 5);
    }
}