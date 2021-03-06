package app.com.pgy.Models.Beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Android on 2019/10/14 0014
 */
public class MyAccount {
    String accountBalanceCny;
    String accountBalance;
    Map<Integer,ListBean> accountList;

    public void setAccountBalanceCny(String accountBalanceCny) {
        this.accountBalanceCny = accountBalanceCny;
    }

    public String getAccountBalanceCny() {
        return accountBalanceCny;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountList(Map<Integer, ListBean> accountList) {
        this.accountList = accountList;
    }

    public Map<Integer, ListBean> getAccountList() {
        if (accountList == null){
            accountList = new HashMap<>();
        }
        return accountList;
    }

    public static class ListBean{
        String total;
        String totalCny;

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotal() {
            return total;
        }

        public void setTotalCny(String totalCny) {
            this.totalCny = totalCny;
        }

        public String getTotalCny() {
            return totalCny;
        }
    }
}
