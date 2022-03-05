package crypto_simulator.simulator;

import crypto_simulator.simulator.matching_engine.PriceInfoReceiver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URISyntaxException;

@SpringBootApplication
public class SimulatorApplication {

	public static void main(String[] args) throws URISyntaxException, InterruptedException {
		SpringApplication.run(SimulatorApplication.class, args);
		PriceInfoReceiver priceInfoReceiverBTC = new PriceInfoReceiver("btc");
		PriceInfoReceiver priceInfoReceiverETH = new PriceInfoReceiver("eth");
		//testEndpointClient.close();
	}
}
