package app.com.pgy.Models.Beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Android on 2019/10/14 0014
 */
public class MyAccount {
    String accountBalanceCny;
    Map<Integer,ListBean> accountList;

    public void setAccountBalanceCny(String accountBalanceCny) {
        this.accountBalanceCny = accountBalanceCny;
    }

    public String getAccountBalanceCny() {
        return accountBalanceCny;
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

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotal() {
            return total;
        }
    }
}
