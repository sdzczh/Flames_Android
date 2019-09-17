package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by YX on 2018/6/5.
 */

public class FinancialBean {
    private String totalSumOfCny;
    private List<FinancialListBean> list;

    public void setTotalSumOfCny(String totalSumOfCny) {
        this.totalSumOfCny = totalSumOfCny;
    }

    public String getTotalSumOfCny() {
        return totalSumOfCny;
    }

    public void setList(List<FinancialListBean> list) {
        this.list = list;
    }

    public List<FinancialListBean> getList() {
        return list;
    }

    public static class FinancialListBean{
        private String availTradeBalance;
        private String frozenTradeBlance;
        private String loanedTradeBlance;
        private String totalTradeBalance;
        private String availUnitBalance;
        private String frozenUnitBlance;
        private String loanedUnitBlance;
        private String totalUnitBalance;
        private int id;
        private int orderCoin;
        private int unitCoin;
        private String totalOfCny;
        private int type;
        private String totalBalance;

        public void setAvailTradeBalance(String availTradeBalance) {
            this.availTradeBalance = availTradeBalance;
        }

        public String getAvailTradeBalance() {
            return availTradeBalance;
        }

        public void setFrozenTradeBlance(String frozenTradeBlance) {
            this.frozenTradeBlance = frozenTradeBlance;
        }

        public String getFrozenTradeBlance() {
            return frozenTradeBlance;
        }

        public void setTotalTradeBalance(String totalTradeBalance) {
            this.totalTradeBalance = totalTradeBalance;
        }

        public String getTotalTradeBalance() {
            return totalTradeBalance;
        }

        public void setAvailUnitBalance(String availUnitBalance) {
            this.availUnitBalance = availUnitBalance;
        }

        public String getAvailUnitBalance() {
            return availUnitBalance;
        }

        public void setFrozenUnitBlance(String frozenUnitBlance) {
            this.frozenUnitBlance = frozenUnitBlance;
        }

        public String getFrozenUnitBlance() {
            return frozenUnitBlance;
        }

        public void setLoanedUnitBlance(String loanedUnitBlance) {
            this.loanedUnitBlance = loanedUnitBlance;
        }

        public String getLoanedUnitBlance() {
            return loanedUnitBlance;
        }

        public String getTotalUnitBalance() {
            return totalUnitBalance;
        }

        public void setTotalUnitBalance(String totalUnitBalance) {
            this.totalUnitBalance = totalUnitBalance;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setTotalOfCny(String totalOfCny) {
            this.totalOfCny = totalOfCny;
        }

        public String getTotalOfCny() {
            return totalOfCny;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setLoanedTradeBlance(String loanedTradeBlance) {
            this.loanedTradeBlance = loanedTradeBlance;
        }

        public String getLoanedTradeBlance() {
            return loanedTradeBlance;
        }

        public void setOrderCoin(int orderCoin) {
            this.orderCoin = orderCoin;
        }

        public int getOrderCoin() {
            return orderCoin;
        }

        public void setUnitCoin(int unitCoin) {
            this.unitCoin = unitCoin;
        }

        public int getUnitCoin() {
            return unitCoin;
        }

        public void setTotalBalance(String totalBalance) {
            this.totalBalance = totalBalance;
        }

        public String getTotalBalance() {
            return totalBalance;
        }
    }

}
