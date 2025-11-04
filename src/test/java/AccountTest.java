import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account;

    @BeforeEach
    public void init(){
        account = new Account("123",10_000);
    }

    @Test
    public void deposit() {
        account.deposit(5000);
        assertEquals(15_000,account.getBalance());
    }

    @Test
    public void withdraw() {
        assertTrue(account.withdraw(3500));
        assertEquals(6500,account.getBalance());
    }

    @Test
    public void isInActive(){
        account.deactivate();
        assertFalse(account.withdraw(1000));
    }

    @Test
    public void withdrawAmount(){
        assertFalse(account.withdraw(-100));
    }

    @Test
    public void withdrawMoreThenBalance(){
        assertFalse(account.withdraw(100_000));
    }
}