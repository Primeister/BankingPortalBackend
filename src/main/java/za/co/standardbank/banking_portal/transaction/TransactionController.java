package za.co.standardbank.banking_portal.transaction;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping("/deposit")
    public Transaction deposit(@RequestParam Long accountId,
            @RequestParam BigDecimal amount) {

        return transactionService.deposit(accountId, amount);
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestParam Long accountId,
            @RequestParam BigDecimal amount) {

        return transactionService.withdraw(accountId, amount);
    }

   @PostMapping("/transfer")
    public void transfer(@RequestParam Long sourceAccountId, @RequestParam String destinationAccountNumber,
        @RequestParam BigDecimal amount) {

    transactionService.transfer(sourceAccountId, destinationAccountNumber,
            amount
    );
}
}
