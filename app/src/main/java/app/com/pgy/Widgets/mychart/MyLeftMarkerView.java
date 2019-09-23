package app.com.pgy.Widgets.mychart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DecimalFormat;

import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;

/**
 * Created by xuqingzhong on 2017/2/8.
 * 左边标志图
 */
public class MyLeftMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView markerTv;
    private float num;
    private  DecimalFormat mFormat;
    public MyLeftMarkerView(Context context, int layoutResource,int digits) {
        super(context, layoutResource);
        mFormat = MathUtils.getDecimalFormat(digits);
        //mFormat=new DecimalFormat("#0.00");
        markerTv = findViewById(R.id.marker_tv);
        markerTv.setTextSize(10);
    }

    public void setData(float num){

        this.num=num;
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        markerTv.setText(mFormat.format(num));
    }

    @Override
    public int getXOffset(float xpos) {
        return 0;
    }

    @Override
    public int getYOffset(float ypos) {
        return 0;
    }
}
