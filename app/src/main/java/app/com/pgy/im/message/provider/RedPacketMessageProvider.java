package app.com.pgy.im.message.provider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.im.UI.RedPackageDetailActivity;
import app.com.pgy.im.message.RedPacketMessage;
import app.com.pgy.im.message.RedPacketNotificationMessage;
import app.com.pgy.im.server.response.GetRedPacketStateResponse;
import app.com.pgy.im.widget.OpenRedPackageDialog;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 红包消息提供者，展示消息
 * Created by YX on 2018/5/18.
 */

@ProviderTag(messageContent = RedPacketMessage.class,showReadState = false)
public class RedPacketMessageProvider extends IContainerItemProvider.MessageProvider<RedPacketMessage> {
    private static final String TAG = "RedPacketMessageProvide";
    private Context context;
    private static class ViewHolder{
        RelativeLayout rl_top;
        TextView tv_title,tv_content,tv_bottom;
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_red_packet_message,null);
        RedPacketMessageProvider.ViewHolder holder = new RedPacketMessageProvider.ViewHolder();
        holder.rl_top = view.findViewById(R.id.rl_top);
        holder.tv_title = view.findViewById(R.id.tv_title);
        holder.tv_content = view.findViewById(R.id.tv_content);
        holder.tv_bottom = view.findViewById(R.id.tv_bottom);
        view.setTag(holder);
        return view;
    }

    @Override
    public Spannable getContentSummary(RedPacketMessage redPacketMessage) {
        /*Preferences.init(context);
        User localUser = Preferences.getLocalUser();
        if (redPacketMessage == null || TextUtils.isEmpty(redPacketMessage.getUid()) ||localUser == null || TextUtils.isEmpty(localUser.getPhone())){
            return null;
        }

        LogUtils.w(TAG,"first:"+redPacketMessage.toString()+",localUserName:"+localUser.getPhone());
        if (redPacketMessage.getUid().equals(localUser.getPhone())){
            return new SpannableString("发出一个红包");
        }else {
            return new SpannableString("收到一个红包");
        }*/
        return new SpannableString("[红包]");
    }

    @Override
    public Spannable getContentSummary(Context context, RedPacketMessage data) {
        /*Preferences.init(context);
        User localUser = Preferences.getLocalUser();
        if (data == null || TextUtils.isEmpty(data.getUid()) ||localUser == null || TextUtils.isEmpty(localUser.getPhone())){
            return null;
        }
        LogUtils.w(TAG,"second:"+data.toString()+",localUserName:"+localUser.getPhone());
        if (data.getUid().equals(localUser.getPhone())){
            return new SpannableString("发出一个红包");
        }else {
            return new SpannableString("收到一个红包");
        }*/
        return new SpannableString("[红包]");
    }

    @Override
    public void onItemClick(final View view, int i, final RedPacketMessage redPacketMessage, final UIMessage uiMessage) {
        context = view.getContext();
        Map<String,Object> map = new HashMap<>();
        map.put("id", redPacketMessage.getPacketId());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("syetemType",SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getRedPacketDetail(Preferences.getAccessToken(), map, new getBeanCallback<GetRedPacketStateResponse>() {
            @Override
            public void onSuccess(GetRedPacketStateResponse redPacketStateResponse) {
                 if (redPacketStateResponse.getState()==2){
                     Toast.makeText(context,"红包已超时",Toast.LENGTH_SHORT).show();
                 }else if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE && redPacketStateResponse.getState()==0){
                        /*如果点击别人发的红包，并且未领取，弹出红包对话框*/
                     final OpenRedPackageDialog.Builder builder = new OpenRedPackageDialog.Builder(context);
                    builder.setRedPacket(redPacketStateResponse);
                    builder.setCancelable(true);
                    builder.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                    /*用户接收红包*/
                            openRedPacket(view,redPacketMessage,uiMessage);
                        }
                    });
                    builder.create().show();
                }else{
            /*如果点击自己发的红包获取别人发的红包已领取，则跳转红包详情*/
                    Intent intent2RedPacketDetail = new Intent(context,RedPackageDetailActivity.class);
                    intent2RedPacketDetail.putExtra("isReceive",uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE);
                    intent2RedPacketDetail.putExtra("detail",redPacketStateResponse);
                    context.startActivity(intent2RedPacketDetail);
                }
            }

            @Override
            public void onError(int errorCode, String reason) {

            }
        });
    }

    /**点击拆开红包*/
    private void openRedPacket(View view, final RedPacketMessage redPacketMessage, final UIMessage uiMessage) {
        final RedPacketMessageProvider.ViewHolder holder = (ViewHolder) view.getTag();
        Map<String,Object> map = new HashMap<>();
        map.put("id", redPacketMessage.getPacketId());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("syetemType",SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.receiveRedPacket(Preferences.getAccessToken(), map, new getBeanCallback<GetRedPacketStateResponse>() {
            @Override
            public void onSuccess(GetRedPacketStateResponse redPacketStateResponse) {
                /*领取完红包，跳转到详情*/
                Intent intent2RedPacketDetail = new Intent(context,RedPackageDetailActivity.class);
                intent2RedPacketDetail.putExtra("isReceive",true);
                intent2RedPacketDetail.putExtra("detail",redPacketStateResponse);
                context.startActivity(intent2RedPacketDetail);
                //消息状态改变
                uiMessage.getReceivedStatus().setMultipleReceive();//修改内存中消息状态
                //将消息状态写入本地数据库
                RongIM.getInstance().setMessageReceivedStatus(uiMessage.getMessageId(), uiMessage.getReceivedStatus(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        setLayout(holder,uiMessage);
                        RedPacketNotificationMessage redPacketNotificationMessage = RedPacketNotificationMessage.obain(RongIM.getInstance().getCurrentUserId(), Preferences.getLocalUser().getName(),redPacketMessage.getUid(),redPacketMessage.getUserName());
                        Message message = Message.obtain(uiMessage.getSenderUserId(),uiMessage.getConversationType(),redPacketNotificationMessage);
                        RongIM.getInstance().sendMessage(message,"","", (IRongCallback.ISendMessageCallback) null);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }

            @Override
            public void onError(int errorCode, String reason) {

            }
        });
    }


    @Override
    public void bindView(View view, int i, RedPacketMessage redPacketMessage, UIMessage uiMessage) {
        RedPacketMessageProvider.ViewHolder holder = (ViewHolder) view.getTag();
        if (uiMessage.getReceivedStatus().isMultipleReceive()){
            holder.rl_top.setBackgroundResource(R.drawable.rc_redpacket_top_read);
        }else {
            holder.rl_top.setBackgroundResource(R.drawable.rc_redpacket_top_unread);

        }
        TextView tv_title = holder.tv_title;
        TextView tv_bottom = holder.tv_bottom;
        String title = redPacketMessage.getContent();
        tv_title.setText(title);
        if (redPacketMessage.getPacketType() == 0){
            tv_bottom.setText("COIN红包");
        }else {
            tv_bottom.setText("COIN拼手气红包");
        }
    }

    private void setLayout(ViewHolder holder, UIMessage uiMessage){
        if (uiMessage.getReceivedStatus().isMultipleReceive()){
            holder.rl_top.setBackgroundResource(R.drawable.rc_redpacket_top_read);
        }else {
            holder.rl_top.setBackgroundResource(R.drawable.rc_redpacket_top_unread);
        }
    }
}
