package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

public class ExchangePLN2USDDto {

    private double amount;

    public ExchangePLN2USDDto(double amount) {
        this.amount = amount;
    }

    private ExchangePLN2USDDto() {}

    public double getAmount() {
        return amount;
    }
}
