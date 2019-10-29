package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuqingzhong
 * 我的钱包
 */

public class MyWallet{
    private String totalSumOfCny;
    private int accountType;
    private List<ListBean> list;

    public void setTotalSumOfCny(String totalSumOfCny) {
        this.totalSumOfCny = totalSumOfCny;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getTotalSumOfCny() {
        return totalSumOfCny;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getAccountType() {
        return accountType;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean implements Serializable{
        /**
         "coinType": 8,  币种类型
         "cnName": "大咖币",   币种中文名
         "totalBalance": "15000",  余量
         "totalOfCny": "24" 折合人民币
         */

        private String cnName;
        private String totalBalance;
        private String totalOfCny;
        private int coinType;
        private String frozenBalance;

        public void setCnName(String cnName) {
            this.cnName = cnName;
        }

        public String getCnName() {
            return cnName;
        }

        public void setCoinType(int coinType) {
            this.coinType = coinType;
        }

        public int getCoinType() {
            return coinType;
        }

        public void setTotalBalance(String totalBalance) {
            this.totalBalance = totalBalance;
        }

        public void setTotalOfCny(String totalOfCny) {
            this.totalOfCny = totalOfCny;
        }

        public String getTotalBalance() {
            return totalBalance;
        }

        public String getTotalOfCny() {
            return totalOfCny;
        }

        public void setFrozenBalance(String frozenBalance) {
            this.frozenBalance = frozenBalance;
        }

        public String getFrozenBalance() {
            return frozenBalance;
        }
    }
}
