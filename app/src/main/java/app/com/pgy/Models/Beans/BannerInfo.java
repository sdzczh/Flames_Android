package app.com.pgy.Models.Beans;

/**
 * Created by YX on 2018/7/26.
 */

public class BannerInfo {
    /**
     * address : wallet
     * imgpath :
     * title :
     * type : 1
     */

    private String address;
    private String imgpath;
    private String title;
    private int type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
