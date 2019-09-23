package app.com.pgy.Models.Beans;

import java.io.Serializable;

/**
 * @author xuqingzhong
 * 版本号
 */

public class version implements Serializable{

    /**
     * apkHash : 14214141124141eggegr
     * apkSize : 12
     * apkUrl : http://download.pgy.apk
     * updateFlag : true
     * updateType : 0
     * updateVersion : 2
     * content":更新内容
     */

    private String apkHash;
    private int apkSize;
    private String apkUrl;
    private boolean updateFlag;
    private int updateType;
    private int updateVersion;
    private String content;

    public boolean isUpdateFlag() {
        return updateFlag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setApkHash(String apkHash) {
        this.apkHash = apkHash;
    }

    public void setApkSize(int apkSize) {
        this.apkSize = apkSize;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public void setUpdateFlag(boolean updateFlag) {
        this.updateFlag = updateFlag;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public void setUpdateVersion(int updateVersion) {
        this.updateVersion = updateVersion;
    }

    public String getApkHash() {
        return apkHash;
    }

    public int getApkSize() {
        return apkSize;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public boolean getUpdateFlag() {
        return updateFlag;
    }

    public int getUpdateType() {
        return updateType;
    }

    public int getUpdateVersion() {
        return updateVersion;
    }

}
