package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * 挖矿记录
 * @author xuqingzhong
 */

public class DigRecord {

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean implements Serializable{
        /**
         * amount : 200
         * createTime : 18/01/09 17:12
         * id : 16
         * coinType : 0
         */

        private String amount;
        private String createTime;
        private int id;
        private int coinType;

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCoinType(int coinType) {
            this.coinType = coinType;
        }

        public String getAmount() {
            return amount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public int getId() {
            return id;
        }

        public int getCoinType() {
            return coinType;
        }

    }
}
