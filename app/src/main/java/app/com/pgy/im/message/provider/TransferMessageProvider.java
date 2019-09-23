package app.com.pgy.im.message.provider;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.im.UI.TransferDetailActivity;
import app.com.pgy.im.message.TransferMeassage;
import app.com.pgy.im.server.response.GetTransferStateResponse;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/5/21.
 */

@ProviderTag(messageContent = TransferMeassage.class,showReadState = false)
public class TransferMessageProvider extends IContainerItemProvider.MessageProvider<TransferMeassage> {
    private static final String TAG = "TransferMessageProvider";
    private Conversation.ConversationType conversationType;
    private Context context;
    private static class ViewHolder{
        TextView tv_title,tv_content;
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_transfer_message_view,null);
        TransferMessageProvider.ViewHolder holder = new TransferMessageProvider.ViewHolder();
        holder.tv_title = view.findViewById(R.id.tv_title);
        holder.tv_content = view.findViewById(R.id.tv_content);
        view.setTag(holder);
        return view;
    }

    @Override
    public Spannable getContentSummary(TransferMeassage transferMeassage) {
        /*Preferences.init(context);
        User localUser = Preferences.getLocalUser();
        if (transferMeassage == null || TextUtils.isEmpty(transferMeassage.getUid()) ||localUser == null || TextUtils.isEmpty(localUser.getPhone())){
            return null;
        }
        LogUtils.w(TAG,"local1:"+localUser.getPhone()+","+transferMeassage.toString());
        if (transferMeassage.getUid().equals(localUser.getPhone())){
            return new SpannableString("收到转账");
        }else {
            return new SpannableString("转账成功");
        }*/
        return new SpannableString("[转账]");
    }

    @Override
    public Spannable getContentSummary(Context context, TransferMeassage data) {
        /*Preferences.init(context);
        User localUser = Preferences.getLocalUser();
        LogUtils.w(TAG,"local2:"+localUser.getPhone()+","+data.toString());
        if (data == null || TextUtils.isEmpty(data.getUid()) ||localUser == null || TextUtils.isEmpty(localUser.getPhone())){
            return null;
        }
        if (data.getUid().equals(localUser.getPhone())){
            return new SpannableString("收到转账");
        }else {
            return new SpannableString("转账成功");
        }*/
        return new SpannableString("[转账]");
    }

    @Override
    public void onItemClick(View view, int i, final TransferMeassage transferMeassage, final UIMessage uiMessage) {
        context = view.getContext();
        Map<String,Object> map = new HashMap<>();
        map.put("id", transferMeassage.getTranId());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("syetemType",SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getTransferDetail(Preferences.getAccessToken(), map, new getBeanCallback<GetTransferStateResponse>() {
            @Override
            public void onSuccess(GetTransferStateResponse transferStateResponse) {
                Intent intent2Detail = new Intent(context, TransferDetailActivity.class);
                /*true代表是别人发给自己的*/
                intent2Detail.putExtra("isReceive",uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE);
                intent2Detail.putExtra("detail",transferStateResponse);
                context.startActivity(intent2Detail);
            }

            @Override
            public void onError(int errorCode, String reason) {

            }
        });
    }

    @Override
    public void bindView(View view, int i, TransferMeassage transferMeassage, UIMessage uiMessage) {
        TransferMessageProvider.ViewHolder holder = (TransferMessageProvider.ViewHolder) view.getTag();

        TextView tv_title = holder.tv_title;
        TextView tv_content = holder.tv_content;

        if (!TextUtils.isEmpty(transferMeassage.getContent())){
            tv_title.setText(transferMeassage.getContent());
        }else {
            if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND){
                tv_title.setText("转账给"+transferMeassage.getToUseName());
            }else {
                tv_title.setText("转账给你");
            }
        }
        String price = transferMeassage.getPrice();
        if (TextUtils.isEmpty(price)){
            price = "0";
        }
        tv_content.setText(price+transferMeassage.getCoinType());

    }
}
