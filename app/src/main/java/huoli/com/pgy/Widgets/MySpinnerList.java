package huoli.com.pgy.Widgets;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import huoli.com.pgy.Adapters.MySpinnerListAdapter;
import huoli.com.pgy.Interfaces.spinnerSingleChooseListener;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.MathUtils;

/**
 * Created by xuqingzhong on 2017/12/21 0021.
 * @author xuqingzhong
 * 自定义spinner下拉、上弹列表框
 */

public class MySpinnerList extends PopupWindow {
    private int popupWidth;
    private int popupHeight;
    private ListView listView;
    private View spinnerView;
    private spinnerSingleChooseListener mySpinnerListener;
    private View targetView;

    public void setMySpinnerListener(spinnerSingleChooseListener mySpinnerListener) {
        this.mySpinnerListener = mySpinnerListener;
    }

    public MySpinnerList(Context context , List<String> item , View targetView) {
        super(context);
        this.targetView = targetView;
        if (item.size()<=0){
            return;
        }
        initView(context);
        initData(context,item);
        setPopConfig(context);
        /*设置整个widow的背景色，为一个light8的边框*/
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_light8stroke_whitesoild));
    }

    /**
     *   初始化控件
     * @param context
     */
    private void initView(Context context) {
        spinnerView = View.inflate(context, R.layout.layout_myspinner, null);
        listView = spinnerView.findViewById(R.id.myspinner_list);
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
     * @param context
     */
    private void setPopConfig(Context context) {
        //获取自身的长宽高
        spinnerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = spinnerView.getMeasuredHeight();
        //popupWidth = spinnerView.getMeasuredWidth();
//        this.setContentView(mDataView);//设置要显示的视图
        //this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(targetView.getMeasuredWidth());
        int count = listView.getCount();
        int halfHeight = MathUtils.getHeightInPx(context)/2;
        if (popupHeight*count>halfHeight){
            this.setHeight(halfHeight);
        }else{
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        /*ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.red));
        this.setBackgroundDrawable(dw);*/
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(true);

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
        /*this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mySpinnerListener != null) {
                    mySpinnerListener.onPopupWindowDismissListener();
                }
            }
        });*/

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
        //popupWidth = targetView.getMeasuredWidth();
        //LogUtils.w("pop","width:"+popupWidth);
        //获取target的位置xy值
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        /*使列表与target中心点对其*/
        //int locationX = (location[0] + targetView.getMeasuredWidth() / 2) - popupWidth / 2;
        /*列表y值为target的y值 减去 列表的高度*/
        int listCount = listView != null ?listView.getCount() :0;
        int locationY = location[1] - popupHeight*listCount;
        showAtLocation(targetView, Gravity.NO_GRAVITY,location[0] , locationY);
    }

    /**
     * 设置显示在target下方（居中）
     * spacing为target与列表之间的间距
     * @param
     */
    public void showDown() {
        //popupWidth = targetView.getMeasuredWidth();
        //LogUtils.w("pop","width:"+popupWidth);
        //获取target的xy值
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        /*使列表与target中心点对其*/
        //int locationX = (location[0] + targetView.getMeasuredWidth() / 2) - popupWidth / 2;
        int locationY = location[1]+targetView.getHeight();
        int locationX = location[0];
        showAtLocation(targetView,Gravity.NO_GRAVITY,locationX,locationY);
    }


    /**
     * 设置显示在target右侧
     * spacing为target与列表之间的间距
     */
    public void showRight() {
        //获取target的xy值
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);

        int locationX = location[0] + targetView.getMeasuredWidth();
        /*使列表与target顶部对其*/
        int locationY = location[1];
        showAtLocation(targetView,Gravity.NO_GRAVITY,locationX,locationY);
    }

    /**
     * 设置显示在target左侧
     * spacing为target与列表之间的间距
     */
    public void showLeft() {
        //获取target的xy值
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);

        int locationX = location[0] - targetView.getMeasuredWidth();
        /*使列表与target顶部对其*/
        int locationY = location[1];
        showAtLocation(targetView,Gravity.NO_GRAVITY,locationX,locationY);
    }
}
