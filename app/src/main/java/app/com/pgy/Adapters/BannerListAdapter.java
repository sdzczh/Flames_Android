package app.com.pgy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Models.Beans.BannerInfo;
import app.com.pgy.R;
import app.com.pgy.Utils.BannerIntentUtils;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Widgets.RatioImageView;

/**
 * Create by Android on 2019/10/28 0028
 */
public class BannerListAdapter extends RecyclerArrayAdapter<BannerInfo> {
    Context mContext;

    public BannerListAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BannerListViewHolder(parent);
    }

    class BannerListViewHolder extends BaseViewHolder<BannerInfo>{
        RatioImageView riv;
        TextView tvTitle;
        public BannerListViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_banner_list_view);
            riv = $(R.id.iv_banner_list);
            tvTitle = $(R.id.tv_banner_title);
        }

        @Override
        public void setData(BannerInfo data) {
            tvTitle.setText(data.getTitle());
            ImageLoaderUtils.display(getContext(),riv,data.getImgpath());
        }
    }
}
