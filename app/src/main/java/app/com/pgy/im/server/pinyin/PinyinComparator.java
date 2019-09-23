package app.com.pgy.im.server.pinyin;


import java.util.Comparator;

import app.com.pgy.im.server.response.GetUserInfosResponse;


/**
 *
 * @author
 *
 */
public class PinyinComparator implements Comparator<GetUserInfosResponse.ResultEntity> {


    public static PinyinComparator instance = null;

    public static PinyinComparator getInstance() {
        if (instance == null) {
            instance = new PinyinComparator();
        }
        return instance;
    }

    @Override
    public int compare(GetUserInfosResponse.ResultEntity o1, GetUserInfosResponse.ResultEntity o2) {
        if (o1.getLetters().equals("@")
                || o2.getLetters().equals("#")) {
            return -1;
        } else if (o1.getLetters().equals("#")
                || o2.getLetters().equals("@")) {
            return 1;
        } else {
            return o1.getLetters().compareTo(o2.getLetters());
        }
    }

}
