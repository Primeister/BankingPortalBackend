package za.co.standardbank.banking_portal.account;

import java.util.List;

import org.springframework.stereotype.Service;

import za.co.standardbank.banking_portal.customer.Customer;
import za.co.standardbank.banking_portal.customer.CustomerRepository;
import za.co.standardbank.banking_portal.security.SecurityService;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final SecurityService securityService;

    public AccountService(AccountRepository accountRepository,
                          CustomerRepository customerRepository,
                          SecurityService securityService) {

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.securityService = securityService;
    }

    public List<Account> getAllAccounts() {

        Long customerId = securityService.getCurrentCustomer().getId();

        return accountRepository.findByCustomerId(customerId);
    }

    public Account getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        Long customerId = securityService.getCurrentCustomer().getId();

        if (!account.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Access denied");
        }

        return account;
    }

    public Account createAccount(Account account) {

        // Get the customer from the authenticated user.
        Customer customer = securityService.getCurrentCustomer();

        account.setCustomer(customer);

        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        Long customerId = securityService.getCurrentCustomer().getId();

        if (!account.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Access denied");
        }

        accountRepository.delete(account);
    }
}