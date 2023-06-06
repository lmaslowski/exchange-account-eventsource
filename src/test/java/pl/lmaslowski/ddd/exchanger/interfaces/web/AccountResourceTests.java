package pl.lmaslowski.ddd.exchanger.interfaces.web;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.lmaslowski.ddd.exchanger.SpringBootBankApp;
import pl.lmaslowski.ddd.exchanger.application.AccountQueryService;
import pl.lmaslowski.ddd.exchanger.application.AccountService;
import pl.lmaslowski.ddd.exchanger.domain.AccountCurrency;
import pl.lmaslowski.ddd.exchanger.domain.AccountData;
import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.DomainEvent;
import pl.lmaslowski.ddd.exchanger.domain.IncompatibilityAccountCurrencyException;
import pl.lmaslowski.ddd.exchanger.domain.InsufficientBalanceException;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.ExchangePLN2USDDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.ExchangeUSD2PLNDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.NewAccountDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.NewPLNAccountWithDepositDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.NewTransferDto;

import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringBootBankApp.class, MockedTestContext.class})
@WebAppConfiguration
public class AccountResourceTests {
    
    @Autowired
    WebApplicationContext webApplicationContext;
    
    private MockMvc mockMvc;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private AccountQueryService accountQueryService;
    
    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Mockito.reset(accountService, accountQueryService);
    }
    
    @Test
    public void shouldCreateNewAccount() throws Exception {
        String accountNo = "12345";
        AccountCurrency currency = AccountCurrency.PLN;
        String balance = "PLN 0.00";
        AccountData accountData = new AccountData(
                accountNo,
                currency,
                balance,
                new ArrayList<DomainEvent>()
        );
        
        Mockito.when(accountService.create(Mockito.any())).thenReturn(accountData);
        
        NewAccountDto newAccountDto = new NewAccountDto(AccountCurrency.PLN);
        mockMvc.perform(
                post("/account")
                        
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(newAccountDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(accountNo)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(balance)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is(currency.toString())));
    }

    @Test
    public void shouldCreateNewPLNAccountWithDeposit() throws Exception {
        String accountNoString = "12345";
        AccountNo accountNo = new AccountNo(accountNoString);

        Mockito.when(accountService.createPLNAccountWithDeposit(Mockito.any())).thenReturn(accountNo);

        NewPLNAccountWithDepositDto newAccountDto = new NewPLNAccountWithDepositDto(100, "Lukasz", "M");

        mockMvc.perform(
                post("/account-pln-with-deposit")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(newAccountDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNo", Matchers.is(accountNoString)));
    }

    @Test
    public void shouldExchangePLNToUSD() throws Exception {
        String accountNoString = "12345";
        AccountNo accountNo = new AccountNo(accountNoString);
        final ExchangePLN2USDDto exchangePLN2USDDto = new ExchangePLN2USDDto(100);

        doNothing().when(accountService).exchangePLN2USD(Mockito.any());

        mockMvc.perform(
                post("/account/"+ accountNoString + "/exchange-pln-to-usd")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(exchangePLN2USDDto)))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void shouldExchangeUSDToPLN() throws Exception {
        String accountNoString = "12345";
        AccountNo accountNo = new AccountNo(accountNoString);
        final ExchangeUSD2PLNDto exchangeUSD2PLNDto = new ExchangeUSD2PLNDto(100);

        doNothing().when(accountService).exchangeUSD2PLN(Mockito.any());

        mockMvc.perform(
                post("/account/"+ accountNoString + "/exchange-pln-to-usd")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(exchangeUSD2PLNDto)))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void shouldTrowExceptionWhenCreateAndRequestIsNotValid() throws Exception {
        mockMvc.perform(
                post("/account")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"accountCurrency\":\"P\"}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    
    @Test
    public void shouldTransferMoneyBetweenAccount() throws Exception {
        doNothing().when(accountService).transfer(Mockito.anyObject());
        
        String accountNo = "12345";
        String balance = "PLN 10.00";
        AccountCurrency currency = AccountCurrency.PLN;
        AccountData accountData = new AccountData(
                accountNo,
                currency,
                balance,
                new ArrayList<>()
        );
        
        Mockito.when(accountQueryService.details(Mockito.any())).thenReturn(accountData);
        
        NewTransferDto newTransferDto = new NewTransferDto(accountNo, "67890", 10);
        
        mockMvc.perform(post("/transfer")
                                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .content(TestUtil.convertObjectToJsonBytes(newTransferDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(accountNo)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(balance)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is(currency.toString())));
    }
    
    
    @Test
    public void shouldThrowExceptionWhenTransferMoneyBetweenAccountAndInsufficientBalance() throws Exception {
        doThrow(InsufficientBalanceException.class).when(accountService).transfer(Mockito.any());
        
        String accountNo = "12345";
        NewTransferDto newTransferDto = new NewTransferDto(accountNo, "67890", 10);
        
        mockMvc.perform(post("/transfer")
                                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .content(TestUtil.convertObjectToJsonBytes(newTransferDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    
    @Test
    public void shouldThrowExceptionWhenTransferMoneyBetweenAccountAndIncompatibilityAccountCurrency() throws Exception {
        doThrow(IncompatibilityAccountCurrencyException.class).when(accountService).transfer(Mockito.any());
        
        String accountNo = "12345";
        NewTransferDto newTransferDto = new NewTransferDto(accountNo, "67890", 10);
        
        mockMvc.perform(post("/transfer")
                                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .content(TestUtil.convertObjectToJsonBytes(newTransferDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}

