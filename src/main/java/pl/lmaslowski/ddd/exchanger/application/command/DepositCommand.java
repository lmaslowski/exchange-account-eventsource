package pl.lmaslowski.ddd.exchanger.application.command;

import pl.lmaslowski.ddd.exchanger.domain.AccountNo;

public final class DepositCommand {
    private final AccountNo accountNo;
    private final double amount;
    
    public DepositCommand(AccountNo anAccountNo, double anAmount) {
        accountNo = anAccountNo;
        amount = anAmount;
    }
    
    public AccountNo getAccountNo() {
        return accountNo;
    }
    
    public double getAmount() {
        return amount;
    }
}
