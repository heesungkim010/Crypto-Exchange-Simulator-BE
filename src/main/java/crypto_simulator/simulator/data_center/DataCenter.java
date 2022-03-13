package crypto_simulator.simulator.data_center;

import crypto_simulator.simulator.service.FilledOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

public class DataCenter {
    private final FilledOrderService filledOrderService;


    @Autowired
    public DataCenter(FilledOrderService filledOrderService, String ticker) {
        this.filledOrderService = filledOrderService;
    }
}