package app.com.pgy.Models.Beans.PushBean;

import java.util.List;

/**
 * 长连接区块链对象，包含一个BlockBean
 * @author xuqingzhong
 */

public class SocketBlockList {


    /**
     * remain : 5809875  剩余火粒/火蚁数量
     * digAmount : 3290125  已出矿数量
     * todayDigAmount : "0.0000"    今日挖矿
     * block : {"blockCode":"2322","createTime":"21:31","text":"已出矿"}
     */

    private int remain;
    private int digAmount;
    private String todayDigAmount;
    private BlockBean block;
    /**
     * blocks : [{"blockCode":19599,"createTime":"13:33","text":"已出矿"},{"blockCode":19600,"createTime":"13:33","text":"已出矿"},{"blockCode":19601,"createTime":"13:33","text":"已出矿"},{"blockCode":19602,"createTime":"13:33","text":"挖矿中..."}]
     * dig_count : 38 挖矿人数
     */

    private int dig_count;
    private List<BlocksBean> blocks;
    private List<BlocksBean> list;

    public List<BlocksBean> getList() {
        return list;
    }

    public void setList(List<BlocksBean> list) {
        this.list = list;
    }

    public SocketBlockList() {
    }

    public String getTodayDigAmount() {
        return todayDigAmount;
    }

    public void setTodayDigAmount(String todayDigAmount) {
        this.todayDigAmount = todayDigAmount;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public void setDigAmount(int digAmount) {
        this.digAmount = digAmount;
    }

    public void setBlock(BlockBean block) {
        this.block = block;
    }

    public int getRemain() {
        return remain;
    }

    public int getDigAmount() {
        return digAmount;
    }

    public BlockBean getBlock() {
        return block;
    }

    public void setDig_count(int dig_count) {
        this.dig_count = dig_count;
    }

    public void setBlocks(List<BlocksBean> blocks) {
        this.blocks = blocks;
    }

    public int getDig_count() {
        return dig_count;
    }

    public List<BlocksBean> getBlocks() {
        return blocks;
    }

    @Override
    public String toString() {
        return "SocketBlockList{" +
                "remain=" + remain +
                ", digAmount=" + digAmount +
                ", todayDigAmount='" + todayDigAmount + '\'' +
                ", block=" + block +
                ", dig_count=" + dig_count +
                ", blocks=" + blocks +
                ", list=" + list +
                '}';
    }

    public static class BlocksBean {
        /**
         * blockCode : 19599
         * createTime : 13:33
         * text : 已出矿
         */

        private int blockCode;
        private String createTime;
        private String text;

        public BlocksBean() {
        }

        public BlocksBean(int blockCode, String createTime, String text) {
            this.blockCode = blockCode;
            this.createTime = createTime;
            this.text = text;
        }

        public void setBlockCode(int blockCode) {
            this.blockCode = blockCode;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getBlockCode() {
            return blockCode;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getText() {
            return text;
        }
    }

}
