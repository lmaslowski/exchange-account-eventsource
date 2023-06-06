package pl.lmaslowski.ddd.exchanger.application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.lmaslowski.ddd.exchanger.SpringBootBankApp;
import pl.lmaslowski.ddd.exchanger.application.command.DepositCommand;
import pl.lmaslowski.ddd.exchanger.application.command.ExchangePLN2USDCommand;
import pl.lmaslowski.ddd.exchanger.application.command.ExchangeUSD2PLNCommand;
import pl.lmaslowski.ddd.exchanger.application.command.NewAccountCommand;
import pl.lmaslowski.ddd.exchanger.application.command.NewPLNAccountWithDepositCommand;
import pl.lmaslowski.ddd.exchanger.application.command.TransferCommand;
import pl.lmaslowski.ddd.exchanger.application.command.WithdrawCommand;
import pl.lmaslowski.ddd.exchanger.domain.AccountCurrency;
import pl.lmaslowski.ddd.exchanger.domain.AccountData;
import pl.lmaslowski.ddd.exchanger.domain.AccountDoesNotExistsException;
import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.IncompatibilityAccountCurrencyException;
import pl.lmaslowski.ddd.exchanger.domain.InsufficientBalanceException;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountCreated;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountDeposited;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountExchangedPLN2USD;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountExchangedUSD2PLN;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountTransferred;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountWithdrawn;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.contains;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringBootBankApp.class})
public class AccountServiceTests {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private AccountQueryService accountQueryService;
    
    @Test
    public void shouldCreateAccountWithZeroBalance() {
        //given
        String accountNo = newAccount(AccountCurrency.PLN);
        
        //then
        AccountData details = accountQueryService.details(accountNo);
        assertThat(details.getAccountNo()).isNotNull();
        assertThat(details.getCurrency()).isEqualTo(AccountCurrency.PLN);
        assertThat(details.getBalance()).isEqualTo("PLN 0.000000000000000000000000000000000000000000000000000000000000000");
        org.hamcrest.MatcherAssert.assertThat(details.getEvents(), contains(instanceOf(AccountCreated.class)));
    }

    @Test
    public void shouldCreatePLNAccountWithDeposit(){
        //when
        final AccountNo accountNo = accountService.createPLNAccountWithDeposit(new NewPLNAccountWithDepositCommand(1, "J", "M"));

        //then
        AccountData details = accountQueryService.details(accountNo.accountNo());
        assertThat(details.getAccountNo()).isNotNull();
        assertThat(details.getCurrency()).isEqualTo(AccountCurrency.PLN);
        assertThat(details.getBalance()).isEqualTo("PLN 1.000000000000000000000000000000000000000000000000000000000000000");
        org.hamcrest.MatcherAssert.assertThat(details.getEvents(), contains(
                instanceOf(AccountCreated.class),
                instanceOf(AccountDeposited.class)
        ));
    }

