package crypto_simulator.simulator.matching_engine;

import javax.websocket.Session;
import java.io.IOException;

public interface ExternalPriceInfoReceiver {
    void receiveMessage(String message) throws IOException;
    void close();
    public void onOpen(Session userSession);

}
