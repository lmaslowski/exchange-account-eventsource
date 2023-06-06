package pl.lmaslowski.ddd.exchanger.application.command;

public final class NewPLNAccountWithDepositCommand {
    private final double balance;
    private final String name;
    private final String lastName;

    public NewPLNAccountWithDepositCommand(double balance, String name, String lastName) {
        this.balance = balance;
        this.name = name;
        this.lastName = lastName;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }
}
