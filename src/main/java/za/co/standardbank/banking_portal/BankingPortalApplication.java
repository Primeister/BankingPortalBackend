package za.co.standardbank.banking_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankingPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingPortalApplication.class, args);
	}

}
