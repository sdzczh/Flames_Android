package app.com.pgy.Models.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * 公告类Bean 实现序列号 可在intent中传递
 * @author xuqingzhong
 */

public class Notice{

    /**
     * list : [{"createTime":"12/28 10:01","id":1,"roundup":"dsfsdfsd","title":"test"}]
     */

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean implements Serializable{
        /**
         * createTime : 12/28 10:01
         * id : 1
         * roundup : dsfsdfsd
         * title : test
         * url
         */

        private String createTime;
        private int id;
        private String roundup;
        private String title;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setRoundup(String roundup) {
            this.roundup = roundup;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreateTime() {
            return createTime;
        }

        public int getId() {
            return id;
        }

        public String getRoundup() {
            return roundup;
        }

        public String getTitle() {
            return title;
        }
    }
}