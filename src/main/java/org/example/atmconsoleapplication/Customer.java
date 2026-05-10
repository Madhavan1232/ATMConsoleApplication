package org.example.atmconsoleapplication;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import java.math.BigDecimal;

@Entity
@Data
@Getter
public class Customer {
    @Id
    private long accountNumber;
    private String  pinCode;
    private BigDecimal balance;
    public enum AccountStatus{
        ACTIVE,
        BLOCKED
    }
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
}
