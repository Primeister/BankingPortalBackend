package za.co.standardbank.banking_portal.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import za.co.standardbank.banking_portal.customer.Customer;
import za.co.standardbank.banking_portal.customer.CustomerRepository;
import za.co.standardbank.banking_portal.user.Role;
import za.co.standardbank.banking_portal.user.User;
import za.co.standardbank.banking_portal.user.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       CustomerRepository customerRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(String firstName,
                         String lastName,
                         String email,
                         String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        Customer customer = new Customer(firstName, lastName, email);
        customerRepository.save(customer);

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(
                email,
                hashedPassword,
                customer,
                Role.CUSTOMER
        );

        return userRepository.save(user);
    }

    public User createUserForExistingCustomer(
            Long customerId,
            String password) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (userRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(
                customer.getEmail(),
                hashedPassword,
                customer,
                Role.CUSTOMER
        );

        return userRepository.save(user);
    }

    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtService.generateToken(user);
    }
}