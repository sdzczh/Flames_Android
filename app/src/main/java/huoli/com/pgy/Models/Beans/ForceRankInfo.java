package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by YX on 2018/7/23.
 */

public class ForceRankInfo {

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public class ListBean{
        /**
         * account : 135****0001
         * forceNumber : 1
         * id : 1
         * rank : 0
         * honorName : 魂师
         */

        private String account; //账户
        private int forceNumber;//算力
        private int id;
        private int rank;//排名
        private String honorName;//称号名称 String

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getForceNumber() {
            return forceNumber;
        }

        public void setForceNumber(int forceNumber) {
            this.forceNumber = forceNumber;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getHonorName() {
            return honorName;
        }

        public void setHonorName(String honorName) {
            this.honorName = honorName;
        }
    }

}
