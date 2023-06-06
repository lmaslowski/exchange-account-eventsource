package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

public class AccountNoDto {

    private String accountNo;

    public AccountNoDto(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNo() {
        return accountNo;
    }
}
