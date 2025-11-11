package app;

import org.springframework.stereotype.Service; // Імпортуємо

@Service
public class Logger{
    public void logTransaction(Transaction transaction){
        String status = transaction.isSuccessful() ? "SUCCESS" : "FAILURE";
        System.out.println("LOG: app.Transaction " + status + " for amount " + transaction.getAmount());
    }
}