package app.com.pgy.Models.Beans;

import java.util.Map;

/**
 * Created by YX on 2018/7/9.
 */

public class GroupsInfo {
    private Map<Integer,Group> list;

    public void setList(Map<Integer, Group> list) {
        this.list = list;
    }

    public Map<Integer, Group> getList() {
        return list;
    }

    public static class Group{
        /**
         * createtime : 1531903717000
         * id : 2
         * keyval : 111111 // 信息   群号 公众号
         * type : 1//0 QQ群 1微信号 2公众号 3微博
         * updatetime : 1531903717000
         * imgpath : http://img.huolicoin.com/fai/eTr/pictureTr/201803270616527284.jpg  //二维码地址
         */
        private int id;
        private String keyval;
        private int type;
        private String imgpath;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKeyval() {
            return keyval;
        }

        public void setKeyval(String keyval) {
            this.keyval = keyval;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
        public String getImgpath() {
            return imgpath;
        }

        public void setImgpath(String imgpath) {
            this.imgpath = imgpath;
        }
    }

}
