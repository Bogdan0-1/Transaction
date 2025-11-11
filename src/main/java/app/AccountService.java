package app;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {
    Logger logger;

    public AccountService(Logger logger) {
        this.logger = logger;
    }

    public synchronized boolean transferFunds(Account source,Account destination,double amount){
        String transactionId = UUID.randomUUID().toString();
        boolean isSuccessful = false;

        if(source == null || destination == null) return false;

        if(amount <= 0){
            Transaction tx = new Transaction(transactionId,source.getAccountId(), destination.getAccountId(), amount,false);
            logger.logTransaction(tx);
            return false;
        }

        // Перевірка 1: Активність
        if (!source.isActive() || !destination.isActive()) {
            Transaction tx = new Transaction(transactionId, source.getAccountId(), destination.getAccountId(), amount, false);
            logger.logTransaction(tx);
            return false;
        }

        // Перевірка 3: Достатньо коштів та зняття
        if (source.withdraw(amount)) {
            destination.deposit(amount);
            isSuccessful = true;
        }

        Transaction tx = new Transaction(transactionId, source.getAccountId(), destination.getAccountId(), amount, isSuccessful);
        logger.logTransaction(tx); // Інтеграція з app.Logger
        return isSuccessful;
    }

}