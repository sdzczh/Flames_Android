package huoli.com.pgy.Models.Beans;


import java.io.Serializable;
import java.util.List;

/**
 * 推送的币种行情
 * @author xuqingzhong
 */

public class PushMarketBean  implements Serializable{
    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean implements Serializable {
        /**
         * chgPrice : 0.0%
         * date : 1524625578000
         * marketType : 2
         * newPrice : 9820
         * newPriceCNY : 1384620
         * orderCoinCnName : 比特币
         * orderCoinName : BTC
         * orderCoinType : 1
         * sumAmount : 103.18
         * unitCoinName : USDT
         * unitCoinType : 2
         */

//         "orderCoinType": 8, 交易币种
//            "high": "0", 最高价
//            "unitCoinType": 0, 计价币种
//            "newPriceCNY": "2", 最新价（rmb）
//                "low": "0",最低价
//            "unitCoinName": "KN", 计价币币种名
//            "chgPrice": "0.0", 涨幅
//            "orderCoinName": "YT", 交易币币种名
//            "orderCoinCnName": "COIN的", 交易币中文名
//            "newPrice": "2", 最近价格
//            "sumAmount": "0" 成交量
        private int id;
        private String chgPrice;        /*涨跌幅*/
        private String date;            /*时间*/
        private int marketType;         /*1：COIN 2：主流*/
        private String newPrice;        /*最新价格（计价币）*/
        private String newPriceCNY;         /*最新价格（人民币）*/
        private String orderCoinCnName;     /*交易币中文名称*/
        private String orderCoinName;       /*交易币名称*/
        private int orderCoinType;          /*交易币编号*/
        private String sumAmount;           /*24小时成交量*/
        private String unitCoinName;        /*计价币名称*/
        private int unitCoinType;           /*计价币编号*/

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getChgPrice() {
            return chgPrice;
        }

        public void setChgPrice(String chgPrice) {
            this.chgPrice = chgPrice;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getMarketType() {
            return marketType;
        }

        public void setMarketType(int marketType) {
            this.marketType = marketType;
        }

        public String getNewPrice() {
            return newPrice;
        }

        public void setNewPrice(String newPrice) {
            this.newPrice = newPrice;
        }

        public String getNewPriceCNY() {
            return newPriceCNY;
        }

        public void setNewPriceCNY(String newPriceCNY) {
            this.newPriceCNY = newPriceCNY;
        }

        public String getOrderCoinCnName() {
            return orderCoinCnName;
        }

        public void setOrderCoinCnName(String orderCoinCnName) {
            this.orderCoinCnName = orderCoinCnName;
        }

        public String getOrderCoinName() {
            return orderCoinName;
        }

        public void setOrderCoinName(String orderCoinName) {
            this.orderCoinName = orderCoinName;
        }

        public int getOrderCoinType() {
            return orderCoinType;
        }

        public void setOrderCoinType(int orderCoinType) {
            this.orderCoinType = orderCoinType;
        }

        public String getSumAmount() {
            return sumAmount;
        }

        public void setSumAmount(String sumAmount) {
            this.sumAmount = sumAmount;
        }

        public String getUnitCoinName() {
            return unitCoinName;
        }

        public void setUnitCoinName(String unitCoinName) {
            this.unitCoinName = unitCoinName;
        }

        public int getUnitCoinType() {
            return unitCoinType;
        }

        public void setUnitCoinType(int unitCoinType) {
            this.unitCoinType = unitCoinType;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "id=" + id +
                    ", chgPrice='" + chgPrice + '\'' +
                    ", date='" + date + '\'' +
                    ", marketType=" + marketType +
                    ", newPrice='" + newPrice + '\'' +
                    ", newPriceCNY='" + newPriceCNY + '\'' +
                    ", orderCoinCnName='" + orderCoinCnName + '\'' +
                    ", orderCoinName='" + orderCoinName + '\'' +
                    ", orderCoinType=" + orderCoinType +
                    ", sumAmount='" + sumAmount + '\'' +
                    ", unitCoinName='" + unitCoinName + '\'' +
                    ", unitCoinType=" + unitCoinType +
                    '}';
        }
    }
}
