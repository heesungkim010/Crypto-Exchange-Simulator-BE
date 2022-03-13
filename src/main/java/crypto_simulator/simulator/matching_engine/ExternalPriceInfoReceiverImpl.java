package crypto_simulator.simulator.matching_engine;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
/*
The ClientEndpoint annotation a class level annotation is used to denote that a POJO is a web socket client and can be deployed as such.
https://javadoc.io/doc/javax.websocket/javax.websocket-api/latest/index.html
*/
public class ExternalPriceInfoReceiverImpl implements ExternalPriceInfoReceiver{

    private String SocketIp = "stream.binance.com";
    private String SocketPort = "9443";
    private String SocketPathFront = "/ws/";
    private String SocketPathEnd = "usdt@bookTicker";
    private String ticker;
    private double bestBidPrice;
    private double bestAskPrice;

    //<symbol>@kline_1m";
    //<symbol>@bookTicker
    ObjectMapper objectMapper = new ObjectMapper();

    //binance websocket endpoint.
    //https://binance-docs.github.io/apidocs/spot/en/#websocket-market-streams

    Session userSession = null;

    public ExternalPriceInfoReceiverImpl(String ticker) {
        try {
            this.ticker = ticker;
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

    @Override
    public double getBestBidPrice() {
        return this.bestBidPrice;
    }

    @Override
    public void setBestBidPrice(double price) {
        this.bestBidPrice = price;
    }

    @Override
    public double getBestAskPrice() {
        return this.bestAskPrice;
    }

    @Override
    public void setBestAskPrice(double price) {
        this.bestAskPrice = price;
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

        /*
        if(ticker == "btc"){
            bestAskPrice = (Math.round(bestAskPrice * 2) / 2);
        }
        */

        this.bestBidPrice = Double.parseDouble(bestBid);
        this.bestAskPrice = Double.parseDouble(bestAsk);

        //System.out.println(currentPriceBuffer.getBestBidPrice());
    }

    //TODO : add  @OnError @OnClose
    //https://javadoc.io/doc/javax.websocket/javax.websocket-api/latest/index.html
}