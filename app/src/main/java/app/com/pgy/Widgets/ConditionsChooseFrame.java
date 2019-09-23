package app.com.pgy.Widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;

/**
 * Created by xuqingzhong on 2017/12/21 0021.
 *
 * @author xuqingzhong
 *         条件筛选
 */

public class ConditionsChooseFrame extends PopupWindow implements MultiLineRadioGroup.OnCheckedChangedListener {
    private static final int DEFAULT_SELECT = -1;
    @BindView(R.id.layout_widget_conditionChoose_chooseType)
    MultiLineRadioGroup layoutWidgetConditionChooseChooseType;
    @BindView(R.id.layout_widget_conditionChoose_chooseState)
    MultiLineRadioGroup layoutWidgetConditionChooseChooseState;
    @BindView(R.id.view_titleWithChoose_title)
    TextView viewTitleWithChooseTitle;
    @BindView(R.id.view_titleWithChoose_statusBar)
    View viewTitleWithChooseStatusBar;
    private View rootFrame;
    private Context context;
    private int firstChoose, secondChoose;
    private ChooseConditionsListener getConditionsListener;

    public void setGetConditionsListener(ChooseConditionsListener getConditionsListener) {
        this.getConditionsListener = getConditionsListener;
    }

    public ConditionsChooseFrame(Context context, String title, List<String> tradeTypes, List<String> stateTypes,int defaultFirst,int defaultSecond) {
        super(context);
        this.context = context;
        initView(context, title);
        this.firstChoose = defaultFirst;
        this.secondChoose = defaultSecond;
        initData(tradeTypes, stateTypes);
        setPopConfig();
    }

    /**
     * 初始化控件
     *
     * @param context
     * @param title
     */
    private void initView(Context context, String title) {
        rootFrame = View.inflate(context, R.layout.layout_widget_conditionchoose, null);
        ButterKnife.bind(this, rootFrame);
        viewTitleWithChooseTitle.setText(title);
        viewTitleWithChooseStatusBar.setVisibility(View.GONE);
        layoutWidgetConditionChooseChooseType.setOnCheckChangedListener(this);
        layoutWidgetConditionChooseChooseState.setOnCheckChangedListener(this);
        setContentView(rootFrame);
    }

    /**
     * 初始化数据
     *
     * @param tradeTypes
     * @param stateTypes
     */
    private void initData(List<String> tradeTypes, List<String> stateTypes) {
        layoutWidgetConditionChooseChooseType.addAll(tradeTypes);
        layoutWidgetConditionChooseChooseState.addAll(stateTypes);
        layoutWidgetConditionChooseChooseType.setItemChecked(firstChoose);
        layoutWidgetConditionChooseChooseState.setItemChecked(secondChoose);
    }

    private void reSetData() {
        firstChoose = DEFAULT_SELECT;
        secondChoose = DEFAULT_SELECT;
        layoutWidgetConditionChooseChooseType.setItemChecked(firstChoose);
        layoutWidgetConditionChooseChooseState.setItemChecked(secondChoose);
    }

    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        //this.setContentView(mDataView);//设置要显示的视图
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        //setBackgroundAlpha(0.8f);//设置屏幕透明度
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(false);

        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                //setBackgroundAlpha(1f);
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
     *
     * @param target
     */
    public void showUp(View target) {
        showAtLocation(target.getRootView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 设置显示在target下方（居中）
     * spacing为target与列表之间的间距
     *
     * @param
     */
    public void showDown(View targetView) {
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        /*使列表与target中心点对其*/
        //int locationX = (location[0] + targetView.getMeasuredWidth() / 2) - popupWidth / 2;
        int locationY = location[1] + targetView.getHeight();
        int locationX = location[0];
        showAtLocation(targetView, Gravity.NO_GRAVITY, locationX, locationY);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    @OnClick({R.id.view_titleWithChoose_frame, R.id.layout_widget_conditionChoose_reset, R.id.layout_widget_conditionChoose_confirm, R.id.layout_widget_conditionChoose_bottomFrame})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*头部空白*/
            case R.id.view_titleWithChoose_frame:
                dismiss();
                break;
            /*重置*/
            case R.id.layout_widget_conditionChoose_reset:
                reSetData();
                break;
                /*确定*/
            case R.id.layout_widget_conditionChoose_confirm:
                if (getConditionsListener != null) {
                    getConditionsListener.getConditionsChoose(firstChoose, secondChoose);
                }
                dismiss();
                break;
                /*空白*/
            case R.id.layout_widget_conditionChoose_bottomFrame:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemChecked(MultiLineRadioGroup group, int position, boolean checked) {
        switch (group.getId()) {
            case R.id.layout_widget_conditionChoose_chooseType:
                LogUtils.w("conditions", "类型选择：" + position);
                firstChoose = position;
                break;
            case R.id.layout_widget_conditionChoose_chooseState:
                LogUtils.w("conditions", "状态选择：" + position);
                secondChoose = position;
                break;
            default:
        }
    }

    @OnClick()
    public void onViewClicked() {
    }

    public interface ChooseConditionsListener {
        void getConditionsChoose(int firstChoose, int secondChoose);
    }
}
