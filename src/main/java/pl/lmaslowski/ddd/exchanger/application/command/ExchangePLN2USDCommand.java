package pl.lmaslowski.ddd.exchanger.application.command;

import pl.lmaslowski.ddd.exchanger.domain.AccountNo;

public final class ExchangePLN2USDCommand {
    private AccountNo accountNo;

    private double amount;

    public ExchangePLN2USDCommand(AccountNo accountNo, double amount) {
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
