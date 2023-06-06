package pl.lmaslowski.ddd.exchanger.application.command;

import pl.lmaslowski.ddd.exchanger.domain.AccountNo;

public final class ExchangeUSD2PLNCommand {
    private AccountNo accountNo;

    private double amount;

    public ExchangeUSD2PLNCommand(AccountNo accountNo, double amount) {
        this.accountNo = accountNo;
        this.amount = amount;
    }

    public AccountNo getAccountNo() {
        return accountNo;
    }

    public double getAmount() {
        return amount;
    }
}
