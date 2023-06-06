package pl.lmaslowski.ddd.exchanger.application;

import org.springframework.stereotype.Service;
import pl.lmaslowski.ddd.exchanger.domain.AccountData;
import pl.lmaslowski.ddd.exchanger.domain.AccountDoesNotExistsException;
import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.AccountRepository;

@Service
public class AccountQueryService {
    
    private AccountRepository accountRepository;
    
    public AccountQueryService(AccountRepository anAccountRepository) {
        accountRepository = anAccountRepository;
    }
    
    public AccountData details(String anAccountNo) throws AccountDoesNotExistsException {
        final AccountData state = accountRepository.withAccountNo(new AccountNo(anAccountNo)).state();
        return state;
    }
}
