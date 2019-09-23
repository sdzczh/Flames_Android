package app.com.pgy.im.server.response;

import java.util.List;

/**
 * Created by AMing on 16/1/26.
 * Company RongCloud
 */
public class GetGroupDetailsResponse {


    /**
     * imgUrl : http://img.huolicoin.com/fai/alk/ImageData/talk/201805241314009422.jpg
     * groupId : 123456
     * list : [{"role":0,"phone":"17616553029","headPath":"http://img.huolicoin.com/fai/eTr/pictureTr/201805221229351445.jpg","name":"COIN3029"},{"role":2,"phone":"17753706337","headPath":"http://img.huolicoin.com/fai/337/17753706337/201804200726453718.png","name":"COIN6337"},{"role":2,"phone":"18853725852","headPath":"http://img.huolicoin.com/fai/eTr/pictureTr/201805221229351445.jpg","name":"哎哟喂"},{"role":2,"phone":"13821429167","headPath":"http://img.huolicoin.com/fai/eTr/pictureTr/201805221229351445.jpg","name":"COIN9167"},{"role":2,"phone":"15294566876","headPath":"http://img.huolicoin.com/fai/876/15294566876/201805250234399621.jpg","name":"COIN6876"},{"role":2,"phone":"18868498433","headPath":"http://img.huolicoin.com/fai/eTr/pictureTr/201805221229351445.jpg","name":"COIN8433"},{"role":2,"phone":"13113161294","headPath":"http://img.huolicoin.com/fai/294/13113161294/201805250243272148.jpg","name":"COIN1294"},{"role":2,"phone":"13807672300","headPath":"http://img.huolicoin.com/fai/eTr/pictureTr/201805221229351445.jpg","name":"COIN2300"},{"role":2,"phone":"18280219101","headPath":"http://img.huolicoin.com/fai/101/18280219101/201805250243244083.jpg","name":"dk搞事情"}]
     * num : 184
     * decription : 本群是COIN官方社区交流群，COIN玩家可在本聊天室交流讨论COIN的相关内容，请大家文明交流遵守群规。
     * name : COIN官方社区聊天室
     * isMuted
     */

    private String imgUrl;
    private String groupId;
    private int num;
    private String decription;
    private String name;
    private boolean isMuted;
    private List<ListBean> list;

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * role : 0
         * phone : 17616553029
         * headPath : http://img.huolicoin.com/fai/eTr/pictureTr/201805221229351445.jpg
         * name : COIN3029
         */

        private int role;
        private String phone;
        private String headPath;
        private String name;

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHeadPath() {
            return headPath;
        }

        public void setHeadPath(String headPath) {
            this.headPath = headPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
