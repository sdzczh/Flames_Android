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
 * Created by YX on 2018/5/21.
 */

@MessageTag(value = "Cus:TranMsg",flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class TransferMeassage extends MessageContent {
    private final static String TAG = "TransferMeassage";

    private String tranId;//转账id
    private String toUseName;//转账给
    private String uid;//转账者的id
    private String  price;//转账金额
    private String coinType;//转账币种名称
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
            jsonObj.put("tranId",getTranId());
            jsonObj.put("toUseName",getToUseName());
            jsonObj.put("uid", getUid());
            jsonObj.put("content", getContent());
            jsonObj.put("price", getPrice());
            jsonObj.put("coinType", getCoinType());
            if (!TextUtils.isEmpty(getExtra())) {
                jsonObj.put("extra", getExtra());
            }
            if (getJSONUserInfo() != null) {
                jsonObj.putOpt("user", getJSONUserInfo());
            }
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

    public TransferMeassage(){

    }

    public static TransferMeassage obain(String tranId, String uid, String toUseName, String price, String coinName){
        TransferMeassage model = new TransferMeassage();
        model.setTranId(tranId);
        model.setToUseName(toUseName);
        model.setUid(uid);
        model.setPrice(price);
        model.setCoinType(coinName);
        return model;
    }
    public static TransferMeassage obain(String tranId, String uid, String toUseName, String price, String coinName, String content, String extra){
        TransferMeassage model = new TransferMeassage();
        model.setTranId(tranId);
        model.setToUseName(toUseName);
        model.setUid(uid);
        model.setPrice(price);
        model.setCoinType(coinName);
        model.setContent(content);
        model.setExtra(extra);
        return model;
    }

    public TransferMeassage(byte[] data){
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

            if (jsonObj.has("tranId")) {
                setTranId(jsonObj.optString("tranId"));
            }
            if (jsonObj.has("uid")) {
                setToUseName(jsonObj.optString("uid"));
            }
            if (jsonObj.has("toUseName")) {
                setToUseName(jsonObj.optString("toUseName"));
            }
            if (jsonObj.has("price")) {
                setPrice(jsonObj.optString("price"));
            }
            if (jsonObj.has("coinType")) {
                setCoinType(jsonObj.optString("coinType"));
            }
            if (jsonObj.has("content")) {
                setContent(jsonObj.optString("content"));
            }
            if (jsonObj.has("extra")) {
                setExtra(jsonObj.optString("extra"));
            }
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
        ParcelUtils.writeToParcel(parcel, getCoinType());
        ParcelUtils.writeToParcel(parcel, getPrice());
        ParcelUtils.writeToParcel(parcel,getToUseName());
        ParcelUtils.writeToParcel(parcel,getUid());
        ParcelUtils.writeToParcel(parcel,getTranId());
        ParcelUtils.writeToParcel(parcel, getUserInfo());
        ParcelUtils.writeToParcel(parcel, getMentionedInfo());
    }

    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public TransferMeassage(Parcel in) {
        setExtra(ParcelUtils.readFromParcel(in));
        setContent(ParcelUtils.readFromParcel(in));
        setCoinType(ParcelUtils.readFromParcel(in));
        setPrice(ParcelUtils.readFromParcel(in));
        setToUseName(ParcelUtils.readFromParcel(in));
        setUid(ParcelUtils.readFromParcel(in));
        setTranId(ParcelUtils.readFromParcel(in));
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
        setMentionedInfo(ParcelUtils.readFromParcel(in, MentionedInfo.class));
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<TransferMeassage> CREATOR = new Creator<TransferMeassage>() {

        @Override
        public TransferMeassage createFromParcel(Parcel source) {
            return new TransferMeassage(source);
        }

        @Override
        public TransferMeassage[] newArray(int size) {
            return new TransferMeassage[size];
        }
    };

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getTranId() {
        return tranId;
    }

    public void setToUseName(String toUseName) {
        this.toUseName = toUseName;
    }

    public String getToUseName() {
        return toUseName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getCoinType() {
        return coinType;
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
        return "TransferMeassage{" +
                "tranId='" + tranId + '\'' +
                ", toUseName='" + toUseName + '\'' +
                ", uid='" + uid + '\'' +
                ", price='" + price + '\'' +
                ", coinType='" + coinType + '\'' +
                ", content='" + content + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
