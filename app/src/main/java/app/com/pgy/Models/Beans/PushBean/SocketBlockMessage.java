package app.com.pgy.Models.Beans.PushBean;

import java.io.Serializable;

/**
 * 区块链详情
 * @author xuqingzhong
 */

public class SocketBlockMessage implements Serializable {
    /**
     * 103/104接口出
     * power : 2730/s         算力
     * difficulty : 0.12R    难度
     * halve_time : 94天  预计产量减半
     * profit : 1200000KN 24小时收益
     * difficulty_next : 0.1241R   下周难度预测
     */

    private String power;
    private String difficulty;
    private String halve_time;
    private String profit;
    private String difficulty_next;

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getHalve_time() {
        return halve_time;
    }

    public void setHalve_time(String halve_time) {
        this.halve_time = halve_time;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getDifficulty_next() {
        return difficulty_next;
    }

    public void setDifficulty_next(String difficulty_next) {
        this.difficulty_next = difficulty_next;
    }
}
