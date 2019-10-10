package app.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.HomeNewBean;
import app.com.pgy.R;
import app.com.pgy.Utils.TimeUtils;

/**
 * Create by Android on 2019/10/10 0010
 */
public class HomeNewsAdapter extends RecyclerArrayAdapter<HomeNewBean> {
    public HomeNewsAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeNewsViewHolder(parent);
    }

    class HomeNewsViewHolder extends BaseViewHolder<HomeNewBean>{
        TextView tv_title,tv_time;
        public HomeNewsViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_home_news_view);
            tv_title = $(R.id.tv_item_home_news_title);
            tv_time = $(R.id.tv_item_home_news_time);
        }

        @Override
        public void setData(HomeNewBean data) {
            tv_title.setText(data.getTitle());
            tv_time.setText(TimeUtils.dateToString(data.getCreatetime()));
        }
    }
}
