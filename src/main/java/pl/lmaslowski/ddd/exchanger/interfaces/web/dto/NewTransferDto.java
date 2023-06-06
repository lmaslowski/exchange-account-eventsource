package pl.lmaslowski.ddd.exchanger.interfaces.web.dto;

import javax.validation.constraints.NotNull;

public class NewTransferDto {
    @NotNull
    private String sourceAccountNo;
    @NotNull
    private String destinationAccountNo;
    @NotNull
    private double amount;
    
    public NewTransferDto(String sourceAccountNo, String destinationAccountNo, double amount) {
        this.sourceAccountNo = sourceAccountNo;
        this.destinationAccountNo = destinationAccountNo;
        this.amount = amount;
    }
    
    private NewTransferDto() {
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
