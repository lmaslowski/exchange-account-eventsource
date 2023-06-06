package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

public class NewDepositDto {
    private String accountNo;
    private double amount;
    
    public NewDepositDto(String accountNo, double amount) {
        this.accountNo = accountNo;
        this.amount = amount;
    }
    
    private NewDepositDto() {
    }
    
    public String getAccountNo() {
        return accountNo;
    }
    
    public double getAmount() {
        return amount;
    }
}
