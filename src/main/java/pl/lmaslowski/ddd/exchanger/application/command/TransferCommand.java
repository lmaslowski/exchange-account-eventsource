package pl.lmaslowski.ddd.exchanger.application.command;

public final class TransferCommand {
    private final String sourceAccountNo;
    private final String destinationAccountNo;
    private final double amount;
    
    public TransferCommand(String aSourceAccountNo, String aDestinationAccountNo, double anAmount) {
        sourceAccountNo = aSourceAccountNo;
        destinationAccountNo = aDestinationAccountNo;
        amount = anAmount;
    }
    
    public String getSourceAccountNo() {
        return sourceAccountNo;
    }
    
    public String getDestinationAccountNo() {
        return destinationAccountNo;
    }
    
    public double getAmount() {
        return amount;
    }
}
