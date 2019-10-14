package app.com.pgy.Models.Beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Android on 2019/10/14 0014
 */
public class MyAccount {
    String accountBalanceCny;
    Map<Integer,String> accountList;

    public void setAccountBalanceCny(String accountBalanceCny) {
        this.accountBalanceCny = accountBalanceCny;
    }

    public String getAccountBalanceCny() {
        return accountBalanceCny;
    }

    public void setAccountList(Map<Integer, String> accountList) {
        this.accountList = accountList;
    }

    public Map<Integer, String> getAccountList() {
        if (accountList == null){
            accountList = new HashMap<>();
        }
        return accountList;
    }
}
