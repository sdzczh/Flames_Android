package app.com.pgy.Models.Beans;

import java.io.Serializable;

/**
 * @author xuqingzhong
 * 实名认证返回值
 */

public class StringNameBean implements Serializable{


    /**
     * name : 张三
     */

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
