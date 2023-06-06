package pl.lmaslowski.ddd.exchanger.domain.event;

import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.DomainEvent;

import java.util.Date;

public final class AccountTransferred implements DomainEvent<AccountTransferred.AccountTransferredDto> {
    private final AccountNo source;
    private final AccountNo dest;
    private final double amount;
    private final Date occurredOn;
    
    public AccountTransferred(AccountNo source, AccountNo dest, double amount) {
        this.source = source;
        this.dest = dest;
        this.amount = amount;
        this.occurredOn = new Date();
    }
    
    public AccountNo source() {
        return source;
    }
    
    public AccountNo dest() {
        return dest;
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
    public AccountTransferred.AccountTransferredDto toDto() {
        return new AccountTransferredDto(this.getClass().getSimpleName(), source.accountNo(), dest.accountNo(), amount, occurredOn.toString());
    }

    public static class AccountTransferredDto {
        private String eventName;
        private String srcAccountNo;
        private String destAccountNo;
        private double amount;
        private String occurredOn;

        public AccountTransferredDto(String eventName, String srcAccountNo, String destAccountNo, double amount, String occurredOn) {
            this.eventName = eventName;
            this.srcAccountNo = srcAccountNo;
            this.destAccountNo = destAccountNo;
            this.amount = amount;
            this.occurredOn = occurredOn;
        }

        public String getEventName() {
            return eventName;
        }

        public String getSrcAccountNo() {
            return srcAccountNo;
        }

        public String getDestAccountNo() {
            return destAccountNo;
        }

        public double getAmount() {
            return amount;
        }

        public String getOccurredOn() {
            return occurredOn;
        }
    }
}
