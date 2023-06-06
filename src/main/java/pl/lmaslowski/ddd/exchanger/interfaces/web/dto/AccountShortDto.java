package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.lmaslowski.ddd.exchanger.domain.AccountCurrency;

public final class AccountShortDto {

    @JsonProperty("id")
    private final String accountNo;

    @JsonProperty("currency")
    private final AccountCurrency accountCurrency;

    private final String balance;

    public AccountShortDto(String anAccountNo, AccountCurrency anAccountCurrency, String aBalance) {
        accountNo = anAccountNo;
        accountCurrency = anAccountCurrency;
        balance = aBalance;
    }

    public String getAccountNo() {
        return accountNo;
    }
    
    public AccountCurrency getAccountCurrency() {
        return accountCurrency;
    }
    
    public String getBalance() {
        return balance;
    }
}
