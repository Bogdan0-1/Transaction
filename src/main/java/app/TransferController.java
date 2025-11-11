package app;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController // Позначаємо цей клас як API контролер
@RequestMapping("/api") // Всі URL будуть починатися з /api
public class TransferController {

    private final AccountService accountService;

    // Спрощене сховище акаунтів (замість БД)
    // Використовуємо ConcurrentHashMap для безпеки у багатопотоковому середовищі
    private final Map<String, Account> accountRepository = new ConcurrentHashMap<>();

    public TransferController(AccountService accountService) {
        this.accountService = accountService;

        // Створимо два тестові акаунти при старті
        accountRepository.put("acc1", new Account("acc1", 1_000_000.0)); // Рахунок 1 з мільйоном
        accountRepository.put("acc2", new Account("acc2", 1_000_000.0)); // Рахунок 2 з мільйоном
    }

    /**
     * DTO (Data Transfer Object) - клас для тіла запиту
     * Нам потрібно передати ID відправника, отримувача та суму
     */
    public static class TransferRequest {
        private String fromAccountId;
        private String toAccountId;
        private double amount;

        // Потрібні геттери і сеттери для Jackson
        public String getFromAccountId() { return fromAccountId; }
        public void setFromAccountId(String fromAccountId) { this.fromAccountId = fromAccountId; }
        public String getToAccountId() { return toAccountId; }
        public void setToAccountId(String toAccountId) { this.toAccountId = toAccountId; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
    }


    /**
     * API endpoint для тестування
     * Приймає POST запити на /api/transfer
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> handleTransfer(@RequestBody TransferRequest request) {

        Account source = accountRepository.get(request.getFromAccountId());
        Account destination = accountRepository.get(request.getToAccountId());

        if (source == null || destination == null) {
            return ResponseEntity.badRequest().body("app.Account not found");
        }

        Object lock1 = request.getFromAccountId().compareTo(request.getToAccountId()) < 0 ? source : destination;
        Object lock2 = request.getFromAccountId().compareTo(request.getToAccountId()) < 0 ? destination : source;

        synchronized (lock1) {
            synchronized (lock2) {
                // Викликаємо ваш сервіс
                boolean success = accountService.transferFunds(source, destination, request.getAmount());

                if (success) {
                    return ResponseEntity.ok("Transfer successful");
                } else {
                    return ResponseEntity.status(400).body("Transfer failed (e.g., insufficient funds or inactive account)");
                }
            }
        }
    }

    /**
     * Допоміжний API endpoint, щоб перевірити баланс (для JMeter)
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountId) {
        Account account = accountRepository.get(accountId);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}