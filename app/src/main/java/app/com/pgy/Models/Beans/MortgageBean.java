package app.com.pgy.Models.Beans;

import java.util.List;

public class MortgageBean {
        /**
         * digDoc : https://www.eolinker.com
         * digList : [{"amount":30,"cointype":8,"createtime":1574705256000,"id":4,"updatetime":1574705256000,"userid":2,"isTeam":1},{"amount":40,"cointype":8,"createtime":1574705255000,"id":3,"updatetime":1574705255000,"userid":2,"isTeam":0},{"amount":20,"cointype":8,"createtime":1574705253000,"id":2,"updatetime":1574705253000,"userid":2,"isTeam":0},{"amount":10,"cointype":8,"createtime":1574618851000,"id":1,"updatetime":1574705251000,"userid":2,"isTeam":0}]
         * mortgageCoinList : [8]
         * totalProfit : 100.0
         * rate : 0.05
         * yesTodayProfit : 60.0
         */

        private String digDoc;
        private double totalProfit;
        private double rate;
        private double yesTodayProfit;
        private List<DigListBean> digList;
        private List<Integer> mortgageCoinList;

        public String getDigDoc() {
            return digDoc;
        }

        public void setDigDoc(String digDoc) {
            this.digDoc = digDoc;
        }

        public double getTotalProfit() {
            return totalProfit;
        }

        public void setTotalProfit(double totalProfit) {
            this.totalProfit = totalProfit;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public double getYesTodayProfit() {
            return yesTodayProfit;
        }

        public void setYesTodayProfit(double yesTodayProfit) {
            this.yesTodayProfit = yesTodayProfit;
        }

        public List<DigListBean> getDigList() {
            return digList;
        }

        public void setDigList(List<DigListBean> digList) {
            this.digList = digList;
        }

        public List<Integer> getMortgageCoinList() {
            return mortgageCoinList;
        }

        public void setMortgageCoinList(List<Integer> mortgageCoinList) {
            this.mortgageCoinList = mortgageCoinList;
        }

        public static class DigListBean {
            /**
             * amount : 30.0
             * cointype : 8
             * createtime : 1574705256000
             * id : 4
             * updatetime : 1574705256000
             * userid : 2
             * isTeam : 1
             */

            private double amount;
            private int cointype;
            private long createtime;
            private int id;
            private long updatetime;
            private int userid;
            private int isTeam;

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public int getCointype() {
                return cointype;
            }

            public void setCointype(int cointype) {
                this.cointype = cointype;
            }

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(long updatetime) {
                this.updatetime = updatetime;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public int getIsTeam() {
                return isTeam;
            }

            public void setIsTeam(int isTeam) {
                this.isTeam = isTeam;
            }
        }

}
