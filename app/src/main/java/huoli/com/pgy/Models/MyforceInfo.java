package huoli.com.pgy.Models;

/**
 * Created by YX on 2018/7/23.
 */

public class MyforceInfo {

    /**
     * differForce : 61 // 距离下一级别差的算力值
     * rank : 4// 算力排名
     * currentForce : 40// 我的算力
     */

    private int differForce;
    private int rank;
    private int currentForce;

    public int getDifferForce() {
        return differForce;
    }

    public void setDifferForce(int differForce) {
        this.differForce = differForce;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getCurrentForce() {
        return currentForce;
    }

    public void setCurrentForce(int currentForce) {
        this.currentForce = currentForce;
    }
}
