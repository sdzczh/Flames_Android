package app.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/5/11.
 */

public class MainDialogBean {
    String imgUrl;
    String linkUrl;
    String linkTitle;
    boolean onoff;


    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setOnoff(boolean onoff) {
        this.onoff = onoff;
    }

    public boolean isOnoff() {
        return onoff;
    }
}
