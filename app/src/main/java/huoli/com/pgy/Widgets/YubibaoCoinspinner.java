package huoli.com.pgy.Widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import huoli.com.pgy.Interfaces.spinnerSingleChooseListener;
import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Utils.ToolsUtils;

/**
 * Created by YX on 2018/7/18.
 */

public class YubibaoCoinspinner extends PopupWindow {
    private Context context;
    private MyDemoGridView listView;
    private View spinnerView;
    private View bottomView;
    private spinnerSingleChooseListener mySpinnerListener;

    public YubibaoCoinspinner(Context context , List<Integer> item,spinnerSingleChooseListener mySpinnerListener) {
        super(context);
        this.context = context;
        this.mySpinnerListener = mySpinnerListener;
        if (item.size() <= 0){
            return;
        }
        initView(context);
        initData(context,item);
        setPopConfig();
    }

    /**
     *   初始化控件
     * @param context
     */
    private void initView(Context context) {
        spinnerView = View.inflate(context, R.layout.popup_yubibao_coinspinner, null);
        listView =  spinnerView.findViewById(R.id.rv_popup_yubibao_coinlist);
        bottomView = spinnerView.findViewById(R.id.view_bottom);
        spinnerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        listView.setOnTouchBlankPositionListener(new MyDemoGridView.OnTouchBlankPositionListener() {
            @Override
            public boolean onTouchBlankPosition() {
                dismiss();
                return true;
            }
        });
    }

    /**
     *   初始化数据
     * @param context
     * @param item
     */
    private void initData(Context context, List<Integer> item) {
        final SpinnerCoinListAdapter adapter = new SpinnerCoinListAdapter(context,item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mySpinnerListener != null){
                    mySpinnerListener.onItemClickListener(position);
                }
            }
        });
    }
    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(spinnerView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //点击返回键
        listView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                dismiss();
                return true;
            }
        });
        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }
    /**
     * 设置显示在target下方
     * spacing为target与列表之间的间距
     * @param
     */
    public void showDown(View targetView) {
        showAtLocation(targetView, Gravity.BOTTOM,0,0);
    }

    class SpinnerCoinListAdapter extends BaseAdapter{
        private Context mContext;
        private List<Integer> coins;
        public SpinnerCoinListAdapter(Context context,List<Integer> coins){
            this.mContext = context;
            this.coins = coins;
        }

        @Override
        public int getCount() {
            return coins == null?0:coins.size();
        }

        @Override
        public Object getItem(int position) {
            return coins == null || coins.size() <= position?null:coins.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_yubiao_coin_spinner_view,parent,false);
            }
            int coinType = coins.get(position);
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(coinType);
            ImageView iv_icon  = convertView.findViewById(R.id.riv_item_yubiao_coin_spinner_icon);
            TextView tv_name = convertView.findViewById(R.id.tv_item_yubibao_coin_spinner_title) ;
            ImageLoaderUtils.displayCircle(mContext,iv_icon,coinInfo.getImgurl());
            tv_name.setText(coinInfo.getCoinname());
            return convertView;
        }
    }
}
