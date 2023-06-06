package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

public class NewWithdrawDto {
    private String accountNo;
    private double amount;
    
    public NewWithdrawDto(String accountNo, double amount) {
        this.accountNo = accountNo;
        this.amount = amount;
    }
    
    private NewWithdrawDto() {
    }
    
    public String getAccountNo() {
        return accountNo;
    }
    
    public double getAmount() {
        return amount;
    }
}
