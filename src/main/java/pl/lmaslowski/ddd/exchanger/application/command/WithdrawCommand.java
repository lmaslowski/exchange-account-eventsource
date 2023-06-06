package pl.lmaslowski.ddd.exchanger.application.command;

import pl.lmaslowski.ddd.exchanger.domain.AccountNo;

public final class WithdrawCommand {
    private final AccountNo accountNo;
    private final double amount;
    
    public WithdrawCommand(AccountNo anAccountNo, double anAmount) {
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
