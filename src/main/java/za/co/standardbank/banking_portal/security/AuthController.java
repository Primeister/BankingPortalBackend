package za.co.standardbank.banking_portal.security;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        authService.register(firstName, lastName, email, password);
        return "User registered successfully";
    }

    @PostMapping("/create-for-customer")
    public String createUserForExistingCustomer(
            @RequestParam Long customerId,
            @RequestParam String password) {

        authService.createUserForExistingCustomer(customerId, password);
        return "User created successfully";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
            @RequestParam String password) {

        return authService.login(email, password);
    }
}