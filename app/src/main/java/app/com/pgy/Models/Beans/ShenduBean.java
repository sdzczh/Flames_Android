package app.com.pgy.Models.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzz on 2020/4/2.
 */

public class ShenduBean {

    public List<List<String>> asks = new ArrayList<>();
    public List<List<String>> bids = new ArrayList<>();

    public List<List<String>> getBids() {
        return bids;
    }

    public void setBids(List<List<String>> bids) {
        this.bids = bids;
    }

    public void setAsks(List<List<String>> asks) {
        this.asks = asks;
    }

    public List<List<String>> getAsks() {
        return asks;
    }

}
