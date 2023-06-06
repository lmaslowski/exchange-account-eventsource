package pl.lmaslowski.ddd.exchanger.interfaces.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lmaslowski.ddd.exchanger.application.AccountQueryService;
import pl.lmaslowski.ddd.exchanger.application.AccountService;
import pl.lmaslowski.ddd.exchanger.application.command.ExchangePLN2USDCommand;
import pl.lmaslowski.ddd.exchanger.application.command.ExchangeUSD2PLNCommand;
import pl.lmaslowski.ddd.exchanger.application.command.NewAccountCommand;
import pl.lmaslowski.ddd.exchanger.application.command.NewPLNAccountWithDepositCommand;
import pl.lmaslowski.ddd.exchanger.application.command.TransferCommand;
import pl.lmaslowski.ddd.exchanger.domain.AccountCurrency;
import pl.lmaslowski.ddd.exchanger.domain.AccountData;
import pl.lmaslowski.ddd.exchanger.domain.AccountDoesNotExistsException;
import pl.lmaslowski.ddd.exchanger.domain.AccountNo;
import pl.lmaslowski.ddd.exchanger.domain.IncompatibilityAccountCurrencyException;
import pl.lmaslowski.ddd.exchanger.domain.InsufficientBalanceException;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.AccountDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.AccountNoDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.AccountShortDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.ExchangePLN2USDDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.ExchangeUSD2PLNDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.NewAccountDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.NewPLNAccountWithDepositDto;
import pl.lmaslowski.ddd.exchanger.interfaces.web.dto.NewTransferDto;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccountResource {
    
    private AccountService accountApplicationService;
    private AccountQueryService accountApplicationQueryService;
    private ObjectMapper objectMapper;

    public AccountResource(AccountService accountApplicationService, AccountQueryService accountApplicationQueryService, ObjectMapper objectMapper) {
        this.accountApplicationService = accountApplicationService;
        this.accountApplicationQueryService = accountApplicationQueryService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountShortDto create(@Valid @RequestBody NewAccountDto newAccountDto) {
        AccountCurrency accountCurrency = newAccountDto.getAccountCurrency();
        
        AccountData accountData = accountApplicationService.create(new NewAccountCommand(accountCurrency));
        return transformShort(accountData);
    }

    @RequestMapping(value = "/account-pln-with-deposit", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountNoDto createAccountWithDeposit(@Valid @RequestBody NewPLNAccountWithDepositDto newAccountDto) {

        AccountNo accountNo = accountApplicationService.createPLNAccountWithDeposit(new NewPLNAccountWithDepositCommand(
                newAccountDto.getBalance(),
                newAccountDto.getName(),
                newAccountDto.getLastName()
        ));

        return new AccountNoDto(accountNo.accountNo());
    }

    @RequestMapping(value="/account/{accountNo}/exchange-pln-to-usd", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void exchangePLN2USD(@PathVariable("accountNo") String accountNo, @Valid @RequestBody ExchangePLN2USDDto exchangePLN2USDDto){
        accountApplicationService.exchangePLN2USD(new ExchangePLN2USDCommand(new AccountNo(accountNo), exchangePLN2USDDto.getAmount()));
    }

    @RequestMapping(value="/account/{accountNo}/exchange-usd-to-pln", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void exchangeUSD2PLN(@PathVariable("accountNo") String accountNo, @Valid @RequestBody ExchangeUSD2PLNDto exchangeUSD2PLNDto){
        accountApplicationService.exchangeUSD2PLN(new ExchangeUSD2PLNCommand(new AccountNo(accountNo), exchangeUSD2PLNDto.getAmount()));
    }

    @RequestMapping(value = "/account/{accountNo}", method = RequestMethod.GET)
    @ResponseBody
    public AccountDto details(@PathVariable("accountNo") String accountNo) throws AccountDoesNotExistsException {
        return accountDetails(accountNo);
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public AccountShortDto transfer(@Valid @RequestBody NewTransferDto transferDto)
            throws IncompatibilityAccountCurrencyException, AccountDoesNotExistsException, InsufficientBalanceException {
        String sourceAccountNo = transferDto.getSourceAccountNo();
        String destinationAccountNo = transferDto.getDestinationAccountNo();
        double amount = transferDto.getAmount();

        accountApplicationService.transfer(new TransferCommand(sourceAccountNo, destinationAccountNo, amount));

        return accountShortDetails(sourceAccountNo);
    }

    private AccountShortDto transformShort(AccountData accountData) {
        return new AccountShortDto(accountData.getAccountNo(), accountData.getCurrency(), accountData.getBalance());
    }

    private AccountDto transform(AccountData accountData) {
        final List<JsonNode> collect = mapEvents(accountData, objectMapper);
        return new AccountDto(accountData.getAccountNo(), accountData.getCurrency(), accountData.getBalance(), accountData.getBalanceUSD(), collect);
    }

    private List<JsonNode> mapEvents(AccountData accountData, ObjectMapper objectMapper) {
        final List<JsonNode> collect = accountData.getEvents().stream().map(domainEvent -> {
            try {
                return objectMapper.writeValueAsString(domainEvent.toDto());
            } catch (JsonProcessingException e) {
                throw new RuntimeException();
            }
        }).map(s -> {
            try {
                return objectMapper.readTree(s);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }).collect(Collectors.toList());
        return collect;
    }

    private AccountDto accountDetails(String sourceAccountNo) throws AccountDoesNotExistsException {
        AccountData accountData = accountApplicationQueryService.details(sourceAccountNo);
         return transform(accountData);
    }

    private AccountShortDto accountShortDetails(String sourceAccountNo) throws AccountDoesNotExistsException {
        AccountData accountData = accountApplicationQueryService.details(sourceAccountNo);
         return transformShort(accountData);
    }
}
