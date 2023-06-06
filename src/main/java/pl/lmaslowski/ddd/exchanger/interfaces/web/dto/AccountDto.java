package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import pl.lmaslowski.ddd.exchanger.domain.AccountCurrency;

import java.util.List;

public final class AccountDto {
    
    @JsonProperty("id")
    private String accountNo;
    
    @JsonProperty("currency")
    private AccountCurrency accountCurrency;
    
    private String balance;

    private String usdBalance;

    @JsonProperty("history")
    private List<JsonNode> domainEventList;

    public AccountDto() {}

    public AccountDto(String anAccountNo, AccountCurrency anAccountCurrency, String aBalance, String aUsdBalance, List<JsonNode> domainEventList) {
        accountNo = anAccountNo;
        accountCurrency = anAccountCurrency;
        balance = aBalance;
        usdBalance = aUsdBalance;
        this.domainEventList = domainEventList;
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

    public String getUsdBalance() { return usdBalance; }

    public List<JsonNode> getDomainEventList() {
        return domainEventList;
    }
}
