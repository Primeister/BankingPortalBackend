package za.co.standardbank.banking_portal.recurringpayment;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import za.co.standardbank.banking_portal.account.Account;

@Entity
public class RecurringPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String beneficiary;

    private String frequency;

    private LocalDate nextPaymentDate;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public RecurringPayment() {
    }

    public RecurringPayment(BigDecimal amount, String beneficiary,
                            String frequency,
                            LocalDate nextPaymentDate,
                            boolean active) {
                                
        this.amount = amount;
        this.beneficiary = beneficiary;
        this.frequency = frequency;
        this.nextPaymentDate = nextPaymentDate;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public String getFrequency() {
        return frequency;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public boolean isActive() {
        return active;
    }

    public Account getAccount() {
        return account;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
