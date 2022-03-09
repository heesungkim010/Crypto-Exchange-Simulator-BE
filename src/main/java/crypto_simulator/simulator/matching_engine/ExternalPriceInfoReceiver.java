package crypto_simulator.simulator.matching_engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;

import javax.websocket.*;
import java.io.EOFException;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
/*
The ClientEndpoint annotation a class level annotation is used to denote that a POJO is a web socket client and can be deployed as such.
https://javadoc.io/doc/javax.websocket/javax.websocket-api/latest/index.html
*/
public class ExternalPriceInfoReceiver {

    private String SocketIp = "stream.binance.com";
    private String SocketPort = "9443";
    private String SocketPathFront = "/ws/";
    private String SocketPathEnd = "usdt@bookTicker";
    //<symbol>@kline_1m";
    //<symbol>@bookTicker
    private CurrentPriceBuffer currentPriceBuffer;
    ObjectMapper objectMapper = new ObjectMapper();

    //binance websocket endpoint.
    //https://binance-docs.github.io/apidocs/spot/en/#websocket-market-streams

    Session userSession = null;

    public ExternalPriceInfoReceiver(String ticker, CurrentPriceBuffer buffer) {
        try {
            this.currentPriceBuffer = buffer;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this,
                    new URI("wss://"+SocketIp+":"+SocketPort+SocketPathFront+ticker+SocketPathEnd));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("=== Websocket opened ===");
        this.userSession = userSession;
    }

    public void close() {
        try {
            if (this.userSession != null) {
                this.userSession.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.userSession = null;
            System.out.println("===Websocket closed===");
        }
    }
    /* no need to send msg from receiver
    public void sendMessage(String message) {
        System.out.println("sendMessage : " + message);
        this.userSession.getAsyncRemote().sendText(message);
    }
    */
    @OnMessage
    public void receiveMessage(String message) throws IOException {
        /*
        TODO:
        parse msg and set current price.
        this.currentPriceBuffer.setCurPrice();
        */
        //PriceInfo priceInfo = objectMapper.readValue(message, PriceInfo.class);

        //parse the received message
        String[] tokens = message.split(",");
        String bestBid = tokens[2].split(":")[1].replaceAll("\"", "");
        String bestAsk = tokens[4].split(":")[1].replaceAll("\"", "");

        double bestBidPrice = Double.parseDouble(bestBid);
        double bestAskPrice = Double.parseDouble(bestAsk);

        currentPriceBuffer.setBestBidPrice(bestBidPrice);
        currentPriceBuffer.setBestAskPrice(bestAskPrice);
        //System.out.println(currentPriceBuffer.getBestBidPrice());
    }

    //TODO : add  @OnError @OnClose
    //https://javadoc.io/doc/javax.websocket/javax.websocket-api/latest/index.html
}