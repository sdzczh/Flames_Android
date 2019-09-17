package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by YX on 2018/7/19.
 */

public class BlockTradeInfo {

    /**
     * today : 400.00000000
     * yesterday : 100.00000000
     * total : 8700.00000000
     * list : [{"amount":"100.00000000","createtime":"07-27 ","operType":"YT交易挖矿--买方"}]
     */

    private String today;
    private String yesterday;
    private String total;
    private List<BlockTradeInfo.ListBean> list;
    private int coinType;

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getYesterday() {
        return yesterday;
    }

    public void setYesterday(String yesterday) {
        this.yesterday = yesterday;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<BlockTradeInfo.ListBean> getList() {
        return list;
    }

    public void setList(List<BlockTradeInfo.ListBean> list) {
        this.list = list;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public int getCoinType() {
        return coinType;
    }

    public static class ListBean {
        /**
         * amount : 100.00000000
         * createtime : 07-27
         * operType : YT交易挖矿--买方
         */

        private String amount;
        private String createtime;
        private String operType;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getOperType() {
            return operType;
        }

        public void setOperType(String operType) {
            this.operType = operType;
        }
    }
}
