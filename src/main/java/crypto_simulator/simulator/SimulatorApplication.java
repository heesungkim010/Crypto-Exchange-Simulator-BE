package crypto_simulator.simulator;

import org.java_websocket.client.WebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class SimulatorApplication {

	public static void main(String[] args) throws URISyntaxException, InterruptedException {
		SpringApplication.run(SimulatorApplication.class, args);

		WebSocketClient client = new SocketClient(new URI("wss://stream.binance.com:9443/ws/bnbusdt@kline_1m"));
		WebSocketClient client2 = new SocketClient(new URI("wss://stream.binance.com:9443/ws/btcusdt@kline_1m"));
		client.connect(); // connect web socket with binance exchange
		client2.connect();
		Thread.sleep(5000);
		client.close();
	}

}
