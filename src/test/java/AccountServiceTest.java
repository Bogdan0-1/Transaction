import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    Account source;
    Account destination;
    AccountService accountService;
    Logger logger;

    @BeforeEach
    public void init(){
        source = new Account("123",10_000);
        destination = new Account("321",10_000);
        logger = new Logger();
        accountService = new AccountService(logger);
    }

    @Test
    public void checkSourceActiveAccount(){
        source.deactivate();
        assertFalse(accountService.transferFunds(source,destination,1000));
    }

    @Test
    public void checkDestinationActiveAccount(){
        destination.deactivate();
        assertFalse(accountService.transferFunds(source,destination,1000));
    }

    @Test
    public void checkSumTransfer(){
        assertFalse(accountService.transferFunds(source,destination,-100));
    }

    @Test
    public void checkBalanceForTransfer(){
        assertFalse(accountService.transferFunds(source,destination,20_000));
    }

    @Test
    public void checkFinalCase(){
        assertTrue(accountService.transferFunds(source,destination,2000));
        assertEquals(12_000,destination.getBalance());
        assertEquals(8000,source.getBalance());
    }
    
}