    @Test
    public void shouldExchangePLN2USD() {
        //given
        final AccountNo accountNo = accountService.createPLNAccountWithDeposit(new NewPLNAccountWithDepositCommand(100, "J", "M"));

        //when
        accountService.exchangePLN2USD(new ExchangePLN2USDCommand(accountNo, 10));

        //then
        AccountData details = accountQueryService.details(accountNo.accountNo());
        assertThat(details.getAccountNo()).isNotNull();
        assertThat(details.getCurrency()).isEqualTo(AccountCurrency.PLN);
        org.hamcrest.MatcherAssert.assertThat(details.getEvents(), contains(
                instanceOf(AccountCreated.class),
                instanceOf(AccountDeposited.class),
                instanceOf(AccountExchangedPLN2USD.class)
        ));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void shouldThrowInsufficientBalanceException_whenExchangePLN2USDWithNotEnoughBalance() {
        //given
        final AccountNo accountNo = accountService.createPLNAccountWithDeposit(new NewPLNAccountWithDepositCommand(0, "J", "M"));

        //when
        accountService.exchangeUSD2PLN(new ExchangeUSD2PLNCommand(accountNo, 1));
    }

    @Test
    public void shouldExchangeUSD2PLN() {
        //given
        final AccountNo accountNo = accountService.createPLNAccountWithDeposit(new NewPLNAccountWithDepositCommand(100, "J", "M"));
        accountService.exchangePLN2USD(new ExchangePLN2USDCommand(accountNo, 10));

        //when
            accountService.exchangeUSD2PLN(new ExchangeUSD2PLNCommand(accountNo, 1));

        //then
        AccountData details = accountQueryService.details(accountNo.accountNo());
        assertThat(details.getAccountNo()).isNotNull();
        assertThat(details.getCurrency()).isEqualTo(AccountCurrency.PLN);
        org.hamcrest.MatcherAssert.assertThat(details.getEvents(), contains(
                instanceOf(AccountCreated.class),
                instanceOf(AccountDeposited.class),
                instanceOf(AccountExchangedPLN2USD.class),
                instanceOf(AccountExchangedUSD2PLN.class)
        ));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void shouldThrowInsufficientBalanceException_whenExchangeUSD2PLNWithNotEnoughBalance() {
        //given
        final AccountNo accountNo = accountService.createPLNAccountWithDeposit(new NewPLNAccountWithDepositCommand(100, "J", "M"));

        //when
        accountService.exchangeUSD2PLN(new ExchangeUSD2PLNCommand(accountNo, 1));
    }

    @Test(expected = AccountDoesNotExistsException.class)
    public void shouldThrowExceptionWhenAccountDoesNotExist() {
        accountQueryService.details(UUID.randomUUID().toString());
    }
    
    @Test
    public void shouldDepositMoneyOnAccount() {
        //given
        String accountNo = newAccount(AccountCurrency.PLN);
        
        //when
        depositOnAccount(accountNo, 10);
        
        //then
        AccountData details = accountQueryService.details(accountNo);
        assertThat(details.getAccountNo()).isEqualTo(accountNo);
        assertThat(details.getCurrency()).isEqualTo(AccountCurrency.PLN);
        assertThat(details.getBalance()).isEqualTo("PLN 10.000000000000000000000000000000000000000000000000000000000000000");
        org.hamcrest.MatcherAssert.assertThat(details.getEvents(), contains(
                instanceOf(AccountCreated.class),
                instanceOf(AccountDeposited.class))
        );
    }
    
    @Test
    public void shouldWithdrawMoneyFromAccount() {
        //given
        String accountNo = newAccount(AccountCurrency.PLN);
        depositOnAccount(accountNo, 10);
        
        //when
        withdrawFromAccount(accountNo, 5);
        
        //then
        AccountData details = accountQueryService.details(accountNo);
        assertThat(details.getAccountNo()).isEqualTo(accountNo);
        assertThat(details.getCurrency()).isEqualTo(AccountCurrency.PLN);
        assertThat(details.getBalance()).isEqualTo("PLN 5.000000000000000000000000000000000000000000000000000000000000000");
        org.hamcrest.MatcherAssert.assertThat(details.getEvents(), contains(
                instanceOf(AccountCreated.class),
                instanceOf(AccountDeposited.class),
                instanceOf(AccountWithdrawn.class)
        ));
    }
    
    @Test(expected = InsufficientBalanceException.class)
    public void shouldThrowExceptionWhenWithdrawMoneyFromAccountAndBalanceIsInsufficient() {
        //given
        String accountNo = newAccount(AccountCurrency.PLN);
        depositOnAccount(accountNo, 10);
        
        //when
        withdrawFromAccount(accountNo, 10.00000001);
    }
    
    @Test
    public void shouldTransferMoneyBetweenAmount() {
        //given
        String sourceAccountNo = newAccount(AccountCurrency.PLN);
        String destAccountNo = newAccount(AccountCurrency.PLN);
        
        depositOnAccount(sourceAccountNo, 5);
        double amountToTransfer = 0.75;
        
        //when
        transfer(sourceAccountNo, destAccountNo, amountToTransfer);
        
        //then
        AccountData sourceAccountDetails = accountQueryService.details(sourceAccountNo);
        assertThat(sourceAccountDetails.getAccountNo()).isEqualTo(sourceAccountNo);
        assertThat(sourceAccountDetails.getCurrency()).isEqualTo(AccountCurrency.PLN);
        assertThat(sourceAccountDetails.getBalance()).isEqualTo("PLN 4.250000000000000000000000000000000000000000000000000000000000000");
        org.hamcrest.MatcherAssert.assertThat(sourceAccountDetails.getEvents(), contains(
                instanceOf(AccountCreated.class),
                instanceOf(AccountDeposited.class),
                instanceOf(AccountTransferred.class)
        ));
        
        AccountData destAccountDetails = accountQueryService.details(destAccountNo);
        assertThat(destAccountDetails.getAccountNo()).isEqualTo(destAccountNo);
        assertThat(destAccountDetails.getCurrency()).isEqualTo(AccountCurrency.PLN);
        assertThat(destAccountDetails.getBalance()).isEqualTo("PLN 0.750000000000000000000000000000000000000000000000000000000000000");
        org.hamcrest.MatcherAssert.assertThat(destAccountDetails.getEvents(), contains(
                instanceOf(AccountCreated.class),
                instanceOf(AccountTransferred.class)
        ));
    }
    
    @Test(expected = IncompatibilityAccountCurrencyException.class)
    public void shouldTransferMoneyBetweenAmountAndAccountsHaveIncompatibilityCurrencyType() {
        //given
        String sourceAccountNo = newAccount(AccountCurrency.PLN);
        String destAccountNo = newAccount(AccountCurrency.EUR);
        
        depositOnAccount(sourceAccountNo, 5);
        double amountToTransfer = 0.75;
        
        //when
        transfer(sourceAccountNo, destAccountNo, amountToTransfer);
    }
    
    private void transfer(String sourceAccountNo, String destAccountNo, double amountToTransfer) {
        TransferCommand transferCommand = new TransferCommand(sourceAccountNo, destAccountNo, amountToTransfer);
        accountService.transfer(transferCommand);
    }
    
    private void withdrawFromAccount(String accountNo, double anAmount)
            throws InsufficientBalanceException, AccountDoesNotExistsException {
        WithdrawCommand withdrawCommand = new WithdrawCommand(new AccountNo(accountNo), anAmount);
        accountService.withdraw(withdrawCommand);
    }
    
    private void depositOnAccount(String accountNo, double amount) throws AccountDoesNotExistsException {
        DepositCommand depositCommand = new DepositCommand(new AccountNo(accountNo), amount);
        accountService.deposit(depositCommand);
    }
    
    private String newAccount(AccountCurrency currency) {
        NewAccountCommand newAccountCommand = new NewAccountCommand(currency);
        AccountData accountData = accountService.create(newAccountCommand);
        return accountData.getAccountNo();
    }
}
