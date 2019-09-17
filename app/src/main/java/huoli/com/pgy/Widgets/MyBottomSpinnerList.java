package huoli.com.pgy.Widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import huoli.com.pgy.Adapters.MyBottomSpinnerListAdapter;
import huoli.com.pgy.Interfaces.spinnerSingleChooseListener;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.MathUtils;

/**
 * Created by xuqingzhong on 2017/12/21 0021.
 * @author xuqingzhong
 * 自定义spinner从父容器底部上弹列表框
 */

public class MyBottomSpinnerList extends PopupWindow {
    private static final String TAG = "MyBottomSpinnerList";
    private ListView listView;
    private View spinnerView;
    private TextView cancel;
    private spinnerSingleChooseListener mySpinnerListener;
    private Context context;

    public void setMySpinnerListener(spinnerSingleChooseListener mySpinnerListener) {
        this.mySpinnerListener = mySpinnerListener;
    }

    public MyBottomSpinnerList(Context context , List<String> item ) {
        super(context);
        if (item.size() <= 0){
            return;
        }
        LogUtils.w(TAG,"size:"+item.size());
        this.context = context;
        initView(context);
        initData(context,item);
        setPopConfig();
    }

    /**
     *   初始化控件
     * @param context
     */
    private void initView(Context context) {
        spinnerView = View.inflate(context, R.layout.layout_mybottomspinner, null);
        listView = spinnerView.findViewById(R.id.mybottomspinner_list);
        cancel = spinnerView.findViewById(R.id.mybottomspinner_cancel);
        setContentView(spinnerView);
    }

    /**
     *   初始化数据
     * @param context
     * @param item
     */
    private void initData(Context context, List<String> item) {
        //给ListView添加数据
        MyBottomSpinnerListAdapter popListViewAdapter = new MyBottomSpinnerListAdapter(context,item);
        listView.setAdapter(popListViewAdapter);
    }
    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
//        this.setContentView(mDataView);//设置要显示的视图
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        spinnerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = spinnerView.getMeasuredHeight();
        int count = listView.getCount();
        int halfHeight = MathUtils.getHeightInPx(context)/2;
        /*if (popupHeight*count>halfHeight){
            this.setHeight(halfHeight);
        }else{
        }*/
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        setBackgroundAlpha(0.8f);//设置屏幕透明度
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(false);

        //点击Item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if (mySpinnerListener != null) {
                    mySpinnerListener.onItemClickListener(position);
                }
            }
        });
        //点击返回键
        listView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                dismiss();
                return true;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
                /*if (mySpinnerListener != null) {
                    mySpinnerListener.onPopupWindowDismissListener();
                }*/
            }
        });

        //点击外围
        /*this.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusable(false);
                dismiss();
                return true;
            }
        });*/
    }
    /**
     * 设置显示在父布局上方(居中)
     * @param target
     */
    public void showUp(View target) {
        showAtLocation(target.getRootView(),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }
}
