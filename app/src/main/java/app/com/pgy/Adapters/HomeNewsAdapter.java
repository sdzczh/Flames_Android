package app.com.pgy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Constants.Constants;
import app.com.pgy.Models.Beans.HomeNewBean;
import app.com.pgy.R;
import app.com.pgy.Utils.TimeUtils;

/**
 * Create by Android on 2019/10/10 0010
 */
public class HomeNewsAdapter extends RecyclerArrayAdapter<HomeNewBean> {
    Context mContext;
    public HomeNewsAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeNewsViewHolder(parent);
    }

    class HomeNewsViewHolder extends BaseViewHolder<HomeNewBean> implements View.OnClickListener {
        TextView tv_title,tv_time;
        LinearLayout llBg;
        public HomeNewsViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_home_news_view);
            tv_title = $(R.id.tv_item_home_news_title);
            tv_time = $(R.id.tv_item_home_news_time);
            llBg = $(R.id.ll_bg);
            llBg.setOnClickListener(this);
        }

        @Override
        public void setData(HomeNewBean data) {
            tv_title.setText(data.getTitle());
            tv_time.setText(TimeUtils.dateToString(data.getCreatetime()));
        }

        @Override
        public void onClick(View v) {
            HomeNewBean item = getItem(getAdapterPosition());
            Intent intent = new Intent(mContext, WebDetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("url", Constants.HTTP_URL+"//web/news/id/"+item.getId()+".action");
            mContext.startActivity(intent);
        }
    }
}
