package app.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.OdinMyFollowBean;
import app.com.pgy.R;
import app.com.pgy.Utils.TimeUtils;

/**
 * *  @Description:描述
 * *  @Author: EDZ
 * *  @CreateDate: 2019/7/23 8:52
 */
public class OdinMyFollowListAdapter extends RecyclerArrayAdapter<OdinMyFollowBean> {
    public OdinMyFollowListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyOdinMyFollowViewHolder(parent);
    }

    class MyOdinMyFollowViewHolder extends BaseViewHolder<OdinMyFollowBean>{

        TextView tv_tel,tv_num,tv_time;
        public MyOdinMyFollowViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_invitation_my_follow);
            tv_tel = $(R.id.tv_item_my_follow_tel);
            tv_num = $(R.id.tv_item_my_follow_num);
            tv_time = $(R.id.tv_item_my_follow_time);
        }

        @Override
        public void setData(OdinMyFollowBean data) {
            if (data == null){
                return;
            }

            tv_tel.setText(data.getPhone());
            tv_num.setText(data.getAmount()+"ECN");
            tv_time.setText(TimeUtils.dateToString(data.getCreate_time(),"yyyy-MM-dd HH:mm:ss"));

        }
    }
}
