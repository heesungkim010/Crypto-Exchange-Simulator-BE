package crypto_simulator.simulator.matching_engine;

public class PriceInfoSenderImpl implements PriceInfoSender{
    private double curBestAskPrice;
    private double prevBestAskPrice;
    private double curBestBidPrice;
    private double prevBestBidPrice;

    public PriceInfoSenderImpl(String ticker) {

    }
    /*
    TODO:
        compare cur and prev
        when changed, send price info to FE server by web-socket.
    */

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
