package org.example.atmconsoleapplication;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public boolean login(long accountNumber, String pinCode) {
        Customer user = customerRepo.findById(accountNumber).orElse(null);
        if (user == null) {
            return false;
        }
        return user.getPinCode().equals(pinCode);
    }

    public BigDecimal checkBalance(long accountNumber) {
        Customer user = customerRepo.findById(accountNumber).orElse(null);
        if (user == null) {
            return null;
        }
        return user.getBalance();
    }

    public void deposit(long accountNumber, BigDecimal depositAmount) {
        Customer user = customerRepo.findById(accountNumber).orElse(null);
        if (user == null) return;

        BigDecimal currentBalance = (user.getBalance() == null) ? BigDecimal.ZERO : user.getBalance();
        user.setBalance(currentBalance.add(depositAmount));
        customerRepo.save(user);
    }

    public boolean withdraw(long accountNumber, BigDecimal amountToAdd) {
        Customer user = customerRepo.findById(accountNumber).orElse(null);
        if (user == null) return false;
        if (amountToAdd.compareTo(user.getBalance()) > 0) {
            return false;
        }
        BigDecimal newBalance = user.getBalance().subtract(amountToAdd);
        user.setBalance(newBalance);

        customerRepo.save(user);
        return true;
    }

    public void createNewUser(Long accountNumber, String pinCode) {
        if (customerRepo.existsById(accountNumber)) return;

        Customer newUser = new Customer();
        newUser.setAccountNumber(accountNumber);
        newUser.setPinCode(pinCode);
        newUser.setBalance(BigDecimal.ZERO);
        newUser.setStatus(Customer.AccountStatus.ACTIVE);
        customerRepo.save(newUser);
    }

    public boolean checkPinCode(String pin, long accountNumber) {
        return customerRepo.findById(accountNumber)
                .map(user -> user.getPinCode().equals(pin))
                .orElse(false);
    }

    public void updatePinCode(String newCode, long accountNumber) {
        customerRepo.findById(accountNumber).ifPresent(user -> {
            user.setPinCode(newCode);
            customerRepo.save(user);
        });
    }

    public boolean exists(long accountNumber) {
        return customerRepo.existsById(accountNumber);
    }

    public boolean isAccountExists(long accountNumber) {
        return customerRepo.findById(accountNumber)
                .map(user -> user.getAccountNumber() == accountNumber)
                .orElse(false);
    }

    public Customer getAccount(long accNum) {
        return customerRepo.findById(accNum).orElse(null);
    }

    public void save(Customer customer) {
        customerRepo.save(customer);
    }
}
