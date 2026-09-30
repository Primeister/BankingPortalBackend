package za.co.standardbank.banking_portal.recurringpayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.co.standardbank.banking_portal.account.Account;
import za.co.standardbank.banking_portal.account.AccountRepository;
import za.co.standardbank.banking_portal.security.SecurityService;
import za.co.standardbank.banking_portal.transaction.Transaction;
import za.co.standardbank.banking_portal.transaction.TransactionDirection;
import za.co.standardbank.banking_portal.transaction.TransactionRepository;
import za.co.standardbank.banking_portal.transaction.TransactionType;

@Service
public class RecurringPaymentService {

    private final RecurringPaymentRepository recurringPaymentRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final SecurityService securityService;

    public RecurringPaymentService(
        RecurringPaymentRepository recurringPaymentRepository,
        AccountRepository accountRepository,
        TransactionRepository transactionRepository,
        SecurityService securityService) {

        this.recurringPaymentRepository = recurringPaymentRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.securityService = securityService;
}

    public List<RecurringPayment> getAllPayments() {

        Long customerId =
                securityService.getCurrentCustomer().getId();

        return recurringPaymentRepository
                .findByAccountCustomerId(customerId);
    }

    public RecurringPayment createPayment(
            RecurringPayment payment,
            Long accountId) {

        if (payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(
                    "Amount must be greater than zero");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        verifyAccountOwnership(account);

        payment.setAccount(account);

        return recurringPaymentRepository.save(payment);
    }

    public void cancelPayment(Long id) {

        RecurringPayment payment =
                recurringPaymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recurring payment not found"));

        verifyAccountOwnership(payment.getAccount());

        payment.setActive(false);

        recurringPaymentRepository.save(payment);
    }

    @Scheduled(cron = "0 0 1 * * *")
@Transactional
public void processRecurringPayments() {

    LocalDate today = LocalDate.now();

    List<RecurringPayment> payments =
            recurringPaymentRepository
                    .findByActiveTrueAndNextPaymentDateLessThanEqual(today);

    for (RecurringPayment payment : payments) {

        Account account = payment.getAccount();

        // Skip the payment if there isn't enough money.
        if (account.getBalance().compareTo(payment.getAmount()) < 0) {
            continue;
        }

        // Deduct the payment from the account.
        account.setBalance(
                account.getBalance().subtract(payment.getAmount())
        );

        accountRepository.save(account);

        // Record the payment as a transaction.
       Transaction transaction = new Transaction(
        payment.getAmount(),
        TransactionType.RECURRING_PAYMENT,
        TransactionDirection.DEBIT,
        LocalDateTime.now()
    );

        transaction.setAccount(account);

        transactionRepository.save(transaction);

        // Move the payment to its next scheduled date.
        if (payment.getFrequency().equalsIgnoreCase("MONTHLY")) {
            payment.setNextPaymentDate(
                    payment.getNextPaymentDate().plusMonths(1)
            );
        } else if (payment.getFrequency().equalsIgnoreCase("WEEKLY")) {
            payment.setNextPaymentDate(
                    payment.getNextPaymentDate().plusWeeks(1)
            );
        }

        recurringPaymentRepository.save(payment);
    }
}

    private void verifyAccountOwnership(Account account) {

        Long customerId =
                securityService.getCurrentCustomer().getId();

        if (!account.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Access denied");
        }
    }
}
