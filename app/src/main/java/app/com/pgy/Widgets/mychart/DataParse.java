package app.com.pgy.Widgets.mychart;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.List;
import app.com.pgy.Models.Beans.KLineBean;
import app.com.pgy.Models.Beans.KMAEntity;
import app.com.pgy.Models.Beans.VMAEntity;
import app.com.pgy.Utils.MathUtils;

/**
 * @author xuqingzhong
 */
public class DataParse {

    /**
     * 得到成交量最大值
     * @return
     */
    public float getVolmax(List<KLineBean.ListBean> datas) {
        float volmax = 0;
        for (KLineBean.ListBean bean : datas) {
            volmax = Math.max(MathUtils.string2float(bean.getAmount()), volmax);
        }
        return volmax;
    }

    /**
     * 得到X轴数据
     * @return
     */
    public List<String> getXVals(List<KLineBean.ListBean> kLineBeanList) {
        List<String> xVals = new ArrayList<>();
        for (int i = 0; i < kLineBeanList.size(); i++) {
            xVals.add(kLineBeanList.get(i).getDate() + "");
        }
        return xVals;
    }

    /**
     * 得到K线数据
     * @return
     */
    public List<CandleEntry> getCandleEntries(List<KLineBean.ListBean> datas) {
        List<CandleEntry> candleEntries = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            KLineBean.ListBean bean = datas.get(i);
            candleEntries.add(new CandleEntry(i, bean.getHigh(), bean.getLow(), bean.getOpen(), bean.getClose()));
        }
        return candleEntries;
    }

    /**
     * 得到成交量数据
     * @return
     */
    public List<BarEntry> getBarEntries(List<KLineBean.ListBean> datas) {
        //成交量数据
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            barEntries.add(new BarEntry(i, datas.get(i).getHigh(), datas.get(i).getLow(), datas.get(i).getOpen(), datas.get(i).getClose(), datas.get(i).getVol()));
        }
        return barEntries;
    }


    /**
     * 得到K线图日均线
     * @return
     */
    public List<Entry> getMaDataL(List<KLineBean.ListBean> datas,int count) {
        List<Entry> maDataL = new ArrayList<>();
        KMAEntity kmaEntity = new KMAEntity(datas,count);
        for (int i = 0; i < kmaEntity.getMAs().size(); i++) {
            maDataL.add(new Entry(kmaEntity.getMAs().get(i), i));
        }
        return maDataL;
    }


    /**
     * 得到成交量5日均线
     * @return
     */
    public List<Entry> getMaDataV(List<KLineBean.ListBean> datas, int count) {
        List<Entry> maDataV = new ArrayList<>();
        VMAEntity vmaEntity = new VMAEntity(datas, count);
        for (int i = 0; i < vmaEntity.getMAs().size(); i++) {
            maDataV.add(new Entry(vmaEntity.getMAs().get(i), i));
        }
        return maDataV;
    }
}
