package crypto_simulator.simulator;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint

/*
The ClientEndpoint annotation a class level annotation is used to denote that a POJO is a web socket client and can be deployed as such.
https://javadoc.io/doc/javax.websocket/javax.websocket-api/latest/index.html
*/
public class WebSocketClient {

    public String SocketIp = "stream.binance.com";
    public String SocketPort = "9443";
    public String SocketPath = "/ws/btcusdt@kline_1m";
    //binance websocket endpoint.
    //https://binance-docs.github.io/apidocs/spot/en/#websocket-market-streams

    Session userSession = null;

    public WebSocketClient() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("wss://"+SocketIp+":"+SocketPort+SocketPath));
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

    public void sendMessage(String message) {
        System.out.println("sendMessage : " + message);
        this.userSession.getAsyncRemote().sendText(message);
    }

    @OnMessage
    public void receiveMessage(String message) throws IOException {
        System.out.println("### Message from the server : " + message);
    }

    //TODO : add  @OnError @OnClose
    //https://javadoc.io/doc/javax.websocket/javax.websocket-api/latest/index.html
}