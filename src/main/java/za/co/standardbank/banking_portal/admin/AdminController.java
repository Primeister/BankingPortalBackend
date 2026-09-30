package za.co.standardbank.banking_portal.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.standardbank.banking_portal.account.Account;
import za.co.standardbank.banking_portal.account.AccountRepository;
import za.co.standardbank.banking_portal.customer.Customer;
import za.co.standardbank.banking_portal.customer.CustomerService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final CustomerService customerService;
    private final AccountRepository accountRepository;

    public AdminController(CustomerService customerService,
                           AccountRepository accountRepository) {
        this.customerService = customerService;
        this.accountRepository = accountRepository;
    }

    // View all registered customers
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // View all bank accounts
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}