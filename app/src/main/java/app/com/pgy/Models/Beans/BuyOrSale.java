package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

import app.com.pgy.Models.Beans.PushBean.RecordsBean;

/**
 * Created by xuqingzhong on 2017/12/26 0026.
 * @author xuqingzhong
 * 买卖类
 */

public class BuyOrSale{


    /**
     * buys : []
     * records : [{"orderType":1,"amount":"1","createTime":"11:52:02","price":"10"},{"orderType":1,"amount":"1","createTime":"14:44:20","price":"6.24"},{"orderType":0,"amount":"0.41","createTime":"13:59:31","price":"0.59"},{"orderType":0,"amount":"0.09","createTime":"13:59:10","price":"0.59"},{"orderType":1,"amount":"0.02","createTime":"13:57:41","price":"0.55"},{"orderType":1,"amount":"0.02","createTime":"13:57:33","price":"0.55"},{"orderType":1,"amount":"0.02","createTime":"13:57:24","price":"0.55"},{"orderType":1,"amount":"0.02","createTime":"13:57:10","price":"0.55"},{"orderType":1,"amount":"1","createTime":"11:49:40","price":"0.03"},{"orderType":0,"amount":"0.25","createTime":"11:48:10","price":"0.04"},{"orderType":0,"amount":"0.5","createTime":"11:47:08","price":"25.5"},{"orderType":0,"amount":"0.5","createTime":"11:47:08","price":"25"},{"orderType":0,"amount":"0.9","createTime":"11:47:08","price":"24"},{"orderType":0,"amount":"0.5","createTime":"11:47:08","price":"23.5"},{"orderType":0,"amount":"0.6","createTime":"11:47:08","price":"23"},{"orderType":0,"amount":"1","createTime":"11:47:08","price":"22.6"},{"orderType":0,"amount":"0.58","createTime":"11:47:08","price":"22.58"},{"orderType":0,"amount":"1","createTime":"11:47:08","price":"21.5"},{"orderType":0,"amount":"0.06113208","createTime":"11:47:08","price":"21.2"},{"orderType":0,"amount":"7.598","createTime":"11:47:08","price":"20"}]
     * price : 10
     * sales : []
     */

    private String rmbRate;
    private String price;
    private List<ListBean> buys;
    private List<RecordsBean> records;
    private List<ListBean> sales;
    private List<KLineBean.ListBean> zline;

    public List<KLineBean.ListBean> getKline() {
        return zline;
    }

    public void setKline(List<KLineBean.ListBean> kline) {
        this.zline = kline;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<ListBean> getBuys() {
        return buys;
    }

    public void setBuys(List<ListBean> buys) {
        this.buys = buys;
    }

    public List<ListBean> getSales() {
        return sales;
    }

    public void setSales(List<ListBean> sales) {
        this.sales = sales;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public void setRmbRate(String rmbRate) {
        this.rmbRate = rmbRate;
    }

    public String getRmbRate() {
        return rmbRate;
    }

    public static class ListBean implements Serializable{


        /**
         * rate : 14.29
         * price : 1
         * remain : 1
         * num : 1
         */

        private String rate;
        private String price;
        private String remain;
        private int num;

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRemain() {
            return remain;
        }

        public void setRemain(String remain) {
            this.remain = remain;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

    @Override
    public String toString() {
        return "BuyOrSale{" +
                "price='" + price + '\'' +
                ", buys=" + buys +
                ", records=" + records +
                ", sales=" + sales +
                '}';
    }
}
