package app.com.pgy.Models.Beans;

import java.util.List;

import app.com.pgy.Models.Beans.PushBean.RecordsBean;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;

/**
 * @author xuqingzhong
 * k线图数据
 */
public class KLineBean{

    /**
     * market : {"orderCoinType":8,"high":"0","unitCoinType":0,"newPriceCNY":"2","low":"0","unitCoinName":"KN","chgPrice":"0.0","orderCoinName":"YT","orderCoinCnName":"COIN的","newPrice":"2","sumAmount":"0"}
     * records : [{"orderType":"卖出","amount":"1","createTime":"2018-07-25 17:08:35","price":"2"},{"orderType":"卖出","amount":"1000","createTime":"2018-07-23 10:09:50","price":"0.0016"}]
     * kline : [{"amount":"1000","timeInteval":1800000,"minPrice":"0","openPrice":"0","closePrice":"0","maxPrice":"0","marketType":1,"timestamp":1531972800000}]
     */

    private MarketBean market;
    private List<RecordsBean> records;
    private List<ListBean> kline;

    public MarketBean getMarket() {
        return market;
    }

    public void setMarket(MarketBean market) {
        this.market = market;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public List<ListBean> getKline() {
        return kline;
    }

    public void setKline(List<ListBean> kline) {
        this.kline = kline;
    }

    public static class MarketBean {
        /**
         * orderCoinType : 8
         * high : 0
         * unitCoinType : 0
         * newPriceCNY : 2
         * low : 0
         * unitCoinName : KN
         * chgPrice : 0.0
         * orderCoinName : YT
         * orderCoinCnName : COIN的
         * newPrice : 2
         * sumAmount : 0
         */

        private int orderCoinType;
        private String high;
        private int unitCoinType;
        private String newPriceCNY;
        private String low;
        private String unitCoinName;
        private String chgPrice;
        private String orderCoinName;
        private String orderCoinCnName;
        private String newPrice;
        private String sumAmount;

        public int getOrderCoinType() {
            return orderCoinType;
        }

        public void setOrderCoinType(int orderCoinType) {
            this.orderCoinType = orderCoinType;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public int getUnitCoinType() {
            return unitCoinType;
        }

        public void setUnitCoinType(int unitCoinType) {
            this.unitCoinType = unitCoinType;
        }

        public String getNewPriceCNY() {
            return newPriceCNY;
        }

        public void setNewPriceCNY(String newPriceCNY) {
            this.newPriceCNY = newPriceCNY;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getUnitCoinName() {
            return unitCoinName;
        }

        public void setUnitCoinName(String unitCoinName) {
            this.unitCoinName = unitCoinName;
        }

        public String getChgPrice() {
            return chgPrice;
        }

        public void setChgPrice(String chgPrice) {
            this.chgPrice = chgPrice;
        }

        public String getOrderCoinName() {
            return orderCoinName;
        }

        public void setOrderCoinName(String orderCoinName) {
            this.orderCoinName = orderCoinName;
        }

        public String getOrderCoinCnName() {
            return orderCoinCnName;
        }

        public void setOrderCoinCnName(String orderCoinCnName) {
            this.orderCoinCnName = orderCoinCnName;
        }

        public String getNewPrice() {
            return newPrice;
        }

        public void setNewPrice(String newPrice) {
            this.newPrice = newPrice;
        }

        public String getSumAmount() {
            return sumAmount;
        }

        public void setSumAmount(String sumAmount) {
            this.sumAmount = sumAmount;
        }

        @Override
        public String toString() {
            return "MarketBean{" +
                    "orderCoinType=" + orderCoinType +
                    ", high='" + high + '\'' +
                    ", unitCoinType=" + unitCoinType +
                    ", newPriceCNY='" + newPriceCNY + '\'' +
                    ", low='" + low + '\'' +
                    ", unitCoinName='" + unitCoinName + '\'' +
                    ", chgPrice='" + chgPrice + '\'' +
                    ", orderCoinName='" + orderCoinName + '\'' +
                    ", orderCoinCnName='" + orderCoinCnName + '\'' +
                    ", newPrice='" + newPrice + '\'' +
                    ", sumAmount='" + sumAmount + '\'' +
                    '}';
        }
    }

    public static class ListBean {
        /**
         * amount : 1000
         * timeInteval : 1800000
         * minPrice : 0
         * openPrice : 0
         * closePrice : 0
         * maxPrice : 0
         * marketType : 1
         * timestamp : 1531972800000
         */

        private String amount;
        private int timeInteval;
        private String minPrice;
        private String openPrice;
        private String closePrice;
        private String maxPrice;
        private int marketType;
        private long timestamp;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getTimeInteval() {
            return timeInteval;
        }

        public void setTimeInteval(int timeInteval) {
            this.timeInteval = timeInteval;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(String minPrice) {
            this.minPrice = minPrice;
        }

        public String getOpenPrice() {
            return openPrice;
        }

        public void setOpenPrice(String openPrice) {
            this.openPrice = openPrice;
        }

        public String getClosePrice() {
            return closePrice;
        }

        public void setClosePrice(String closePrice) {
            this.closePrice = closePrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public int getMarketType() {
            return marketType;
        }

        public void setMarketType(int marketType) {
            this.marketType = marketType;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public float getOpen() {
            return MathUtils.string2float(openPrice);
        }

        public float getClose() {
            return MathUtils.string2float(closePrice);
        }

        public float getHigh() {
            float v = MathUtils.string2float(maxPrice);
            return v>0?v:Math.max(getOpen(),getClose());
        }

        public float getLow() {
            float v = MathUtils.string2float(minPrice);
            return v>0?v:Math.min(getOpen(),getClose());
        }

        public float getVol() {
            return MathUtils.string2float(amount);
        }

        public String getDate() {
            return TimeUtils.dateToString(timestamp);
        }
    }
}

