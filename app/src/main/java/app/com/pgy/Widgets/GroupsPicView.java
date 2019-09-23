package app.com.pgy.Widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;

/**
 * Created by YX on 2018/7/9.
 */

public class GroupsPicView extends PopupWindow {
    private View mView;
    private ImageView iv_close;
    private TextView tv_name;
    private ImageView iv_img;
    private TextView tv_title;
    private Context context;
    private View targetView;
    private SavePic savePic;
    private String imgUrl;
    private String name;

    public GroupsPicView(Context context,String name,View targetView,SavePic savePic){
        super(context);
        this.context = context;
        this.name = name;
        this.targetView = targetView;
        this.savePic = savePic;
        initView();
        setPopConfig();
    }

    private void initView(){
        mView = View.inflate(context, R.layout.view_group_erweima_popupwindow,null);
        iv_close = mView.findViewById(R.id.iv_group_close);
        tv_name = mView.findViewById(R.id.tv_groups_name);
        iv_img = mView.findViewById(R.id.iv_groups_img);
        tv_title = mView.findViewById(R.id.tv_groups_title);
        tv_name.setText(name);
        iv_img.setOnClickListener(saveOnClick);
        tv_title.setOnClickListener(saveOnClick);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setContentView(mView);
    }

    private View.OnClickListener saveOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (savePic != null){
                savePic.save(imgUrl);
            }
        }
    };

    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(false);
        setBackgroundAlpha(1f);//设置屏幕透明度
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.color_66000000));
        this.setBackgroundDrawable(dw);
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(true);

        //获取自身的长宽高
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {

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

    public void show(String url) {
        imgUrl = url;
        ImageLoaderUtils.display(context,iv_img,url,R.mipmap.ic_launcher);
        showAtLocation(targetView, Gravity.CENTER,0,0);
    }

    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public interface SavePic{
        void save(String url);
    }
}
