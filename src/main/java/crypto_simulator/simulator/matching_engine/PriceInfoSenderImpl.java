package crypto_simulator.simulator.matching_engine;

public class PriceInfoSenderImpl implements PriceInfoSender, Runnable{
    private double curBestAskPrice;
    private double prevBestAskPrice;
    private double curBestBidPrice;
    private double prevBestBidPrice;

    public PriceInfoSenderImpl(String ticker) {
        this.curBestAskPrice = 0;
        this.curBestBidPrice = 0;
        this.prevBestAskPrice = 0;
        this.prevBestBidPrice = 0;
    }

    @Override
    public void run() {
        while(true){
            checkPriceAndSendPrice();
        }
    }

    public void checkPriceAndSendPrice(){
        /*
        compare cur and prev
        when changed, send price info to FE server by web-socket.
        */

        if((curBestAskPrice > prevBestAskPrice) || (curBestAskPrice < prevBestAskPrice) ){
            // found diff
            // TODO : send price info to FE server.
            this.prevBestAskPrice = this.curBestAskPrice; // set prev as cur price
        }
        if(curBestBidPrice != prevBestBidPrice){

            this.prevBestBidPrice = this.curBestBidPrice;
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
