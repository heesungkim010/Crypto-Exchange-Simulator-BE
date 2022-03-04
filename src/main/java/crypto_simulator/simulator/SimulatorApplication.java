package crypto_simulator.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URISyntaxException;

@SpringBootApplication
public class SimulatorApplication {

	public static void main(String[] args) throws URISyntaxException, InterruptedException {
		SpringApplication.run(SimulatorApplication.class, args);
		WebSocketClient testEndpointClient = new WebSocketClient();
		testEndpointClient.close();
	}
}
