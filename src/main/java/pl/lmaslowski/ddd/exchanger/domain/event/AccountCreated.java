package pl.lmaslowski.ddd.exchanger.domain.event;

import pl.lmaslowski.ddd.exchanger.domain.AccountCurrency;
import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.DomainEvent;

import java.util.Date;

public final class AccountCreated implements DomainEvent<AccountCreated.AccountCreatedDto> {
    private final AccountNo accountNo;
    private final AccountCurrency accountCurrency;
    private final double balance;
    private final Date occurredOn;
    
    public AccountCreated(AccountNo accountNo, AccountCurrency accountCurrency, double balance, Date occuredOn) {
        this.accountNo = accountNo;
        this.accountCurrency = accountCurrency;
        this.balance = balance;
        this.occurredOn = occuredOn;
    }
    
    public AccountNo accountNo() {
        return accountNo;
    }
    
    public AccountCurrency accountCurrency() {
        return accountCurrency;
    }
    
    public double balance() {
        return balance;
    }
    
    @Override
    public int eventVersion() {
        return 0;
    }
    
    @Override
    public Date occurredOn() {
        return occurredOn;
    }

    public AccountCreatedDto toDto() {
        return new AccountCreatedDto(this.getClass().getSimpleName(), accountNo.accountNo(), accountCurrency.toString(), balance, occurredOn.toString());
    }

    public static class AccountCreatedDto {
        private String eventName;
        private String accountNo;
        private String accountCurrency;
        private double balance;
        private String occurredOn;

        public AccountCreatedDto() {
        }

        public AccountCreatedDto(String accountNo) {
            this.accountNo = accountNo;
        }

        public AccountCreatedDto(String eventName, String accountNo, String accountCurrency, double balance, String occurredOn) {
            this.eventName = eventName;
            this.accountNo = accountNo;
            this.accountCurrency = accountCurrency;
            this.balance = balance;
            this.occurredOn = occurredOn;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public String getAccountCurrency() {
            return accountCurrency;
        }

        public double getBalance() {
            return balance;
        }

        public String getOccurredOn() {
            return occurredOn;
        }

        public String getEventName() { return eventName; }
    }
}
