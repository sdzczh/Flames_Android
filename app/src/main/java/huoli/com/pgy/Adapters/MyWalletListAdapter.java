package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.Interfaces.getPositionCallback;
import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.Models.Beans.MyWallet;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Utils.ToolsUtils;

/**
 * 我的钱包适配器
 *
 * @author xuqingzhong
 */
public class MyWalletListAdapter extends RecyclerArrayAdapter<MyWallet.ListBean> {

    private getPositionCallback callback;

    public MyWalletListAdapter(Context context) {
        super(context);
    }

    public void setCallback(getPositionCallback callback) {
        this.callback = callback;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyWalletViewHolder(parent);
    }

    class MyWalletViewHolder extends BaseViewHolder<MyWallet.ListBean> implements View.OnClickListener {
        private LinearLayout ll_coin;
        private ImageView iv_coin_icon;
        private TextView tv_coin_name;
        private TextView tv_total;
        private TextView tv_total_cny;
        private TextView tv_coin_enname;

        MyWalletViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_my_wallet_coin_view);
            ll_coin = $(R.id.ll_item_my_wallet_coin);
            iv_coin_icon = $(R.id.riv_item_my_wallet_coin_img);
            tv_coin_name = $(R.id.tv_item_my_wallet_coin_name);
            tv_total = $(R.id.tv_item_my_wallet_coin_total);
            tv_total_cny = $(R.id.tv_item_my_wallets_coin_totalcny);
            tv_coin_enname = $(R.id.tv_item_my_wallet_coin_enname);
            ll_coin.setOnClickListener(this);
        }

        @Override
        public void setData(MyWallet.ListBean myWallet) {
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(myWallet.getCoinType());
            tv_coin_enname.setText(coinInfo.getCoinname());
            tv_coin_name.setText(coinInfo.getCnname());
            ImageLoaderUtils.displayCircle(getContext(),iv_coin_icon,coinInfo.getImgurl());
            /*可用余额*/
            tv_total.setText(myWallet.getTotalBalance());
            /*折合人民币*/
            tv_total_cny.setText(myWallet.getTotalOfCny());

        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.getPosition(getPosition());
            }
        }
    }
}