package pl.lmaslowski.ddd.exchanger.domain;

import pl.lmaslowski.ddd.exchanger.domain.event.AccountCreated;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountDeposited;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountExchangedPLN2USD;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountExchangedUSD2PLN;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountTransferred;
import pl.lmaslowski.ddd.exchanger.domain.event.AccountWithdrawn;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Date;

public final class Account extends EventSourcedRootEntity {

    private transient AccountNo accountNo;
    private String name;
    private String lastName;
    private transient volatile MonetaryAmount balance;
    private transient volatile MonetaryAmount balanceUSD;
    private transient AccountCurrency currency;

    public Account() {
        this(new AccountNo(), AccountCurrency.PLN, 0);
    }

    public Account(String name, String lastName) {
        this(new AccountNo(), AccountCurrency.PLN, 0);
        this.name = name;
        this.lastName = lastName;
    }

    public Account(AccountCurrency anAccountTypeCurrency) {
        this(new AccountNo(), anAccountTypeCurrency, 0);
    }

    private Account(AccountNo anAccountNo, AccountCurrency anAccountTypeCurrency, double aBalance) {
        this.apply(new AccountCreated(anAccountNo, anAccountTypeCurrency, aBalance, new Date()));
    }

    public synchronized void withdraw(double amount) {
        throwExceptionIfInsufficientBalance(amount);
        this.apply(new AccountWithdrawn(accountNo(), amount));
    }

    public synchronized void deposit(double amount) {
        this.apply(new AccountDeposited(accountNo(), amount));
    }

    public synchronized void transfer(Account destination, double amount) {
        throwExceptionIfIncompatibilityAccountCurrency(destination);
        throwExceptionIfInsufficientBalance(amount);

        AccountTransferred aDomainEvent = new AccountTransferred(accountNo(), destination.accountNo, amount);

        this.apply(aDomainEvent);
        destination.apply(aDomainEvent);
    }

    public synchronized void exchangePLNToUSD(double amountPLN, double exchangeRate) {
        throwExceptionIfInsufficientBalance(amountPLN);
        throwExceptionIfExchangeRateIsInvalid(exchangeRate);

        final double moneyUSD = moneyPLN(amountPLN).divide(exchangeRate).getNumber().doubleValue();

        this.apply(new AccountExchangedPLN2USD(accountNo(), amountPLN, moneyUSD, exchangeRate));
    }

    public synchronized void exchangeUSDToPLN(double amountUSD, double exchangeRate) {
        throwExceptionIfInsufficientUSDBalance(amountUSD);
        throwExceptionIfExchangeRateIsInvalid(exchangeRate);

        final double moneyPLN = moneyUSD(amountUSD).multiply(exchangeRate).getNumber().doubleValue();

        this.apply(new AccountExchangedUSD2PLN(accountNo(), amountUSD, moneyPLN, exchangeRate));
    }

    private void throwExceptionIfExchangeRateIsInvalid(double exchangeRate) {
        if (exchangeRate <= 0D) {
            throw new IllegalArgumentException();
        }
    }

    boolean isBalanceEqualTo(double anAmount) {
        return balance().isEqualTo(money(anAmount));
    }

    public AccountData state() {
        return new AccountData(accountNoValue(), currency(), balanceString(), balanceUSDString(), mutatingEvents());
    }

    public AccountNo accountNo() {
        return accountNo;
    }

    protected void when(AccountCreated accountCreated) {
        setAccountNo(accountCreated.accountNo());
        setCurrency(accountCreated.accountCurrency());
        setBalance(accountCreated.balance());
        setBalanceUSD(0);
    }

    protected void when(AccountWithdrawn accountWithdrawn) {
        assertArgumentEquals(accountNo(), accountWithdrawn.accountNo(), "must be equal");
        setBalance(balance.subtract(money(accountWithdrawn.amount())));
    }

    protected void when(AccountDeposited accountDeposited) {
        assertArgumentEquals(accountNo(), accountDeposited.accountNo(), "must be equal");
        setBalance(balance.add(money(accountDeposited.amount())));
    }

