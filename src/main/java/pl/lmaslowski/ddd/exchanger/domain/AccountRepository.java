package pl.lmaslowski.ddd.exchanger.domain;

import java.util.List;

public interface AccountRepository {
    Account withAccountNo(AccountNo accountNo) throws AccountDoesNotExistsException;
    
    void save(Account account);
    
    List<AccountData> viewAll();
}
