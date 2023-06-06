package pl.lmaslowski.ddd.exchanger.interfaces.web;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import pl.lmaslowski.ddd.exchanger.application.AccountQueryService;
import pl.lmaslowski.ddd.exchanger.application.AccountService;

public class MockedTestContext {
    
    @Bean
    public AccountService accountService() {
        return Mockito.mock(AccountService.class);
    }
    
    @Bean
    public AccountQueryService accountQueryService() {
        return Mockito.mock(AccountQueryService.class);
    }
}
