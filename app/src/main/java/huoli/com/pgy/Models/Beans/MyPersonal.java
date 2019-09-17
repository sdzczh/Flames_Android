package huoli.com.pgy.Models.Beans;

import android.text.TextUtils;

/**
 * Created by YX on 2018/7/23.
 */

public class MyPersonal {
    /**
     * sex : f //性别m 代表男性，f 代表女性
     * grade : 魂士 40
     */

    private String sex;
    private String grade;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSexStr(){
        if (TextUtils.isEmpty(sex)){
            return "";
        }

        if (sex.equals("f")){
            return "女";
        }else {
            return "男";
        }
    }
}
