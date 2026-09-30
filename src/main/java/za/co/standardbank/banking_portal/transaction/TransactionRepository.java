package za.co.standardbank.banking_portal.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountCustomerIdOrderByTimestampDesc(Long customerId);
    List<Transaction> findByAccountCustomerId(Long customerId);
}

