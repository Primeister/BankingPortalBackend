package za.co.standardbank.banking_portal.recurringpayment;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recurring-payments")
public class RecurringPaymentController {

    private final RecurringPaymentService recurringPaymentService;

    public RecurringPaymentController(
            RecurringPaymentService recurringPaymentService) {

        this.recurringPaymentService = recurringPaymentService;
    }

    @GetMapping
    public List<RecurringPayment> getAllPayments() {
        return recurringPaymentService.getAllPayments();
    }

    @PostMapping
    public RecurringPayment createPayment(
            @RequestBody RecurringPayment payment,
            @RequestParam Long accountId) {

        return recurringPaymentService.createPayment(
                payment,
                accountId
        );
    }

    @DeleteMapping("/{id}")
    public void cancelPayment(@PathVariable Long id) {
        recurringPaymentService.cancelPayment(id);
    }


    }
