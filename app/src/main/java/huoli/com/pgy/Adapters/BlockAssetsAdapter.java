package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.Models.Beans.MyWallet;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Utils.ToolsUtils;

/**
 * Created by YX on 2018/7/16.
 */

public class BlockAssetsAdapter extends RecyclerArrayAdapter<MyWallet.ListBean> {

    public BlockAssetsAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyBlockAssetsViewHolder(parent);
    }


    class MyBlockAssetsViewHolder extends BaseViewHolder<MyWallet.ListBean>{
        LinearLayout ll_bg;
        RoundedImageView riv_icon;
        TextView tv_cnName;
        TextView tv_total;
        TextView tv_enName;
        TextView tv_totalCny;
        public MyBlockAssetsViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_my_block_assets_list_view);
            ll_bg = $(R.id.ll_item_my_block_assets_bg);
            riv_icon = $(R.id.riv_item_my_block_assets_coinicon);
            tv_cnName = $(R.id.tv_item_my_block_assets_coinzhname);
            tv_total = $(R.id.tv_item_my_block_assets_cointotal);
            tv_enName = $(R.id.tv_item_my_block_assets_coinenname);
            tv_totalCny = $(R.id.tv_item_my_block_assets_totalcny);
        }

        @Override
        public void setData(MyWallet.ListBean data) {
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(data.getCoinType());
            tv_enName.setText(coinInfo.getCoinname());
            tv_cnName.setText(coinInfo.getCnname());
            ImageLoaderUtils.displayCircle(getContext(),riv_icon,coinInfo.getImgurl());
            /*可用余额*/
            tv_total.setText(data.getTotalBalance());
            /*折合人民币*/
            tv_totalCny.setText("" + data.getTotalOfCny());
        }

    }
}
