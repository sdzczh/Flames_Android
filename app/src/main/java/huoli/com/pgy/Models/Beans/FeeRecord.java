package huoli.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * 挖火蚁消费记录
 * @author xuqingzhong
 */

public class FeeRecord {

    /**
     * intervalDays : 90
     * list : [{"amount":"200","createTime":"18/01/09 17:12","id":16,"coinType":0,"invalidTime":"18/04/09 17:12","service":"挖火蚁"}]
     */

    private int intervalDays;
    private List<ListBean> list;

    public void setIntervalDays(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public int getIntervalDays() {
        return intervalDays;
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
         * invalidTime : 18/04/09 17:12
         * service : 挖火蚁
         */

        private String amount;
        private String createTime;
        private int id;
        private int coinType;
        private String invalidTime;
        private String service;

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

        public void setInvalidTime(String invalidTime) {
            this.invalidTime = invalidTime;
        }

        public void setService(String service) {
            this.service = service;
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

        public String getInvalidTime() {
            return invalidTime;
        }

        public String getService() {
            return service;
        }
    }
}
