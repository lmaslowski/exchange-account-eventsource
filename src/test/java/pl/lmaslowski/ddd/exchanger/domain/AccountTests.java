package pl.lmaslowski.ddd.exchanger.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AccountTests {

    @Test
    public void givenAccountCurrency_whenCreate_thenAccountWithBalanceZeroShouldBeCreated() {
        Account account = new Account(AccountCurrency.PLN);

        AccountData accountData = account.state();

        assertNotNull(accountData.getAccountNo());
        assertEquals(AccountCurrency.PLN, accountData.getCurrency());
        assertEquals("PLN 0.000000000000000000000000000000000000000000000000000000000000000",
                accountData.getBalance());
        assertEquals(accountData.getEvents().size(), 1);
    }

    @Test
    public void givenAccountWithEnoughBalance_whenDeposit_thenBalanceShouldBeIncreased() {
        Account account = new Account(AccountCurrency.PLN);
        account.deposit(10);

        AccountData accountData = account.state();

        assertNotNull(accountData.getAccountNo());
        assertEquals(AccountCurrency.PLN, accountData.getCurrency());
        assertEquals("PLN 10.000000000000000000000000000000000000000000000000000000000000000",
                accountData.getBalance());
        assertEquals(accountData.getEvents().size(), 2);
    }

    @Test
    public void givenAccountWithEnoughBalance_whenExchangePLN2USD_thenAccountBalanceShouldBeDecreased() {
        Account account = new Account();
        account.deposit(100);
        account.exchangePLNToUSD(10, 1);

        AccountData accountData = account.state();

        assertNotNull(accountData.getAccountNo());
        assertEquals(AccountCurrency.PLN, accountData.getCurrency());
        assertEquals("PLN 90.000000000000000000000000000000000000000000000000000000000000000",
                accountData.getBalance());
        assertEquals("USD 10.000000000000000000000000000000000000000000000000000000000000000",
                accountData.getBalanceUSD());
        assertEquals(accountData.getEvents().size(), 3);

    }

    @Test
    public void givenAccountWithEnoughBalance_whenExchangeUSD2PLN_thenAccountBalanceShouldBeDecreased() {
        Account account = new Account();
        account.deposit(100);
        account.exchangePLNToUSD(10, 1);
        account.exchangeUSDToPLN(10, 1);

        AccountData accountData = account.state();

        assertNotNull(accountData.getAccountNo());
        assertEquals(AccountCurrency.PLN, accountData.getCurrency());
        assertEquals("PLN 100.000000000000000000000000000000000000000000000000000000000000000",
                accountData.getBalance());
        assertEquals("USD 0.000000000000000000000000000000000000000000000000000000000000000",
                accountData.getBalanceUSD());
        assertEquals(accountData.getEvents().size(), 4);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenAccountWithNotEnoughBalance_whenExchangeUSD2PLN_thenShouldThrowException() {
        Account account = new Account();
        account.exchangeUSDToPLN(5, 1.0);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenAccountWithNotEnoughBalance_whenExchangePLN2USD_thenShouldThrowException()
            throws InsufficientBalanceException {
        Account account = new Account();
        account.exchangePLNToUSD(5, 1.0);
    }

    @Test
    public void givenAccountWithEnoughBalance_whenWithdraw_thenAccountBalanceShouldBeDecreased() {
        Account account = new Account(AccountCurrency.PLN);
        account.deposit(10);
        account.withdraw(5);

        AccountData accountData = account.state();

        assertNotNull(accountData.getAccountNo());
        assertEquals(AccountCurrency.PLN, accountData.getCurrency());
        assertEquals("PLN 5.000000000000000000000000000000000000000000000000000000000000000",
                accountData.getBalance());
        assertEquals(accountData.getEvents().size(), 3);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenAccountWithNotEnoughBalance_whenWithdraw_thenShouldThrowException()
            throws InsufficientBalanceException {
        Account account = new Account(AccountCurrency.PLN);
        account.withdraw(5);
    }

    @Test
    public void givenValidAccounts_whenTransfer_thenMoneyShouldBeTransferred() {
        Account accountSource = new Account(AccountCurrency.PLN);
        accountSource.deposit(10);

        Account accountDestination = new Account(AccountCurrency.PLN);
        accountDestination.deposit(10);

        accountSource.transfer(accountDestination, 5.25);

        AccountData view1 = accountSource.state();
        AccountData view = accountDestination.state();

        assertTrue(accountSource.isBalanceEqualTo(4.75));
        assertTrue(accountDestination.isBalanceEqualTo(15.25));
    }

    @Test(expected = IncompatibilityAccountCurrencyException.class)
    public void givenAccountsTransfer_whenIncompatibilityAccountCurrency_thenThrowException() {
        Account accountSource = new Account(AccountCurrency.PLN);
        accountSource.deposit(10);

        Account accountDestination = new Account(AccountCurrency.EUR);
        accountDestination.deposit(10);

        accountSource.transfer(accountDestination, 10);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenAccountsTransfer_whenInsufficientBalanceException_thenThrowException() {
        Account accountSource = new Account(AccountCurrency.PLN);

        Account accountDestination = new Account(AccountCurrency.PLN);
        accountDestination.deposit(10);

        accountSource.transfer(accountDestination, 10);
    }
}