package za.co.standardbank.banking_portal.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.co.standardbank.banking_portal.account.Account;
import za.co.standardbank.banking_portal.account.AccountRepository;
import za.co.standardbank.banking_portal.security.SecurityService;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final SecurityService securityService;

    public TransactionService(TransactionRepository transactionRepository,
        AccountRepository accountRepository,
        SecurityService securityService) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.securityService = securityService;
        }

    public List<Transaction> getAllTransactions() {

        Long customerId = securityService.getCurrentCustomer().getId();

        return transactionRepository.findByAccountCustomerIdOrderByTimestampDesc(customerId);
        }

    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Amount must be greater than zero");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        verifyAccountOwnership(account);

        account.setBalance(account.getBalance().add(amount));

        accountRepository.save(account);

        Transaction transaction = new Transaction(
        amount,
        TransactionType.DEPOSIT,
        TransactionDirection.CREDIT,
        LocalDateTime.now()
        );

        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Amount must be greater than zero");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        verifyAccountOwnership(account);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));

        accountRepository.save(account);

        Transaction transaction = new Transaction(
        amount,
        TransactionType.WITHDRAWAL,
        TransactionDirection.DEBIT,
        LocalDateTime.now()
        );

        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }

    @Transactional
public void transfer(
        Long fromAccountId,
        String toAccountNumber,
        BigDecimal amount) {

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new RuntimeException("Amount must be greater than zero");
    }

    Account fromAccount = accountRepository.findById(fromAccountId)
            .orElseThrow(() -> new RuntimeException("Source account not found"));

    Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
            .orElseThrow(() -> new RuntimeException("Destination account not found"));

    if (fromAccount.getAccountNumber().equals(toAccountNumber)) {
        throw new RuntimeException("Cannot transfer to the same account");
    }

    // Make sure the source account belongs to the logged-in customer.
    verifyAccountOwnership(fromAccount);

    if (fromAccount.getBalance().compareTo(amount) < 0) {
        throw new RuntimeException("Insufficient funds");
    }

    fromAccount.setBalance(
            fromAccount.getBalance().subtract(amount)
    );

    toAccount.setBalance(
            toAccount.getBalance().add(amount)
    );

    accountRepository.save(fromAccount);
    accountRepository.save(toAccount);

    LocalDateTime now = LocalDateTime.now();

    // Transaction on the sending account.
    Transaction outgoingTransaction = new Transaction(
            amount,
            TransactionType.TRANSFER,
            TransactionDirection.DEBIT,
            now
    );

    outgoingTransaction.setAccount(fromAccount);
    outgoingTransaction.setRelatedAccount(toAccount);

    transactionRepository.save(outgoingTransaction);

    // Transaction on the receiving account.
    Transaction incomingTransaction = new Transaction(
            amount,
            TransactionType.TRANSFER,
            TransactionDirection.CREDIT,
            now
    );

    incomingTransaction.setAccount(toAccount);
    incomingTransaction.setRelatedAccount(fromAccount);

    transactionRepository.save(incomingTransaction);
}

    private void verifyAccountOwnership(Account account) {

        Long customerId =
                securityService.getCurrentCustomer().getId();

        if (!account.getCustomer().getId().equals(customerId)) {
                throw new RuntimeException("Access denied");
        }
        }
}
