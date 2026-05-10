package org.example.atmconsoleapplication;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final CustomerRepo adminRepo;

    public AdminService(CustomerRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public String unBlockUsers(long userAccNo) {
        Optional<Customer> useropt = adminRepo.findByAccountNumber(userAccNo);

        if (useropt.isPresent()) {
            Customer user = useropt.get();

            if (user.getStatus() == Customer.AccountStatus.BLOCKED) {
                user.setStatus(Customer.AccountStatus.ACTIVE);
                adminRepo.save(user);
                return "Account successfully unblocked.";
            } else {
                return "Account is already active.";
            }
        }
        return "No account found with the given account number.";
    }

    public void viewAllCustomers() {
        List<Customer> users = adminRepo.findAll();

        if (users.isEmpty()) {
            System.out.println("No users in the database.");
            return;
        }

        System.out.println("\n========== CUSTOMER RECORDS ==========\n");

        for (Customer user : users) {
            System.out.println("Account Number : " + user.getAccountNumber());
            System.out.println("Account Status : " + user.getStatus() +
                    (user.getStatus() == Customer.AccountStatus.BLOCKED ? " ⚠️" : ""));
            System.out.println("Available Balance : ₹" + user.getBalance());
            System.out.println("--------------------------------------");
        }
    }

    public void viewBlockedUsers() {
        List<Customer> users = adminRepo.findAll();

        if (users.isEmpty()) {
            System.out.println("No users in the database.");
            return;
        }

        boolean foundBlocked = false;

        System.out.println("\n========== BLOCKED CUSTOMER RECORDS ==========\n");

        for (Customer user : users) {
            if (user.getStatus() == Customer.AccountStatus.BLOCKED) {
                foundBlocked = true;

                System.out.println("Account Number : " + user.getAccountNumber());
                System.out.println("Account Status : " + user.getStatus() + " ⚠️");
                System.out.println("Available Balance : ₹" + user.getBalance());
                System.out.println("--------------------------------------");
            }
        }

        if (!foundBlocked) {
            System.out.println("No blocked users found.");
        }
    }

    public String deleteCustomer(long userAccNo, boolean confirm) {
        if (!confirm) {
            return "Deletion cancelled.";
        }

        if (adminRepo.existsById(userAccNo)) {
            adminRepo.deleteById(userAccNo);
            return "Customer deleted successfully.";
        } else {
            return "Customer not found.";
        }
    }

    public String deleteAllCustomers(boolean confirm) {
        if (!confirm) {
            return "Deletion cancelled.";
        }

        if (adminRepo.count() == 0) {
            return "No customers found in the database.";
        }

        adminRepo.deleteAll();
        return "All customers have been deleted.";
    }
}
