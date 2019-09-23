package app.com.pgy.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.ImSystemMessage;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;

/**
 * 系统消息数据适配器
 *
 * @author xuqingzhong
 */
public class SystemMessageListAdapter extends RecyclerArrayAdapter<ImSystemMessage.ListBean> {
    public SystemMessageListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeViewHolder(parent);
    }

    class NoticeViewHolder extends BaseViewHolder<ImSystemMessage.ListBean> {
        private TextView itemImSystemMessageTitle;
        private View itemImSystemMessagePoint;
        private TextView itemImSystemMessageTime;
        private ImageView itemImSystemMessageImg;
        private TextView itemImSystemMessageDesc;

        NoticeViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_im_systemmessage);
            itemImSystemMessageTitle = $(R.id.item_im_systemMessage_title);
            itemImSystemMessagePoint = $(R.id.item_im_systemMessage_point);
            itemImSystemMessageTime = $(R.id.item_im_systemMessage_time);
            itemImSystemMessageImg = $(R.id.item_im_systemMessage_img);
            itemImSystemMessageDesc = $(R.id.item_im_systemMessage_desc);
        }

        @Override
        public void setData(ImSystemMessage.ListBean message) {
            itemImSystemMessageTitle.setText(message.getTitle());
            switch (message.getState()){
                default:
                    break;
                    /*未读*/
                case 0:
                    itemImSystemMessagePoint.setVisibility(View.VISIBLE);
                    break;
                    /*已读*/
                case 1:
                    itemImSystemMessagePoint.setVisibility(View.GONE);
                    break;

            }
            itemImSystemMessageTime.setText(message.getCreateTime());
            String imgPath = message.getImgPath();
            if (!TextUtils.isEmpty(imgPath)) {
                itemImSystemMessageImg.setVisibility(View.VISIBLE);
                ImageLoaderUtils.display(getContext(), itemImSystemMessageImg, imgPath, R.color.txt_all9);
            }else{
                itemImSystemMessageImg.setVisibility(View.GONE);
            }
            itemImSystemMessageDesc.setText(message.getRoundup());
        }
    }
}