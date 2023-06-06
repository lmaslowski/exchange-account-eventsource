package pl.lmaslowski.ddd.exchanger.infrastructure.exchangeapi;

import org.springframework.stereotype.Service;
import pl.lmaslowski.ddd.exchanger.domain.port.inbound.ExchangeRate;

@Service
public class ExchangeRateApiStub implements ExchangeRate {

    @Override
    public double getUSDPLN() {
        return 4.18;
    }
}
