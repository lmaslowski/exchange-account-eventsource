package pl.lmaslowski.ddd.exchanger.domain;

import java.util.Date;

public interface DomainEvent<T> {
    
    int eventVersion();
    
    Date occurredOn();

    T toDto();
}
