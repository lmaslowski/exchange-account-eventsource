package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

public class ExchangeUSD2PLNDto {
    private double amount;

    public ExchangeUSD2PLNDto(double amount) {
        this.amount = amount;
    }

    private ExchangeUSD2PLNDto() {
    }

    public double getAmount() {
        return amount;
    }
}
