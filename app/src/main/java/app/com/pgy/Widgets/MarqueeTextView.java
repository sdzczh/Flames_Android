package app.com.pgy.Widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import app.com.pgy.Utils.LogUtils;

/**
 * Created by God-Office on 2018/3/26.
 */

public class MarqueeTextView extends AppCompatTextView {
    private int onTextW;
    private int  textWidth;
    private boolean isMeasure=false;
    String str;
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    /**
     * 获取文字宽度
     */
    public int  getTextWidth(){
        Paint paint=this.getPaint();
        str=this.getText().toString();
        textWidth= (int) paint.measureText(str);
        onTextW = textWidth/str.length();
        return textWidth;
    }

    private int getStrWidth(String tempstr){
        int w = 0;
        if (!TextUtils.isEmpty(tempstr)){
            Paint paint=this.getPaint();
            w = (int) paint.measureText(tempstr);
        }
        return w;
    }

    private String getNewText(){
        getTextWidth();
        if (getWidth() >= textWidth){
            while (getWidth() >= getStrWidth(str)){
                str = str+"\u3000\u3000";
            }
            LogUtils.e("textview","strlength=="+str.length());
            return str;
        }else {
            return "";
        }
    }

    public void startScoll(){
        String newText = getNewText();
        if (!TextUtils.isEmpty(newText)){
            super.setText(getNewText());
        }
    }

}
