package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

public class NewPLNAccountWithDepositDto {

    private double balance;

    private String name;

    private String lastName;

    public NewPLNAccountWithDepositDto(double balance, String name, String lastName) {
        this.balance = balance;
        this.name = name;
        this.lastName = lastName;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }
}
