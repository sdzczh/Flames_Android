package huoli.com.pgy.Widgets;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import huoli.com.pgy.Adapters.MySpinnerListAdapter;
import huoli.com.pgy.Interfaces.spinnerSingleChooseListener;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.MathUtils;

/**
 * Created by xuqingzhong on 2017/12/21 0021.
 * @author xuqingzhong
 * 自定义中间弹出列表框
 */

public class MyCenterSpinnerList extends PopupWindow {
    private ListView listView;
    private View spinnerView;
    private spinnerSingleChooseListener mySpinnerListener;
    private Context context;
    private View targetView;

    public void setMySpinnerListener(spinnerSingleChooseListener mySpinnerListener) {
        this.mySpinnerListener = mySpinnerListener;
    }

    public MyCenterSpinnerList(Context context , String title, List<String> item, View targetView) {
        super(context);
        this.context = context;
        this.targetView = targetView;
        if (item.size()<=0){
            return;
        }
        initView(context,title);
        initData(context,item);
        setPopConfig();
        /*设置整个widow的背景色，为一个light8的边框*/
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_corners_whitesolid));
    }

    /**
     *   初始化控件
     * @param context
     */
    private void initView(Context context,String title) {
        spinnerView = View.inflate(context, R.layout.layout_mycenterspinner, null);
        listView = spinnerView.findViewById(R.id.myCenterSpinner_list);
        TextView titleView = spinnerView.findViewById(R.id.myCenterSpinner_title);
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
        setContentView(spinnerView);
    }

    /**
     *   初始化数据
     * @param context
     * @param item
     */
    private void initData(Context context, List<String> item) {
        //给ListView添加数据
        MySpinnerListAdapter popListViewAdapter = new MySpinnerListAdapter(context,item);
        listView.setAdapter(popListViewAdapter);
    }
    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
//        this.setContentView(mDataView);//设置要显示的视图
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //this.setWidth(targetView.getMeasuredWidth());
        spinnerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = spinnerView.getMeasuredHeight();
        int count = listView.getCount();
        int halfHeight = MathUtils.getHeightInPx(context)/2;
        this.setHeight(popupHeight*count>halfHeight?halfHeight:ViewGroup.LayoutParams.WRAP_CONTENT);
        //this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        setBackgroundAlpha(0.8f);//设置屏幕透明度
        /*ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.red));
        this.setBackgroundDrawable(dw);*/
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(true);

        //获取自身的长宽高
        spinnerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

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

        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });

        //点击外围
        this.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusable(false);
                dismiss();
                return true;
            }
        });
    }
    /**
     * 设置显示在target上方(居中)
     * spacing为target与列表之间的间距
     */
    public void showUp() {
        //showAtLocation(spinnerView, Gravity.NO_GRAVITY, MathUtils.getWidthInPx(context)/2 - popupWidth/2,MathUtils.getHeightInPx(context)/2 - popupHeight/2);
        showAtLocation(targetView,Gravity.CENTER,0,0);
    }

    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }
}
