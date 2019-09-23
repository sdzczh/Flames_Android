package app.com.pgy.Models.Beans;

import java.util.List;

/**
 * 系统通知Bean 实现序列号 可在intent中传递
 * @author xuqingzhong
 */

public class ImSystemMessage {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1
         * state : 0
         * createTime : 2018/06/08 11: 44
         * title : 标题
         * roundup : 摘要
         * url : http: //test.huolicoin.com: 8080/coinorderapp/notice/system/1.action
         * imgPath : http: //img.huolicoin.com/fai/eTr/pictureTr/201802040833358683.jpg
         */

        private int id;
        private int state;
        private String createTime;
        private String title;
        private String roundup;
        private String url;
        private String imgPath;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRoundup() {
            return roundup;
        }

        public void setRoundup(String roundup) {
            this.roundup = roundup;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }
    }
}