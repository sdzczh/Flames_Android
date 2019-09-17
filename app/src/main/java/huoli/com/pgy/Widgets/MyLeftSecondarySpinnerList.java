package huoli.com.pgy.Widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import huoli.com.pgy.Adapters.LeftSecondarySpinnerAdapter;
import huoli.com.pgy.Interfaces.getPositionCallback;
import huoli.com.pgy.Interfaces.spinnerSecondaryChooseListener;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.ToolsUtils;

/**
 * @author xuqingzhong
 *         自定义spinner二级联动下拉列表
 */

public class MyLeftSecondarySpinnerList extends PopupWindow {
    private static final String TAG = "MyLeftSecondarySpinnerL";
    private TabLayout tab;
    private RecyclerView recyclerView;
    private LeftSecondarySpinnerAdapter adapter;
    private Context context;
    private int popupWidth;
    private int popupHeight;
    private View spinnerView;
    /**
     * 根目录被选中的节点
     */
    private int selectedPerCoinType;
    private int selectedTradeCoinType;
    private spinnerSecondaryChooseListener listener;
    private List<Integer> tradeCoins;

    public void setListener(spinnerSecondaryChooseListener listener) {
        this.listener = listener;
    }

    public MyLeftSecondarySpinnerList(Context context, List<Integer> rootList, Map<Integer, List<Integer>> subList) {
        super(context);
        this.context = context;
        if (rootList.size() <= 0) {
            return;
        }
        initView(context);
        initData(context, rootList, subList);
        setPopConfig();
        /*设置整个widow的背景色，为一个light8的边框*/
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_corners_whitesolid));
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {
        spinnerView = View.inflate(context, R.layout.layout_widget_leftsecondaryspinner, null);
        tab = spinnerView.findViewById(R.id.layout_widget_leftSecondarySpinner_tab);
        recyclerView = spinnerView.findViewById(R.id.layout_widget_leftSecondarySpinner_list);
        recyclerView.addItemDecoration(new DividerDecoration(context.getResources().getColor(R.color.divider_line),MathUtils.dip2px(context,1)));
        spinnerView.findViewById(R.id.layout_widget_leftSecondarySpinner_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        /*弹出popupwindow时，二级菜单默认隐藏，当点击某项时，二级菜单再弹出*/
        setContentView(spinnerView);
    }

    /**
     * 初始化数据
     *
     * @param mContext
     * @param rootList 一级列表
     * @param subMap  二级页面
     */
    private void initData(final Context mContext, final List<Integer> rootList, final Map<Integer, List<Integer>> subMap) {
        if (rootList.size() <= 0 || subMap.size() <= 0) {
            return;
        }
        /*设置标题*/
        for (Integer coinType : rootList) {
            String coinName = ToolsUtils.getCoinName(coinType);
            tab.addTab(tab.newTab().setText(coinName));
        }
        /*初始化RecyclerView*/
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (adapter == null){
            adapter = new LeftSecondarySpinnerAdapter(mContext);
        }
        recyclerView.setAdapter(adapter);
        /*选中的计价币,及相应的交易币列表*/
        selectedPerCoinType = rootList.get(0);
        tradeCoins = subMap.get(selectedPerCoinType);
        if (tradeCoins == null){
            tradeCoins = new ArrayList<>();
        }
        adapter.addAll(tradeCoins);
        LogUtils.w(TAG,"交易币"+tradeCoins.toString());
        /*切换计价币，换交易币*/
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtils.w(TAG,"selectPer:"+selectedPerCoinType+",pos:"+tab.getPosition()+",tab:"+rootList.get(tab.getPosition()));
                selectedPerCoinType = rootList.get(tab.getPosition());
                adapter.clear();
                tradeCoins = subMap.get(selectedPerCoinType);
                if (tradeCoins == null){
                    tradeCoins = new ArrayList<>();
                }
                LogUtils.w(TAG,"交易币"+tradeCoins.toString());
                adapter.addAll(tradeCoins);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        adapter.setOnItemClickListener(new getPositionCallback() {
            @Override
            public void getPosition(int pos) {
                /*获取并设置选中计价币种*/
                if (tradeCoins == null || tradeCoins.size() <= 0) {
                    selectedTradeCoinType = -1;
                } else {
                    selectedTradeCoinType = tradeCoins.get(pos);
                }
                if (listener != null) {
                    listener.onItemClickListener(selectedPerCoinType, selectedTradeCoinType);
                }
                dismiss();
            }
        });

    }

    private void addCurrentTradeCoinList(Map<Integer, List<Integer>> subMap, int selectedPerCoinType) {

    }

    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        //获取自身的长宽高
        spinnerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWidth = spinnerView.getMeasuredWidth();
        popupHeight = spinnerView.getMeasuredHeight();
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        setBackgroundAlpha(0.8f);//设置屏幕透明度
        ColorDrawable dw = new ColorDrawable(0x50000000);
        this.setBackgroundDrawable(dw);
        this.setClippingEnabled(false);
        //setWindowFullScreen(true);
        this.setAnimationStyle(R.style.AnimationLeftMove);
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(true);
        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
                //setWindowFullScreen(false);
                /*if (mySpinnerListener != null) {
                    mySpinnerListener.onPopupWindowDismissListener();
                }*/
            }
        });
    }

    public void showLeft() {
        showAtLocation(spinnerView,Gravity.TOP | Gravity.START,0,0);
    }

    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    private void setWindowFullScreen(boolean isFull){
        /*全屏覆盖状态栏*/
        Window window = ((Activity) context).getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        if (isFull) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(lp);
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 获取状态通知栏高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }


}
