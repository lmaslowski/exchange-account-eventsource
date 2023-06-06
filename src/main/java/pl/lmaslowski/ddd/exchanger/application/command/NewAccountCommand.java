package pl.lmaslowski.ddd.exchanger.application.command;

import pl.lmaslowski.ddd.exchanger.domain.AccountCurrency;

public final class NewAccountCommand {
    private final AccountCurrency accountCurrency;
    
    public NewAccountCommand(AccountCurrency anAccountCurrency) {
        accountCurrency = anAccountCurrency;
    }
    
    public AccountCurrency getAccountCurrency() {
        return accountCurrency;
    }
}
