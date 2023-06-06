package pl.lmaslowski.ddd.exchanger.interfaces.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lmaslowski.ddd.exchanger.domain.AccountDoesNotExistsException;
import pl.lmaslowski.ddd.exchanger.domain.IncompatibilityAccountCurrencyException;
import pl.lmaslowski.ddd.exchanger.domain.InsufficientBalanceException;

@RestController
@ControllerAdvice
public class ExceptionHandlingAdvice {
    
    @ExceptionHandler({
            HttpMessageConversionException.class,
            BindException.class,
            IllegalArgumentException.class,
            InsufficientBalanceException.class,
            IncompatibilityAccountCurrencyException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto validationError(Throwable ex) {
        return new ErrorDto(ex.getClass().getName(), ex.getMessage());
    }
    
    @ExceptionHandler({
            AccountDoesNotExistsException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto notFound(Throwable ex) {
        return new ErrorDto(ex.getClass().getName(), ex.getMessage());
    }
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto handle(Throwable ex) {
        return new ErrorDto(ex.getClass().getName(), ex.getMessage());
    }
    
    public static class ErrorDto {
        private String message;
        private String exceptionClass;
        
        public ErrorDto(String message) {
            this.message = message;
        }
        
        ErrorDto(String exceptionClass, String message) {
            this.exceptionClass = exceptionClass;
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getExceptionClass() {
            return exceptionClass;
        }
    }
}
