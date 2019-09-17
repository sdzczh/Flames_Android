package huoli.com.pgy.Models.Beans;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YX on 2018/4/26.
 */

public class DigPageInfo {

     private List<Integer>coinChg;//升级或降级后新增或减少可挖币种 List
     private int countOnline;//当前用户所在矿场的在线人数 Integer
     private String honorPic;//  当前用户称号图片 String
     private String honorName;//魂师" 称号String
     private int level;//当前用户称号等级 Integer
     private int levelChg;//当前用户比之前升降等级 Integer
     private String mineCoin;// 当前用户所属矿场可挖币种 String
     private String mineName;//  当前用户所在矿场名称 String
     private String minePic;//http://img.huolicoin.com/fai/ine/ImageData/mine/201804240624395136.jpg",  当前用户所在矿场背景 String
     private int soulVal;// 当前用户算力 Integer


    public void setCoinChg(List<Integer> coinChg) {
        this.coinChg = coinChg;
    }

    public List<Integer> getCoinChg() {
        return coinChg;
    }

    public void setCountOnline(int countOnline) {
        this.countOnline = countOnline;
    }

    public int getCountOnline() {
        return countOnline;
    }

    public void setHonorPic(String honorPic) {
        this.honorPic = honorPic;
    }

    public String getHonorPic() {
        return honorPic;
    }

    public void setHonorName(String honorName) {
        this.honorName = honorName;
    }

    public String getHonorName() {
        return honorName;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevelChg(int levelChg) {
        this.levelChg = levelChg;
    }

    public int getLevelChg() {
        return levelChg;
    }

    public void setMineCoin(String mineCoin) {
        this.mineCoin = mineCoin;
    }

    public String getMineCoin() {
        return mineCoin;
    }

    public List<Integer> getMineCoinList(){
        List<Integer> coins = new ArrayList<>();
        if (!TextUtils.isEmpty(mineCoin)){
            String[] split = mineCoin.split(",");
            for (int i = 0;i < split.length;i++){
                try {
                    coins.add(Integer.parseInt(split[i]));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return coins;
    }

    public void setMineName(String mineName) {
        this.mineName = mineName;
    }

    public String getMineName() {
        return mineName;
    }

    public void setMinePic(String minePic) {
        this.minePic = minePic;
    }

    public String getMinePic() {
        return minePic;
    }

    public void setSoulVal(int soulVal) {
        this.soulVal = soulVal;
    }

    public int getSoulVal() {
        return soulVal;
    }
}
