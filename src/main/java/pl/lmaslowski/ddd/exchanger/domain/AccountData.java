package pl.lmaslowski.ddd.exchanger.domain;

import java.util.List;

public final class AccountData {
    private final String accountNo;
    private final AccountCurrency currency;
    private final String balance;
    private String balanceUSD;
    private final List<DomainEvent> events;

    public AccountData(String accountNo, AccountCurrency currency, String balance, List<DomainEvent> events) {
        this.accountNo = accountNo;
        this.currency = currency;
        this.balance = balance;
        this.events = events;
    }

    public AccountData(String accountNo, AccountCurrency currency, String balance, String balanceUSD, List<DomainEvent> events) {
        this.accountNo = accountNo;
        this.currency = currency;
        this.balance = balance;
        this.balanceUSD = balanceUSD;
        this.events = events;
    }
    
    public String getAccountNo() {
        return accountNo;
    }
    
    public AccountCurrency getCurrency() {
        return currency;
    }
    
    public String getBalance() {
        return balance;
    }

    public String getBalanceUSD() { return balanceUSD; }

    public List<DomainEvent> getEvents() {
        return events;
    }
}
