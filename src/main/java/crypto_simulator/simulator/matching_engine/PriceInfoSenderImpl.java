package crypto_simulator.simulator.matching_engine;

import crypto_simulator.simulator.router.RouterPriceInfo;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.server.ServerEndpoint;

@Slf4j
@ServerEndpoint("/websocket")
public class PriceInfoSenderImpl implements PriceInfoSender, Runnable{
    private String ticker;
    private double curBestAskPrice;
    private double prevBestAskPrice;
    private double curBestBidPrice;
    private double prevBestBidPrice;
    private RouterPriceInfo meBuyToPriceSenderRouter;

    public PriceInfoSenderImpl(String ticker, RouterPriceInfo meBuyToPriceSenderRouter) {
        this.ticker = ticker;
        this.curBestAskPrice = 0;
        this.curBestBidPrice = 0;
        this.prevBestAskPrice = 0;
        this.prevBestBidPrice = 0;
        this.meBuyToPriceSenderRouter = meBuyToPriceSenderRouter;
    }

    @Override
    public void run() {
        while(true){
            try {
                checkPriceAndSendPrice();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkPriceAndSendPrice() throws InterruptedException {
        /*
        compare cur and prev
        when changed, send price info to FE server by web-socket.
        using ONLY AskPrice From M.E.BUY
        */
        this.curBestAskPrice = this.meBuyToPriceSenderRouter.receive();
        if( curBestAskPrice != prevBestAskPrice ){
            // found diff
            // TODO : send price info to FE server.
            this.prevBestAskPrice = this.curBestAskPrice; // set prev as cur price
        }
    }

    public double getCurBestAskPrice() {
        return curBestAskPrice;
    }

    public void setCurBestAskPrice(double curBestAskPrice) {
        this.curBestAskPrice = curBestAskPrice;
    }

    public double getCurBestBidPrice() {
        return curBestBidPrice;
    }

    public void setCurBestBidPrice(double curBestBidPrice) {
        this.curBestBidPrice = curBestBidPrice;
    }

}
