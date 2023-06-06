package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

import pl.lmaslowski.ddd.exchanger.domain.AccountCurrency;

import javax.validation.constraints.NotNull;

public class NewAccountDto {
    
    @NotNull
    private AccountCurrency accountCurrency;
    
    public NewAccountDto(@NotNull AccountCurrency accountCurrency) {
        this.accountCurrency = accountCurrency;
    }
    
    public NewAccountDto() {
    }
    
    public AccountCurrency getAccountCurrency() {
        return accountCurrency;
    }
}
