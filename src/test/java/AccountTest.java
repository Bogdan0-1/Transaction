import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account;
    private final double initialBalance = 10_000;

    @BeforeEach
    public void init(){
        account = new Account("123", initialBalance);
    }

    @Nested
    @DisplayName("Тести на основі Класів Еквівалентності (Equivalence class Partitioning) (ECP)")
    class EquivalenceClassTests {

        @Test
        @DisplayName("ECP: Успішне поповнення (Валідний клас)")
        public void deposit_ValidAmount() {
            account.deposit(5000);
            assertEquals(initialBalance + 5000, account.getBalance());
        }

        @Test
        @DisplayName("ECP: Успішне зняття (Валідний клас)")
        public void withdraw_ValidAmount() {
            assertTrue(account.withdraw(3500));
            assertEquals(initialBalance - 3500, account.getBalance());
        }

        @Test
        @DisplayName("ECP: Зняття з неактивного акаунта (Невалідний клас - стан)")
        public void withdraw_FromInactiveAccount(){
            account.deactivate();
            assertFalse(account.withdraw(1000));
            assertEquals(initialBalance, account.getBalance());
        }

        @Test
        @DisplayName("ECP: Зняття від'ємної суми (Невалідний клас - сума)")
        public void withdraw_NegativeAmount(){
            assertFalse(account.withdraw(-100));
            assertEquals(initialBalance, account.getBalance());
        }

        @Test
        @DisplayName("ECP: Зняття суми, що перевищує баланс (Невалідний клас - сума)")
        public void withdraw_AmountMoreThanBalance(){
            assertFalse(account.withdraw(initialBalance + 10_000));
            assertEquals(initialBalance, account.getBalance());
        }
    }

    @Nested
    @DisplayName("Тести на основі Аналізу Граничних Значень (Boundary Value Analysis) (BVA)")
    class BoundaryValueTests {

        @Test
        @DisplayName("BVA (Deposit): Сума на межі (0)")
        public void deposit_AmountAtBoundary_Zero() {
            account.deposit(0);
            assertEquals(initialBalance, account.getBalance());
        }

        @Test
        @DisplayName("BVA (Deposit): Сума трохи вище межі (0.01)")
        public void deposit_AmountJustAboveBoundary_Zero() {
            account.deposit(0.01);
            assertEquals(initialBalance + 0.01, account.getBalance());
        }

        @Test
        @DisplayName("BVA (Deposit): Сума трохи нижче межі (-0.01)")
        public void deposit_AmountJustBelowBoundary_Zero() {
            account.deposit(-0.01);
            assertEquals(initialBalance, account.getBalance());
        }

        @Test
        @DisplayName("BVA (Withdraw): Сума на межі (0)")
        public void withdraw_AmountAtBoundary_Zero() {
            assertFalse(account.withdraw(0));
            assertEquals(initialBalance, account.getBalance());
        }

        @Test
        @DisplayName("BVA (Withdraw): Сума трохи вище межі (0.01)")
        public void withdraw_AmountJustAboveBoundary_Zero() {
            assertTrue(account.withdraw(0.01));
            assertEquals(initialBalance - 0.01, account.getBalance());
        }

        @Test
        @DisplayName("BVA (Withdraw): Сума на межі (Рівно баланс)")
        public void withdraw_AmountAtBoundary_FullBalance() {
            assertTrue(account.withdraw(initialBalance));
            assertEquals(0, account.getBalance());
        }

        @Test
        @DisplayName("BVA (Withdraw): Сума трохи вище межі (Баланс + 0.01)")
        public void withdraw_AmountJustAboveBoundary_FullBalance() {
            assertFalse(account.withdraw(initialBalance + 0.01));
            assertEquals(initialBalance, account.getBalance());
        }

        @Test
        @DisplayName("BVA (Withdraw): Сума трохи нижче межі (Баланс - 0.01)")
        public void withdraw_AmountJustBelowBoundary_FullBalance() {
            assertTrue(account.withdraw(initialBalance - 0.01));
            assertEquals(0.01, account.getBalance(), 0.001);
        }
    }
}