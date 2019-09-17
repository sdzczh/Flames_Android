package huoli.com.pgy.im.message;

import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * 红包消息类
 * Created by YX on 2018/5/15.
 */

@MessageTag(value = "Cus:RedMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class RedPacketMessage extends MessageContent {
    private final static String TAG = "RedPacketMessage";

    private String packetId;//红包id
    private int packetType;//红包类型 0，个人红包 1，群红包
    private String uid;//发红包者的id
    private String userName;//发红包者的名字
    private String content;//发红包的时，输入的内容
    protected String extra;


    /**
     * 将本地消息对象序列化为消息数据。
     *
     * @return 消息数据。
     */
    @Override
    public byte[] encode() {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("packetId",getPacketId());
            jsonObj.put("packetType",getPacketType());
            jsonObj.put("content", getContent());
            jsonObj.put("uid", getUid());
            jsonObj.put("name", getUserName());
            if (!TextUtils.isEmpty(getExtra()))
                jsonObj.put("extra", getExtra());

            if (getJSONUserInfo() != null)
                jsonObj.putOpt("user", getJSONUserInfo());

            if (getJsonMentionInfo() != null) {
                jsonObj.putOpt("mentionedInfo", getJsonMentionInfo());
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSONException " + e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RedPacketMessage(){

    }
//    public static RedPacketMessage obain(String packetId,int packetType,String content){
//        RedPacketMessage model = new RedPacketMessage();
//        model.setPacketId(packetId);
//        model.setPacketType(packetType);
//        model.setContent(content);
//        return model;
//    }
    public static RedPacketMessage obain(String packetId, int packetType, String content, String uid, String userName){
        RedPacketMessage model = new RedPacketMessage();
        model.setPacketId(packetId);
        model.setPacketType(packetType);
        model.setUid(uid);
        model.setContent(content);
        model.setUserName(userName);
        return model;
    }
    public static RedPacketMessage obain(String packetId, int packetType, String uid, String userName, String content, String extra){
        RedPacketMessage model = new RedPacketMessage();
        model.setPacketId(packetId);
        model.setPacketType(packetType);
        model.setUid(uid);
        model.setUserName(userName);
        model.setContent(content);
        model.setExtra(extra);
        return model;
    }

    public RedPacketMessage(byte[] data){
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(jsonStr)){
            return;
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("packetId"))
                setPacketId(jsonObj.optString("packetId"));

            if (jsonObj.has("packetType"))
                setPacketType(jsonObj.optInt("packetType"));

            if (jsonObj.has("uid"))
                setUid(jsonObj.optString("uid"));

            if (jsonObj.has("name"))
                setUserName(jsonObj.optString("name"));

            if (jsonObj.has("content"))
                setContent(jsonObj.optString("content"));

            if (jsonObj.has("extra"))
                setExtra(jsonObj.optString("extra"));

            if (jsonObj.has("user")) {
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }

            if (jsonObj.has("mentionedInfo")) {
                setMentionedInfo(parseJsonToMentionInfo(jsonObj.getJSONObject("mentionedInfo")));
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSONException " + e.getMessage());
        }
    }
    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        ParcelUtils.writeToParcel(parcel, getExtra());
        ParcelUtils.writeToParcel(parcel, getContent());
        ParcelUtils.writeToParcel(parcel, getUserName());
        ParcelUtils.writeToParcel(parcel, getUid());
        ParcelUtils.writeToParcel(parcel,getPacketType());
        ParcelUtils.writeToParcel(parcel,getPacketId());
        ParcelUtils.writeToParcel(parcel, getUserInfo());
        ParcelUtils.writeToParcel(parcel, getMentionedInfo());
    }

    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public RedPacketMessage(Parcel in) {
        setExtra(ParcelUtils.readFromParcel(in));
        setContent(ParcelUtils.readFromParcel(in));
        setUserName(ParcelUtils.readFromParcel(in));
        setUid(ParcelUtils.readFromParcel(in));
        setPacketType(ParcelUtils.readIntFromParcel(in));
        setPacketId(ParcelUtils.readFromParcel(in));
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
        setMentionedInfo(ParcelUtils.readFromParcel(in, MentionedInfo.class));
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<RedPacketMessage> CREATOR = new Creator<RedPacketMessage>() {

        @Override
        public RedPacketMessage createFromParcel(Parcel source) {
            return new RedPacketMessage(source);
        }

        @Override
        public RedPacketMessage[] newArray(int size) {
            return new RedPacketMessage[size];
        }
    };

    public void setPacketId(String packetId){
        this.packetId = packetId;
    }

    public String getPacketId(){
        return packetId;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public int getPacketType() {
        return packetType;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }

    @Override
    public String toString() {
        return "RedPacketMessage{" +
                "packetId='" + packetId + '\'' +
                ", packetType=" + packetType +
                ", uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
