package za.co.standardbank.banking_portal.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import za.co.standardbank.banking_portal.customer.Customer;
import za.co.standardbank.banking_portal.user.User;
import za.co.standardbank.banking_portal.user.UserRepository;

@Service
public class SecurityService {

    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Customer getCurrentCustomer() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

        return user.getCustomer();
    }
}
