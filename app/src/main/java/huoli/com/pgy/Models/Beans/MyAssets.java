package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by Administrator on 2018/4/5 0005.
 */

public class MyAssets {

    /**
     * list : [{"availBalance":"100","availBalanceOfCny":"10800","frozenBlance":1,"id":1,"type":1}]
     */

    private List<MyWallet.ListBean> list;

    public void setList(List<MyWallet.ListBean> list) {
        this.list = list;
    }

    public List<MyWallet.ListBean> getList() {
        return list;
    }

}
