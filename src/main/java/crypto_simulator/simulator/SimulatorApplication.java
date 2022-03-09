package crypto_simulator.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URISyntaxException;

@SpringBootApplication
public class SimulatorApplication {

	public static void main(String[] args) throws URISyntaxException, InterruptedException {
		SpringApplication.run(SimulatorApplication.class, args);
		//AppConfig appConfig = new AppConfig();

		/*
		NewOrder newOrder = new NewOrder(1L, Ticker.BTCUSD, OrderStatus.OPEN, OrderType.BUY,
				40000, 10, 0.1, 20, 90000, 0, LocalDateTime.now(),1L);
		hashMap.put(1L, newOrder);
		*/
		/*
		System.out.println("start");
		long start = currentTimeMillis();

		ReservedOrders[] priceIndexArray = new ReservedOrders[180000];

		System.out.println("finish");
		long end = currentTimeMillis();
		System.out.println(end-start);
		*/
	}
}
