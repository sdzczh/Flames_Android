package app.com.pgy.Models.Beans;

import java.io.Serializable;

/**
 * @author xuqingzhong
 * 上传图片返回值
 */

public class StringBean implements Serializable{

    private String imgPath;

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    @Override
    public String toString() {
        return "StringBean{" +
                "imgPath='" + imgPath + '\'' +
                '}';
    }
}
