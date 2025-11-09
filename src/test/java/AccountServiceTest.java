import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    Account source;
    Account destination;
    AccountService accountService;
    Logger logger;

    private final double initialBalance = 10_000;

    @BeforeEach
    public void init(){
        source = new Account("123", initialBalance);
        destination = new Account("321", initialBalance);
        logger = new Logger();
        accountService = new AccountService(logger);
    }

    @Nested
    @DisplayName("Тести на основі Класів Еквівалентності (ECP)")
    class EquivalenceClassTests {

        @Test
        @DisplayName("ECP: Успішний переказ (Валідний клас)")
        public void transfer_Successful() {
            assertTrue(accountService.transferFunds(source, destination, 2000));
            assertEquals(initialBalance - 2000, source.getBalance());
            assertEquals(initialBalance + 2000, destination.getBalance());
        }

        @Test
        @DisplayName("ECP: Неактивний відправник (Невалідний клас - стан)")
        public void transfer_InactiveSourceAccount() {
            source.deactivate();
            assertFalse(accountService.transferFunds(source, destination, 1000));
            assertEquals(initialBalance, source.getBalance());
            assertEquals(initialBalance, destination.getBalance());
        }

        @Test
        @DisplayName("ECP: Неактивний отримувач (Невалідний клас - стан)")
        public void transfer_InactiveDestinationAccount() {
            destination.deactivate();
            assertFalse(accountService.transferFunds(source, destination, 1000));
            assertEquals(initialBalance, source.getBalance());
            assertEquals(initialBalance, destination.getBalance());
        }

        @Test
        @DisplayName("ECP: Від'ємна сума (Невалідний клас - сума)")
        public void transfer_NegativeAmount() {
            // Ваш код перевіряє `amount <= 0`
            assertFalse(accountService.transferFunds(source, destination, -100));
            assertEquals(initialBalance, source.getBalance());
            assertEquals(initialBalance, destination.getBalance());
        }

        @Test
        @DisplayName("ECP: Сума перевищує баланс (Невалідний клас - сума)")
        public void transfer_AmountMoreThanBalance() {
            assertFalse(accountService.transferFunds(source, destination, initialBalance + 20_000));
            assertEquals(initialBalance, source.getBalance());
            assertEquals(initialBalance, destination.getBalance());
        }

        @Test
        @DisplayName("ECP: Відправник є null (Невалідний клас - об'єкт)")
        public void transfer_SourceIsNull() {
            assertFalse(accountService.transferFunds(null, destination, 1000));
        }

        @Test
        @DisplayName("ECP: Отримувач є null (Невалідний клас - об'єкт)")
        public void transfer_DestinationIsNull() {
            assertFalse(accountService.transferFunds(source, null, 1000));
        }
    }

    @Nested
    @DisplayName("Тести на основі Аналізу Граничних Значень (BVA)")
    class BoundaryValueTests {

        @Test
        @DisplayName("BVA: Сума на межі (0)")
        public void transfer_AmountAtBoundary_Zero() {
            assertFalse(accountService.transferFunds(source, destination, 0));
            assertEquals(initialBalance, source.getBalance());
            assertEquals(initialBalance, destination.getBalance());
        }

        @Test
        @DisplayName("BVA: Сума трохи вище межі (0.01)")
        public void transfer_AmountJustAboveBoundary_Zero() {
            assertTrue(accountService.transferFunds(source, destination, 0.01));
            assertEquals(initialBalance - 0.01, source.getBalance());
            assertEquals(initialBalance + 0.01, destination.getBalance());
        }

        @Test
        @DisplayName("BVA: Сума на межі (Рівно баланс)")
        public void transfer_AmountAtBoundary_FullBalance() {
            assertTrue(accountService.transferFunds(source, destination, initialBalance));
            assertEquals(0, source.getBalance());
            assertEquals(initialBalance * 2, destination.getBalance());
        }

        @Test
        @DisplayName("BVA: Сума трохи вище межі (Баланс + 0.01)")
        public void transfer_AmountJustAboveBoundary_FullBalance() {
            assertFalse(accountService.transferFunds(source, destination, initialBalance + 0.01));
            assertEquals(initialBalance, source.getBalance());
            assertEquals(initialBalance, destination.getBalance());
        }
    }
}