    protected void when(AccountTransferred accountTransferred) {
        if (accountNo().equals(accountTransferred.source())) {
            assertArgumentEquals(accountNo(), accountTransferred.source(), "must be equal");
            setBalance(balance.subtract(money(accountTransferred.amount())));
        } else if (accountNo().equals(accountTransferred.dest())) {
            assertArgumentEquals(accountNo(), accountTransferred.dest(), "must be equal");
            setBalance(balance.add(money(accountTransferred.amount())));
        }
    }

    protected void when(AccountExchangedPLN2USD accountExchangedPLN2USD) {
        setBalance(balance.subtract(moneyPLN(accountExchangedPLN2USD.amount())));
        setBalanceUSD(balanceUSD.add(moneyUSD(accountExchangedPLN2USD.amountUSD())));
    }

    protected void when(AccountExchangedUSD2PLN accountExchangedFromPLNToUSD) {
        setBalance(balance.add(moneyPLN(accountExchangedFromPLNToUSD.amountPLN())));
        setBalanceUSD(balanceUSD.subtract(moneyUSD(accountExchangedFromPLNToUSD.amountUSD())));
    }

    private void throwExceptionIfInsufficientBalance(double amount) {
        if (balance().isLessThan(money(amount))) {
            throw new InsufficientBalanceException();
        }
    }

    private void throwExceptionIfInsufficientUSDBalance(double amount) {
        if (balanceUSD().isLessThan(moneyUSD(amount))) {
            throw new InsufficientBalanceException();
        }
    }

    private void throwExceptionIfIncompatibilityAccountCurrency(Account destinationAccount)
            throws IncompatibilityAccountCurrencyException {
        if (currency() != destinationAccount.currency()) {
            throw new IncompatibilityAccountCurrencyException();
        }
    }

    private void setCurrency(AccountCurrency accountTypeCurrency) {
        this.currency = accountTypeCurrency;
    }

    private void setAccountNo(AccountNo accountNo) {
        this.accountNo = accountNo;
    }

    private void setBalance(double aBalance) {
        this.assertArgumentRange(aBalance, 0, Double.MAX_VALUE, "Balance can't be less than zero!");
        this.setBalance(money(aBalance));
    }

    private void setBalance(MonetaryAmount aBalance) {
        if (aBalance.isNegative()) {
            throw new IllegalArgumentException("Balance can't be less than zero!");
        }
        this.balance = aBalance;
    }

    private void setBalanceUSD(double aBalance) {
        this.assertArgumentRange(aBalance, 0, Double.MAX_VALUE, "Balance can't be less than zero!");
        this.setBalanceUSD(moneyUSD(aBalance));
    }

    private void setBalanceUSD(MonetaryAmount aBalance) {
        if (aBalance.isNegative()) {
            throw new IllegalArgumentException("Balance can't be less than zero!");
        }
        this.balanceUSD = aBalance;
    }

    private MonetaryAmount moneyUSD(double anAmount) {
        return Monetary.getDefaultAmountFactory().setNumber(anAmount).setCurrency(AccountCurrency.USD.toString()).create();
    }

    private MonetaryAmount moneyPLN(double anAmount) {
        return Monetary.getDefaultAmountFactory().setNumber(anAmount).setCurrency(AccountCurrency.PLN.toString()).create();
    }

    private MonetaryAmount money(double anAmount) {
        return Monetary.getDefaultAmountFactory().setNumber(anAmount).setCurrency(currency().toString()).create();
    }

    private MonetaryAmount money(double anAmount, AccountCurrency accountCurrency) {
        return Monetary.getDefaultAmountFactory().setNumber(anAmount).setCurrency(accountCurrency.toString()).create();
    }

    private AccountCurrency currency() {
        return currency;
    }

    private String accountNoValue() {
        return accountNo.accountNo();
    }

    private MonetaryAmount balance() {
        return balance;
    }

    private MonetaryAmount balanceUSD() { return balanceUSD; }

    private String balanceString() {
        return balance.toString();
    }

    private String balanceUSDString() {
        return balanceUSD.toString();
    }
}
