package huoli.com.pgy.im.server.response;

import java.util.List;

/**
 * Created by AMing on 16/5/23.
 * Company RongCloud
 */
public class GetUserInfosResponse {
    private List<ResultEntity> list;

    public List<ResultEntity> getList() {
        return list;
    }

    public void setList(List<ResultEntity> list) {
        this.list = list;
    }

    public static class ResultEntity {
        private String phone;
        private String name;
        private String headPath;
        private String nameSpelling;
        private String letters;

        public String getLetters() {
            return letters;
        }

        public void setLetters(String letters) {
            this.letters = letters;
        }

        public String getNameSpelling() {
            return nameSpelling;
        }

        public void setNameSpelling(String nameSpelling) {
            this.nameSpelling = nameSpelling;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadPath() {
            return headPath;
        }

        public void setHeadPath(String headPath) {
            this.headPath = headPath;
        }
    }
}
