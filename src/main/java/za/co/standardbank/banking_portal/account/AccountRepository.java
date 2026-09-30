package za.co.standardbank.banking_portal.account;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByCustomerId(Long customerId);

    Optional<Account> findByAccountNumber(String accountNumber);

}
