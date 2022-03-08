package crypto_simulator.simulator;

import crypto_simulator.simulator.domain.NewOrder;
import crypto_simulator.simulator.domain.OrderStatus;
import crypto_simulator.simulator.domain.OrderType;
import crypto_simulator.simulator.domain.Ticker;
import crypto_simulator.simulator.matching_engine.ExternalPriceInfoReceiver;
import crypto_simulator.simulator.matching_engine.ReservedOrders;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.currentTimeMillis;

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
