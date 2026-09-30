package za.co.standardbank.banking_portal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import za.co.standardbank.banking_portal.customer.Customer;
import za.co.standardbank.banking_portal.customer.CustomerRepository;
import za.co.standardbank.banking_portal.user.Role;
import za.co.standardbank.banking_portal.user.User;
import za.co.standardbank.banking_portal.user.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdminUser(
            UserRepository userRepository,
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminEmail = "admin@standardbank.local";

            // Don't create the admin again if it already exists.
            if (userRepository.findByEmail(adminEmail).isPresent()) {
                return;
            }

            Customer adminCustomer = new Customer(
                    "System",
                    "Administrator",
                    adminEmail
            );

            customerRepository.save(adminCustomer);

            User adminUser = new User(
                    adminEmail,
                    passwordEncoder.encode("admin123"),
                    adminCustomer,
                    Role.ADMIN
            );

            userRepository.save(adminUser);

            System.out.println("Admin user created: " + adminEmail);
        };
    }
}