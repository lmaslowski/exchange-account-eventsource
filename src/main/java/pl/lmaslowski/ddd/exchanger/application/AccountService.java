package pl.lmaslowski.ddd.exchanger.application;

import org.springframework.stereotype.Service;
import pl.lmaslowski.ddd.exchanger.application.command.DepositCommand;
import pl.lmaslowski.ddd.exchanger.application.command.ExchangePLN2USDCommand;
import pl.lmaslowski.ddd.exchanger.application.command.ExchangeUSD2PLNCommand;
import pl.lmaslowski.ddd.exchanger.application.command.NewAccountCommand;
import pl.lmaslowski.ddd.exchanger.application.command.NewPLNAccountWithDepositCommand;
import pl.lmaslowski.ddd.exchanger.application.command.TransferCommand;
import pl.lmaslowski.ddd.exchanger.application.command.WithdrawCommand;
import pl.lmaslowski.ddd.exchanger.domain.Account;
import pl.lmaslowski.ddd.exchanger.domain.AccountCurrency;
import pl.lmaslowski.ddd.exchanger.domain.AccountData;
import pl.lmaslowski.ddd.exchanger.domain.AccountDoesNotExistsException;
import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.AccountRepository;
import pl.lmaslowski.ddd.exchanger.domain.IncompatibilityAccountCurrencyException;
import pl.lmaslowski.ddd.exchanger.domain.InsufficientBalanceException;
import pl.lmaslowski.ddd.exchanger.domain.port.inbound.ExchangeRate;

@Service
public class AccountService {
    
    private AccountRepository accountRepository;
    private ExchangeRate exchangeRate;

    public AccountService(AccountRepository accountRepository, ExchangeRate exchangeRate) {
        this.accountRepository = accountRepository;
        this.exchangeRate = exchangeRate;
    }

    public AccountData create(NewAccountCommand newAccountCommand) {
        AccountCurrency accountCurrency = newAccountCommand.getAccountCurrency();
        Account account = new Account(accountCurrency);
        
        accountRepository.save(account);
        
        return account.state();
    }

    public AccountNo createPLNAccountWithDeposit(NewPLNAccountWithDepositCommand command) {
        final Account account = new Account(command.getName(), command.getLastName());
        account.deposit(command.getBalance());

        accountRepository.save(account);

        return account.accountNo();
    }
    
    public void withdraw(WithdrawCommand withdrawCommand)
            throws InsufficientBalanceException, AccountDoesNotExistsException {
        AccountNo accountNo = withdrawCommand.getAccountNo();
        double amount = withdrawCommand.getAmount();
        
        Account account = account(accountNo);
        account.withdraw(amount);
    }
    
    public void deposit(DepositCommand depositCommand) throws AccountDoesNotExistsException {
        AccountNo accountNo = depositCommand.getAccountNo();
        double amount = depositCommand.getAmount();
        
        Account account = account(accountNo);
        account.deposit(amount);
    }
    
    public void transfer(TransferCommand transferCommand) throws InsufficientBalanceException,
                                                                         IncompatibilityAccountCurrencyException,
                                                                         AccountDoesNotExistsException {
        Account srcAccount = account(new AccountNo(transferCommand.getSourceAccountNo()));
        Account destAccount = account(new AccountNo(transferCommand.getDestinationAccountNo()));
        
        srcAccount.transfer(destAccount, transferCommand.getAmount());
    }

    public void exchangePLN2USD(ExchangePLN2USDCommand command) {
        final Account account = account(command.getAccountNo());
        account.exchangePLNToUSD(command.getAmount(), exchangeRate.getUSDPLN());
    }

    public void exchangeUSD2PLN(ExchangeUSD2PLNCommand command) {
        final Account account = account(command.getAccountNo());
        account.exchangeUSDToPLN(command.getAmount(), exchangeRate.getUSDPLN());
    }

    private Account account(AccountNo accountNo) throws AccountDoesNotExistsException {
        return accountRepository.withAccountNo(accountNo);
    }
}
