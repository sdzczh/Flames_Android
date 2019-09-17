package huoli.com.pgy.Models.Beans;

import java.io.Serializable;

/**
 * @author xuqingzhong
 * 验证码
 */

public class verficationCode implements Serializable{

    /**
     * codeId : 验证码id
     */

    private String codeId;

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeId() {
        return codeId;
    }

}
