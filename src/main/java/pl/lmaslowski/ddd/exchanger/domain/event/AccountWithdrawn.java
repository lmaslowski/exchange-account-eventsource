package pl.lmaslowski.ddd.exchanger.domain.event;

import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.DomainEvent;

import java.util.Date;

public final class AccountWithdrawn implements DomainEvent<AccountWithdrawn.AccountWithdrawnDto> {
    private final AccountNo accountNo;
    private final double amount;
    private final Date occurredOn;
    
    public AccountWithdrawn(AccountNo accountNo, double amount) {
        this.accountNo = accountNo;
        this.amount = amount;
        this.occurredOn = new Date();
    }
    
    public AccountNo accountNo() {
        return accountNo;
    }
    
    public double amount() {
        return amount;
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
    public AccountWithdrawn.AccountWithdrawnDto toDto() {
        return new AccountWithdrawnDto(this.getClass().getSimpleName(), accountNo.accountNo(), amount, occurredOn.toString());
    }

    public static class AccountWithdrawnDto {
        private String eventName;
        private String accountNo;
        private double amount;
        private String occurredOn;

        public AccountWithdrawnDto(String eventName, String accountNo, double amount, String occurredOn) {

            this.eventName = eventName;
            this.accountNo = accountNo;
            this.amount = amount;
            this.occurredOn = occurredOn;
        }

        public String getEventName() {
            return eventName;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public double getAmount() {
            return amount;
        }

        public String getOccurredOn() {
            return occurredOn;
        }
    }
}
