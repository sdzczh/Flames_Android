package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by YX on 2018/7/23.
 */

public class BlockAssetFlow {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * amount : 0.00071680
         * createtime : 2018-08-03 14:43:32
         * remain : 0.00071680
         * fee : 0.00000000
         * accounttype : 2
         * ordernum : W131533278612379
         * remark :
         * payaddress :
         * id : 21
         * state : 1
         * updatetime : 2018-08-03 14:43:32
         * type:0
         */

        private String amount;
        private String createtime;
        private String remain;
        private String fee;
        private int accounttype;
        private String ordernum;
        private String remark;
        private String payaddress;
        private int id;
        private int state;
        private String updatetime;
        int type;

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

        public String getRemain() {
            return remain;
        }

        public void setRemain(String remain) {
            this.remain = remain;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public int getAccounttype() {
            return accounttype;
        }

        public void setAccounttype(int accounttype) {
            this.accounttype = accounttype;
        }

        public String getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(String ordernum) {
            this.ordernum = ordernum;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getPayaddress() {
            return payaddress;
        }

        public void setPayaddress(String payaddress) {
            this.payaddress = payaddress;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
