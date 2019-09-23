package app.com.pgy.Widgets.mychart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Models.Beans.KLineBean;

/**
 * Created by xuqingzhong on 2017/2/8.
 * 柱状图
 */
public class MyCombinedChart extends CombinedChart {
    private MyLeftMarkerView myMarkerViewLeft;
    private MyHMarkerView myMarkerViewH;
    private MyBottomMarkerView myBottomMarkerView;
    private List<KLineBean.ListBean> kLineBeans;

    public MyCombinedChart(Context context) {
        super(context);
    }

    public MyCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMarker(MyLeftMarkerView markerLeft, MyHMarkerView markerH, List<KLineBean.ListBean> kLineBeans) {
        this.myMarkerViewLeft = markerLeft;
        this.myMarkerViewH = markerH;
        this.kLineBeans = kLineBeans;
    }

    public void setMarker(MyLeftMarkerView markerLeft, MyBottomMarkerView markerBottom, List<KLineBean.ListBean> kLineBeans) {
        this.myMarkerViewLeft = markerLeft;
        this.myBottomMarkerView = markerBottom;
        this.kLineBeans = kLineBeans;
    }

    public void setMarker(MyLeftMarkerView markerLeft, MyBottomMarkerView markerBottom, MyHMarkerView markerH, List<KLineBean.ListBean> kLineBeans) {
        this.myMarkerViewLeft = markerLeft;
        this.myBottomMarkerView = markerBottom;
        this.myMarkerViewH = markerH;
        this.kLineBeans = kLineBeans;
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (!mDrawMarkerViews || !valuesToHighlight()) {
            return;
        }
        for (int i = 0; i < mIndicesToHighlight.length; i++) {
            Highlight highlight = mIndicesToHighlight[i];
            int xIndex = mIndicesToHighlight[i].getXIndex();
            int dataSetIndex = mIndicesToHighlight[i].getDataSetIndex();
            float deltaX = mXAxis != null
                    ? mXAxis.mAxisRange
                    : ((mData == null ? 0.f : mData.getXValCount()) - 1.f);
            if (xIndex <= deltaX && xIndex <= deltaX * mAnimator.getPhaseX()) {
                Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
                // make sure entry not null
                if (e == null || e.getXIndex() != mIndicesToHighlight[i].getXIndex()) {
                    continue;
                }
                float[] pos = getMarkerPosition(e, highlight);
                // check bounds
                if (!mViewPortHandler.isInBounds(pos[0], pos[1])) {
                    continue;
                }
                if (null != myMarkerViewH) {
                    myMarkerViewH.refreshContent(e, mIndicesToHighlight[i]);
                    int width = (int) mViewPortHandler.contentWidth();
                    myMarkerViewH.setTvWidth(width);
                    myMarkerViewH.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    myMarkerViewH.layout(0, 0, width,
                            myMarkerViewH.getMeasuredHeight());
                    myMarkerViewH.draw(canvas, mViewPortHandler.contentLeft(), mIndicesToHighlight[i].getTouchY() - myMarkerViewH.getHeight() / 2);
                }

                if (null != myMarkerViewLeft) {
                    //修改标记值
                    float yValForHighlight = mIndicesToHighlight[i].getTouchYValue();
                    myMarkerViewLeft.setData(yValForHighlight);

                    myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);

                    myMarkerViewLeft.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    myMarkerViewLeft.layout(0, 0, myMarkerViewLeft.getMeasuredWidth(),
                            myMarkerViewLeft.getMeasuredHeight());

                    myMarkerViewLeft.draw(canvas, mViewPortHandler.contentLeft(), mIndicesToHighlight[i].getTouchY() - myMarkerViewLeft.getHeight() / 2);

                }

                if (null != myBottomMarkerView) {
                    if (kLineBeans == null){
                        kLineBeans = new ArrayList<>();
                    }
                    String time;
                    int xIndex1 = mIndicesToHighlight[i].getXIndex();
                    if (kLineBeans.size()-1 > xIndex1){
                        time  = kLineBeans.get(xIndex1).getDate();
                    }else{
                        time = "0.00";
                    }
                    myBottomMarkerView.setData(time);
                    myBottomMarkerView.refreshContent(e, mIndicesToHighlight[i]);

                    myBottomMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    myBottomMarkerView.layout(0, 0, myBottomMarkerView.getMeasuredWidth(),
                            myBottomMarkerView.getMeasuredHeight());

                    myBottomMarkerView.draw(canvas, pos[0] - myBottomMarkerView.getWidth() / 2, mViewPortHandler.contentBottom());
                }


            }
        }
    }
}
