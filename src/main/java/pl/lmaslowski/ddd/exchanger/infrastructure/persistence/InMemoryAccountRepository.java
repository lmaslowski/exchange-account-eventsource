package pl.lmaslowski.ddd.exchanger.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import pl.lmaslowski.ddd.exchanger.domain.Account;
import pl.lmaslowski.ddd.exchanger.domain.AccountData;
import pl.lmaslowski.ddd.exchanger.domain.AccountDoesNotExistsException;
import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.AccountRepository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryAccountRepository implements AccountRepository {
    
    private java.util.Map<AccountNo, Account> accounts = new ConcurrentHashMap<>();
    
    @Override
    public Account withAccountNo(AccountNo anAccountNo) {
        Account account = accounts.get(anAccountNo);
        throwExceptionIfItsNull(account);
        return account;
    }
    
    private void throwExceptionIfItsNull(Account account) {
        if (Objects.isNull(account)) {
            throw new AccountDoesNotExistsException();
        }
    }
    
    @Override
    public void save(Account anAccount) {
        accounts.put(anAccount.accountNo(), anAccount);
    }
    
    @Override
    public List<AccountData> viewAll() {
        return accounts.entrySet().stream().map(a -> a.getValue().state()).collect(Collectors.toList());
    }
}
