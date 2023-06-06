package pl.lmaslowski.ddd.exchanger.domain.event;

import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.DomainEvent;

import java.util.Date;

public class AccountExchangedUSD2PLN implements DomainEvent<AccountExchangedUSD2PLN.AccountExchangedUSD2PLNDto> {

    private AccountNo accountNo;
    private double amountUSD;
    private double amountPLN;
    private double exchangeRate;
    private final Date occurredOn;

    public AccountExchangedUSD2PLN(AccountNo accountNo, double amountUSD, double amountPLN, double exchangeRate) {
        this.accountNo = accountNo;
        this.amountUSD = amountUSD;
        this.amountPLN = amountPLN;
        this.exchangeRate = exchangeRate;
        this.occurredOn = new Date();
    }

    public AccountNo accountNo() {
        return accountNo;
    }

    public double amountUSD() {
        return amountUSD;
    }

    public double amountPLN() {
        return amountPLN;
    }

    public double exchangeRate() {
        return exchangeRate;
    }

    public Date getOccurredOn() {
        return occurredOn;
    }

    @Override
    public int eventVersion() {
        return 0;
    }

    @Override
    public Date occurredOn() {
        return occurredOn;
    }

    @Override
    public AccountExchangedUSD2PLN.AccountExchangedUSD2PLNDto toDto() {
        return new AccountExchangedUSD2PLNDto(this.getClass().getSimpleName(), accountNo.accountNo(), amountUSD, amountPLN, exchangeRate, occurredOn.toString());
    }

    public static class AccountExchangedUSD2PLNDto {
        private String eventName;
        private String accountNo;
        private double amountUSD;
        private double amountPLN;
        private double exchangeRate;
        private String occurredOn;

        public AccountExchangedUSD2PLNDto() {
        }

        public AccountExchangedUSD2PLNDto(String eventName, String accountNo, double amountUSD, double amountPLN, double exchangeRate, String occurredOn) {
            this.eventName = eventName;
            this.accountNo = accountNo;
            this.amountUSD = amountUSD;
            this.amountPLN = amountPLN;
            this.exchangeRate = exchangeRate;
            this.occurredOn = occurredOn;
        }

        public String getEventName() {
            return eventName;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public double getAmountUSD() {
            return amountUSD;
        }

        public double getAmountPLN() {
            return amountPLN;
        }

        public double getExchangeRate() {
            return exchangeRate;
        }

        public String getOccurredOn() {
            return occurredOn;
        }
    }
}
