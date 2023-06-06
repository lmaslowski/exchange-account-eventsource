package pl.lmaslowski.ddd.exchanger.domain;

import java.util.UUID;

public final class AccountNo {
    
    private final String accountNo;
    
    public AccountNo() {
        accountNo = UUID.randomUUID().toString().toUpperCase();
    }
    
    public AccountNo(String anAccountNo) {
        accountNo = anAccountNo;
    }
    
    public String accountNo() {
        return accountNo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountNo)) return false;
        
        AccountNo accountNo1 = (AccountNo) o;
        
        return accountNo != null ? accountNo.equals(accountNo1.accountNo) : accountNo1.accountNo == null;
    }
    
    @Override
    public int hashCode() {
        return accountNo != null ? accountNo.hashCode() : 0;
    }
}
