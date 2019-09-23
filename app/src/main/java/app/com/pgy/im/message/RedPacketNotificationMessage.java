package app.com.pgy.im.message;

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
 * 红包被拆后，提示消息类
 * Created by YX on 2018/5/19.
 */

@MessageTag(value = "Cus:RedNtf", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class RedPacketNotificationMessage extends MessageContent {
    private final static String TAG = "RedPacketMessage";

    private String sendUid;//发送此消息的用户id
    private String sendName;//发送此消息的用户姓名
    private String redFromUid;//红包发送者id
    private String redFromName;//红包发送者名字
    protected String extra;

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("sendUid",getSendUid());
            jsonObj.put("sendName",getSendName());
            jsonObj.put("redFromUid",getRedFromUid());
            jsonObj.put("redFromName",getRedFromName());

            if (!TextUtils.isEmpty(getExtra()))
                jsonObj.put("extra", getExtra());

            if (getJSONUserInfo() != null)
                jsonObj.putOpt("user", getJSONUserInfo());

            if (getJsonMentionInfo() != null) {
                jsonObj.putOpt("mentionedInfo", getJsonMentionInfo());
            }
        }catch (JSONException e){
            Log.e(TAG, "JSONException " + e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RedPacketNotificationMessage(){

    }

    public static RedPacketNotificationMessage obain(String sendUid, String sendName, String redFromUid, String redFromName){
        RedPacketNotificationMessage model = new RedPacketNotificationMessage();
        model.setSendUid(sendUid);
        model.setSendName(sendName);
        model.setRedFromUid(redFromUid);
        model.setRedFromName(redFromName);
        return model;
    }

    public static RedPacketNotificationMessage obain(String sendUid, String sendName, String redFromUid, String redFromName, String extra){
        RedPacketNotificationMessage model = new RedPacketNotificationMessage();
        model.setSendUid(sendUid);
        model.setSendName(sendName);
        model.setRedFromUid(redFromUid);
        model.setRedFromName(redFromName);
        model.setExtra(extra);
        return model;
    }

    public RedPacketNotificationMessage(byte[] data){
        String jsonStr = null;
        try {
            jsonStr = new String(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(jsonStr)){
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.has("sendUid")){
                setSendUid(jsonObject.optString("sendUid"));
            }
            if (jsonObject.has("sendName")){
                setSendName(jsonObject.optString("sendName"));
            }
            if (jsonObject.has("redFromUid")){
                setRedFromUid(jsonObject.optString("redFromUid"));
            }
            if (jsonObject.has("redFromName")){
                setRedFromName(jsonObject.optString("redFromName"));
            }
            if (jsonObject.has("extra"))
                setExtra(jsonObject.optString("extra"));

            if (jsonObject.has("user")) {
                setUserInfo(parseJsonToUserInfo(jsonObject.getJSONObject("user")));
            }
            if (jsonObject.has("mentionedInfo")){
                setMentionedInfo(parseJsonToMentionInfo(jsonObject.getJSONObject("mentionedInfo")));

            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        ParcelUtils.writeToParcel(parcel, getRedFromName());
        ParcelUtils.writeToParcel(parcel, getRedFromUid());
        ParcelUtils.writeToParcel(parcel, getSendName());
        ParcelUtils.writeToParcel(parcel,getSendUid());
        ParcelUtils.writeToParcel(parcel, getUserInfo());
        ParcelUtils.writeToParcel(parcel, getMentionedInfo());
    }

    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public RedPacketNotificationMessage(Parcel in) {
        setExtra(ParcelUtils.readFromParcel(in));
        setRedFromName(ParcelUtils.readFromParcel(in));
        setRedFromUid(ParcelUtils.readFromParcel(in));
        setSendName(ParcelUtils.readFromParcel(in));
        setSendUid(ParcelUtils.readFromParcel(in));
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
        setMentionedInfo(ParcelUtils.readFromParcel(in, MentionedInfo.class));
    }

    public static final Creator<RedPacketNotificationMessage> CREATOR = new Creator<RedPacketNotificationMessage>() {
        @Override
        public RedPacketNotificationMessage createFromParcel(Parcel parcel) {
            return new RedPacketNotificationMessage(parcel);
        }

        @Override
        public RedPacketNotificationMessage[] newArray(int i) {
            return new RedPacketNotificationMessage[i];
        }
    };

    public void setSendUid(String sendUid) {
        this.sendUid = sendUid;
    }

    public String getSendUid() {
        return sendUid;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendName() {
        return sendName;
    }

    public void setRedFromUid(String redFromUid) {
        this.redFromUid = redFromUid;
    }

    public String getRedFromUid() {
        return redFromUid;
    }

    public void setRedFromName(String redFromName) {
        this.redFromName = redFromName;
    }

    public String getRedFromName() {
        return redFromName;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }
}
