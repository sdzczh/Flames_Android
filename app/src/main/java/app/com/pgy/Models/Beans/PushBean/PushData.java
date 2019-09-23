package app.com.pgy.Models.Beans.PushBean;

import java.io.Serializable;
import java.util.Objects;

/**
 * 场景切换累
 * @author xuqingzhong
 */

public class PushData  implements Serializable{
    private String scene;
    private String c1;
    private String c2;
    private String gear;


    public PushData() {
    }

    public PushData(String scene) {
        this.scene = scene;
    }

    public PushData(String scene, String c1, String c2, String gear) {
        this.scene = scene;
        this.c1 = c1;
        this.c2 = c2;
        this.gear = gear;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PushData pushData = (PushData) obj;
        return Objects.equals(scene,pushData.scene)&&Objects.equals(c1,pushData.c1) && Objects.equals(c2,pushData.c2) && Objects.equals(gear,pushData.gear);
    }

    @Override
    public String toString() {
        return "PushData{" +
                "scene='" + scene + '\'' +
                ", c1='" + c1 + '\'' +
                ", c2='" + c2 + '\'' +
                ", gear='" + gear + '\'' +
                '}';
    }
}
