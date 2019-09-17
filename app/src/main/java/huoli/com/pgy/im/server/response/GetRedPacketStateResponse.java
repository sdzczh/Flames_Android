package huoli.com.pgy.im.server.response;

import java.io.Serializable;
import java.util.List;

/**
 * 创建日期：2018/5/21 0021 on 下午 4:10
 * 描述:获取红包状态
 *
 * @author 徐庆重
 */

public class GetRedPacketStateResponse implements Serializable{

    /**
     * remainNum : 1
     * amount : 2
     * amtOfCny : 20
     * remainAmt : 2
     * num : 1
     * coinType : 2
     * name : 13500010001
     * state : 0
     * inactiveTime : 2018-05-2221: 01: 14
     * note : 恭喜发财
     * headPath : http: //img.huolicoin.com/fai/001/13500010001/201803020811479186.png
     * reciveList : [{"reciveTime":"2018-05-2121: 01: 10","amount":"2","amtOfCny":"20","besthand":0,"coinType":2,"name":"小可爱","headPath":""}]
     */

    private int remainNum;
    private String amount;
    private String amtOfCny;
    private String remainAmt;
    private int num;
    private int coinType;
    private String name;
    private int state;
    private String inactiveTime;
    private String note;
    private String headPath;
    private List<ReciveListBean> reciveList;

    public int getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(int remainNum) {
        this.remainNum = remainNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmtOfCny() {
        return amtOfCny;
    }

    public void setAmtOfCny(String amtOfCny) {
        this.amtOfCny = amtOfCny;
    }

    public String getRemainAmt() {
        return remainAmt;
    }

    public void setRemainAmt(String remainAmt) {
        this.remainAmt = remainAmt;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getInactiveTime() {
        return inactiveTime;
    }

    public void setInactiveTime(String inactiveTime) {
        this.inactiveTime = inactiveTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public List<ReciveListBean> getReciveList() {
        return reciveList;
    }

    public void setReciveList(List<ReciveListBean> reciveList) {
        this.reciveList = reciveList;
    }

    public static class ReciveListBean implements Serializable{
        /**
         * reciveTime : 2018-05-2121: 01: 10
         * amount : 2
         * amtOfCny : 20
         * besthand : 0
         * coinType : 2
         * name : 小可爱
         * headPath :
         */

        private String reciveTime;
        private String amount;
        private String amtOfCny;
        private int besthand;
        private int coinType;
        private String name;
        private String headPath;

        public String getReciveTime() {
            return reciveTime;
        }

        public void setReciveTime(String reciveTime) {
            this.reciveTime = reciveTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAmtOfCny() {
            return amtOfCny;
        }

        public void setAmtOfCny(String amtOfCny) {
            this.amtOfCny = amtOfCny;
        }

        public int getBesthand() {
            return besthand;
        }

        public void setBesthand(int besthand) {
            this.besthand = besthand;
        }

        public int getCoinType() {
            return coinType;
        }

        public void setCoinType(int coinType) {
            this.coinType = coinType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadPath() {
            return headPath;
        }

        public void setHeadPath(String headPath) {
            this.headPath = headPath;
        }
    }
}
