package pl.lmaslowski.ddd.exchanger.domain.event;

import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.DomainEvent;

import java.util.Date;

public class AccountExchangedPLN2USD implements DomainEvent<AccountExchangedPLN2USD.AccountExchangedPLN2USDDto> {

    private AccountNo accountNo;
    private double amount;
    private double amountUSD;
    private double exchangeRate;
    private final Date occurredOn;

    public AccountExchangedPLN2USD(AccountNo accountNo, double amount, double amountUSD, double exchangeRate) {
        this.accountNo = accountNo;
        this.amount = amount;
        this.amountUSD = amountUSD;
        this.exchangeRate = exchangeRate;
        this.occurredOn = new Date();
    }

    public AccountNo accountNo() {
        return accountNo;
    }

    public double amount() {
        return amount;
    }

    public double amountUSD() {
        return amountUSD;
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
        return null;
    }

    @Override
    public AccountExchangedPLN2USD.AccountExchangedPLN2USDDto toDto() {
        return new AccountExchangedPLN2USDDto(this.getClass().getSimpleName(),
                accountNo.accountNo(),
                amount,
                amountUSD,
                exchangeRate,
                occurredOn.toString());
    }

    public static class AccountExchangedPLN2USDDto {
        private String eventName;
        private String accountNo;
        private final double amountPLN;
        private final double amountUSD;
        private final double exchangeRate;
        private final String occurredOn;

        public AccountExchangedPLN2USDDto(String eventName, String accountNo, double amount, double amountUSD, double exchangeRate, String occurredOn) {

            this.eventName = eventName;
            this.accountNo = accountNo;
            this.amountPLN = amount;
            this.amountUSD = amountUSD;
            this.exchangeRate = exchangeRate;
            this.occurredOn = occurredOn;
        }

        public String getEventName() {
            return eventName;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public double getAmountPLN() {
            return amountPLN;
        }

        public double getAmountUSD() {
            return amountUSD;
        }

        public double getExchangeRate() {
            return exchangeRate;
        }

        public String getOccurredOn() {
            return occurredOn;
        }
    }
}
