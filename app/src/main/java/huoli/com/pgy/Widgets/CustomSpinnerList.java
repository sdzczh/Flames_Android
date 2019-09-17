package huoli.com.pgy.Widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import huoli.com.pgy.Adapters.CustomSpinnerListAdater;
import huoli.com.pgy.Interfaces.spinnerSingleChooseListener;
import huoli.com.pgy.R;

/**
 * Created by YX on 2018/7/9.
 */

public class CustomSpinnerList extends PopupWindow {
    private ListView listView;
    private View spinnerView;
    private spinnerSingleChooseListener mySpinnerListener;
    private Context context;
    private View targetView;

    public void setMySpinnerListener(spinnerSingleChooseListener mySpinnerListener) {
        this.mySpinnerListener = mySpinnerListener;
    }

    public CustomSpinnerList(Context context , List<String> item, View targetView) {
        super(context);
        this.context = context;
        this.targetView = targetView;
        if (item.size()<=0){
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
        spinnerView = View.inflate(context, R.layout.view_custom_spinner_list, null);
        listView = spinnerView.findViewById(R.id.lv_custom_spinner_list);
        TextView cancelView = spinnerView.findViewById(R.id.tv_custom_spinner_cancel);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setContentView(spinnerView);
    }

    /**
     *   初始化数据
     * @param context
     * @param item
     */
    private void initData(Context context, List<String> item) {
        //给ListView添加数据
        CustomSpinnerListAdater popListViewAdapter = new CustomSpinnerListAdater(context,item);
        listView.setAdapter(popListViewAdapter);
    }
    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(false);
        setBackgroundAlpha(0.8f);//设置屏幕透明度
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent));
        this.setBackgroundDrawable(dw);
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(false);

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
        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }
    /**
     * 设置显示在target上方(居中)
     * spacing为target与列表之间的间距
     */
    public void showUp() {
        //showAtLocation(spinnerView, Gravity.NO_GRAVITY, MathUtils.getWidthInPx(context)/2 - popupWidth/2,MathUtils.getHeightInPx(context)/2 - popupHeight/2);
        showAtLocation(targetView, Gravity.BOTTOM,0,0);
    }

    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }
}
