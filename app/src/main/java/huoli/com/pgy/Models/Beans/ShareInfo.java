package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by YX on 2018/7/10.
 */

public class ShareInfo {

    private List<ListBean> list;
    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * maintitle : 主标题
         * imgpath : http:
         * subtitle : 副标题
         * shareUrl : http:
         * id : 1
         */

        private String maintitle;
        private String imgpath;
        private String subtitle;
        private String shareUrl;
        private int id;
        public String getMaintitle() {
            return maintitle;
        }

        public void setMaintitle(String maintitle) {
            this.maintitle = maintitle;
        }

        public String getImgpath() {
            return imgpath;
        }

        public void setImgpath(String imgpath) {
            this.imgpath = imgpath;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
