package app.com.pgy.Widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.Random;

import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;

/**
 * 挖矿 矿石
 * @author xuqingzhong
 * */

public class BlockImageView extends AppCompatImageView{
    private static final String TAG = "BlockImageView";
    private static final int DELAY_TIME = 10*1000;
    private Paint paintText,bgPaint;
    private Rect mBound;
    private RectF mBgRect;
    private static final int paddingSize = 10;
    private static final int roundSize = 10;
    private String text = "DK";

    public BlockImageView(Context context){
        this(context, null);
    }

    public BlockImageView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public BlockImageView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paintText = new Paint();
        mBound = new Rect();
        paintText.setTextSize(32);
        paintText.setColor(Color.WHITE);
        paintText.setStyle(Paint.Style.STROKE);
        paintText.setAntiAlias(true);
        paintText.getTextBounds(text, 0, text.length(), mBound);
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(getResources().getColor(R.color.bg_half));
        bgPaint.setAntiAlias(true);
        fadeIn();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();
        float textWidth = paintText.measureText(text);
        float x = width / 2 - textWidth / 2;
        Paint.FontMetrics metrics = paintText.getFontMetrics();
        //metrics.ascent为负数
        float dy = -(metrics.descent + metrics.ascent) / 2;
        float y = height / 2 + dy;
        mBgRect = new RectF(x-paddingSize*3,y-15-paddingSize*2,x+textWidth+paddingSize*3,y-8+paddingSize*2);
//        LogUtils.e("狂傲","left="+(x-paddingSize*3)+",top="+(y-16-paddingSize*2)+",right="+(x+textWidth+paddingSize*3)+",bottom="+(y+paddingSize*2));
        canvas.drawRoundRect(mBgRect,roundSize,roundSize,bgPaint);
        canvas.drawText(text, x,y, paintText);
    }

    public void setText(String text){
        this.text = text;
        invalidate();
    }

    public void setRadius(int radius){
        int diameter = radius * 2;
        LayoutParams params = new LayoutParams(diameter, diameter);
        setLayoutParams(params);
    }

    /**随机飘动*/
    public void floatToPosition(int duration){
        /*起始位置*/
        int startX = 0,startY = 0;
        int blockWidth = BlockImageView.this.getWidth();
        int blockHeight = BlockImageView.this.getHeight();
        Random random = new Random();
        /*随机xy为view的宽高*/
        if (blockWidth <= 0 || blockHeight <= 0){
            blockWidth = blockHeight = MathUtils.dip2px(getContext(), 50);
        }
        int randomMoveX = random.nextInt()%(blockWidth);
        int randomMoveY = random.nextInt()%(blockHeight);
        LogUtils.w("random","随机移动X："+randomMoveX+",Y:"+randomMoveY);
        float toX = getX() + randomMoveX;
        float toY = getY() + randomMoveY;
        /*判断边界*/
        if (toX <=blockWidth/2 || toX >= BlockImageView.this.getRootView().getWidth()-blockWidth/2){
            randomMoveX = -randomMoveX;
        }
        if (toY <= blockHeight/2 || toY >= BlockImageView.this.getRootView().getHeight()-blockHeight/2){
            randomMoveY = -randomMoveY;
        }

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(this, "translationX", startX, randomMoveX).setDuration(duration);
        translationX.setRepeatMode(ValueAnimator.REVERSE);
        translationX.setRepeatCount(ValueAnimator.INFINITE);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(this, "translationY", startY, randomMoveY).setDuration(duration);
        translationY.setRepeatMode(ValueAnimator.REVERSE);
        translationY.setRepeatCount(ValueAnimator.INFINITE);
        set.playTogether(translationX,translationY);
        //set.setInterpolator(new BounceInterpolator());
        set.setStartDelay(new Random().nextLong()%(DELAY_TIME/2));
        set.setDuration(DELAY_TIME).start();
    }

    /**进入场景*/
    public void fadeIn(){
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        ObjectAnimator scaleXIn = ObjectAnimator.ofFloat(this, "scaleX", 0f,1f);
        ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(this, "scaleY", 0f,1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeIn,scaleXIn,scaleYIn);
        set.setDuration(500).start();
        set.addListener(new AnimatorListener(){
            @Override
            public void onAnimationStart(Animator arg0){
                setEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animator arg0){

            }

            @Override
            public void onAnimationEnd(Animator arg0){
                setEnabled(true);
                floatToPosition(DELAY_TIME);
            }

            @Override
            public void onAnimationCancel(Animator arg0){

            }
        });
    }

    /**退出场景*/
    public void fadeOut(View targetView, final BlockImageViewFadeOutListener listener){
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        ObjectAnimator scaleXOut = ObjectAnimator.ofFloat(this, "scaleX", 1f,0f);
        ObjectAnimator scaleYOut = ObjectAnimator.ofFloat(this, "scaleY", 1f,0f);
        int[] location = new int[2];
        if (targetView != null) {
            targetView.getLocationOnScreen(location);
        }
        int targetX = location[0];
        int targetY = location[1];
        ObjectAnimator fadeX = ObjectAnimator.ofFloat(this, "translationX", 0,targetX-this.getX());
        ObjectAnimator fadeY = ObjectAnimator.ofFloat(this, "translationY", 0,targetY-this.getY());
        LogUtils.w(TAG,"targetX:"+targetX+",x:"+getX());
        LogUtils.w(TAG,"targetY:"+targetY+",y:"+getY());
        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeOut,scaleXOut,scaleYOut,fadeX,fadeY);
        set.setDuration(500).start();
        set.addListener(new AnimatorListener(){
            @Override
            public void onAnimationStart(Animator arg0){
                setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animator arg0){

            }

            @Override
            public void onAnimationEnd(Animator arg0){
                if (listener != null) {
                    listener.fadeOutTime();
                }
            }

            @Override
            public void onAnimationCancel(Animator arg0){

            }
        });
    }

    /**动画消失时候的监听*/
    public interface BlockImageViewFadeOutListener{
        void fadeOutTime();
    }
}