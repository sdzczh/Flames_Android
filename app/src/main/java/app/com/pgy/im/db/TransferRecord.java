package app.com.pgy.im.db;

import java.io.Serializable;
import java.util.List;

/**
 * 转账记录
 * @author xuqingzhong
 */

public class TransferRecord {

    /**
     * list : [{"amount":"200","createTime":"18/01/09 17:12","id":16,"coinType":0,"invalidTime":"18/04/09 17:12","service":"挖火蚁"}]
     */

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean implements Serializable{

        private String name;
        private String amount;
        private int coinType;
        private String createTime;
        private int id;
        private String priceOfCNy;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPriceOfCNy() {
            return priceOfCNy;
        }

        public void setPriceOfCNy(String priceOfCNy) {
            this.priceOfCNy = priceOfCNy;
        }
    }
}
