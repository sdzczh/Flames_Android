package app.com.pgy.Constants;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Models.Beans.BuyOrSale;
import app.com.pgy.Models.Beans.C2cBusinessEntrust;
import app.com.pgy.Models.Beans.C2cNormalBusiness;
import app.com.pgy.Models.Beans.C2cNormalEntrust;
import app.com.pgy.Models.Beans.Entrust;
import app.com.pgy.Models.Beans.EntrustDetails;
import app.com.pgy.Models.Beans.ResponseBean.LastDealBean;

import static app.com.pgy.Constants.StaticDatas.BUY;

/**
 * 创建日期：2018/4/23 0023 on 下午 6:09
 * 描述:
 *
 * @author 徐庆重
 */

public class DefaultData {

    public static LastDealBean getTradeC2cLastDealList() {
        LastDealBean entrust = new LastDealBean();
        List<LastDealBean.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<5;i++){
            LastDealBean.ListBean bean = new LastDealBean.ListBean();
            bean.setOrderType(i%2);
            bean.setAmount("1"+i);
            bean.setPrice("20"+i);
            bean.setCreateTime(i+"00"+i);
            listBeans.add(bean);
        }
        entrust.setList(listBeans);
        return entrust;
    }


    public static LastDealBean getTradeGoodsLastDealList() {
        LastDealBean entrust = new LastDealBean();
        List<LastDealBean.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<5;i++){
            LastDealBean.ListBean bean = new LastDealBean.ListBean();
            bean.setOrderType(i%2);
            bean.setAmount("1"+i);
            bean.setPrice("20"+i);
            bean.setCreateTime(i+"00"+i);
            listBeans.add(bean);
        }
        entrust.setList(listBeans);
        return entrust;
    }

    public static List<BuyOrSale.ListBean> getGoodsList() {
        List<BuyOrSale.ListBean> list = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            BuyOrSale.ListBean bean = new BuyOrSale.ListBean();
            bean.setNum(i);
            bean.setPrice("0.00");
            bean.setRemain("0.00");
            list.add(bean);
        }
        return list;
    }

    public static Entrust getTradeGoodsCurrentEntrustList() {
        Entrust entrust = new Entrust();
        List<Entrust.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<5;i++){
            Entrust.ListBean bean = new Entrust.ListBean();
            bean.setOrderCoinType(i%8);
            bean.setUnitCoinType(i%8+2);
            bean.setOrderType(i%2);
            bean.setAmount("1"+i);
            bean.setPrice("20"+i);
            bean.setDealAmount(i*10+20+"");
            bean.setCreateTime(i+"00"+i);
            listBeans.add(bean);
        }
        entrust.setList(listBeans);
        return entrust;
    }

    public static EntrustDetails getEntrustDetails(int id) {
        EntrustDetails details = new EntrustDetails();
        details.setAmount("101");
        details.setAverage("55");
        details.setCreateTime("12-12-12");
        details.setDealAmount("22");
        details.setOrderCoinType(4);
        details.setTotal("222");
        details.setUnitCoinType(1);
        details.setType(BUY);
        List<EntrustDetails.ListBean> list = new ArrayList<>();
        for (int i=0;i<13;i++){
            EntrustDetails.ListBean bean = new EntrustDetails.ListBean();
            bean.setAmount(i+"0");
            bean.setCreateTime("12-12-1"+i);
            bean.setOrderCoinType(4);
            bean.setUnitCoinType(1);
            bean.setPrice(i+"0");
            bean.setTotal("20"+i);
            list.add(bean);
        }
        details.setList(list);
        return details;
    }

    public static C2cNormalBusiness getTradeC2cBuyNormalList() {
        C2cNormalBusiness entrust = new C2cNormalBusiness();
        List<C2cNormalBusiness.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<5;i++){
            C2cNormalBusiness.ListBean bean = new C2cNormalBusiness.ListBean();
            bean.setPayType(i%8);
            bean.setUserName("测试人员"+i);
            bean.setPrice("20"+i);
            bean.setRemain(i*10+20+"");
            bean.setQuantity(i*10+i);
            bean.setTotalMin(i*10+"");
            bean.setTotalMax(i*11+"");
            listBeans.add(bean);
        }
        entrust.setList(listBeans);
        return entrust;
    }

    public static List<C2cNormalEntrust.ListBean> getC2CTradeOrder(int tradeType, int stateType) {
         List<C2cNormalEntrust.ListBean> list = new ArrayList<>();
        for (int i =1;i<20;i++){
            C2cNormalEntrust.ListBean bean = new C2cNormalEntrust.ListBean();
            bean.setPrice("6.70"+i);
            bean.setAmount("100"+i);
            bean.setCreateTime("2018-01-01 15:00:00");
            bean.setTotal("6700.00");
            bean.setId(i);
            if (stateType < 0) {
                bean.setState(i % 6);
            }else{
                bean.setState(tradeType);
            }
            if (tradeType < 0){
                bean.setOrderType(i % 2);
            }else {
                bean.setOrderType(stateType);
            }
            bean.setInactiveTime("16:00");
            list.add(bean);
        }
        return list;
    }

    public static List<C2cBusinessEntrust.ListBean> getC2cEntrustList(int tradeType, int stateType) {
        List<C2cBusinessEntrust.ListBean> list = new ArrayList<>();
        for (int i =1;i<20;i++){
            C2cBusinessEntrust.ListBean bean = new C2cBusinessEntrust.ListBean();
            bean.setPrice("6.70"+i);
            bean.setRemain(i+"00.00");
            bean.setAmount("100"+i);
            bean.setTotalMin(i+"00.00");
            bean.setTotalMax(i+"000.00");
            bean.setCreateTime("2018-01-01 15:00:00");
            bean.setId(i);
            if (stateType<0) {
                bean.setState(i % 3);
            }else{
                bean.setState(stateType);
            }
            if (tradeType<0) {
                bean.setOrderType(i % 2);
            }else {
                bean.setOrderType(tradeType);
            }
            bean.setOrderFlag((i%2)==0);
            bean.setPayType(i%8);
            list.add(bean);
        }
        return list;
    }

   /* *//**获取算力等级划分*//*
    public static List<Configuration.CalculateForceLevel> getForceLevels() {
        List<Configuration.CalculateForceLevel> list = new ArrayList<>();
        for (int i=1;i<=8;i++){
            Configuration.CalculateForceLevel forceLevel = new Configuration.CalculateForceLevel();
            forceLevel.setLevel(i);
            forceLevel.setName("等级"+i);
            forceLevel.setStart(i*100);
            forceLevel.setEnd(i*100+99);
            forceLevel.setHomeName("村落"+i);
            List<Integer> coins = new ArrayList<>();
            for (int j=1;j<=i;j++) {
                coins.add(j);
            }
            forceLevel.setDigCoinType(coins);
            list.add(forceLevel);

        }
        return list;
    }

    public static Map<Integer,Configuration.CalculateForceLevel> getLevelDetailMap() {
        Map<Integer,Configuration.CalculateForceLevel> levelDetailMap = new HashMap<>();
        for (Configuration.CalculateForceLevel level:getForceLevels()) {
            levelDetailMap.put(level.getLevel(),level);
        }
        return levelDetailMap;
    }*/
    public static String shenDuData="{\n" +
                    "\t\"code\": 1,\n" +
                    "\t\"info\": {\n" +
                    "\t\t\"asks\": [\n" +
                    "\t\t\t[\"6637.2\", \"1.43997106\", \"10\"],\n" +
                    "\t\t\t[\"6637.4\", \"0.2\", \"1\"],\n" +
                    "\t\t\t[\"6637.6\", \"0.625\", \"2\"],\n" +
                    "\t\t\t[\"6637.8\", \"0.067898\", \"1\"],\n" +
                    "\t\t\t[\"6638\", \"0.298\", \"1\"],\n" +
                    "\t\t\t[\"6638.2\", \"0.002\", \"2\"],\n" +
                    "\t\t\t[\"6638.4\", \"0.071\", \"2\"],\n" +
                    "\t\t\t[\"6638.6\", \"3.35752014\", \"4\"],\n" +
                    "\t\t\t[\"6638.8\", \"2.70309529\", \"4\"],\n" +
                    "\t\t\t[\"6639\", \"1.78562799\", \"5\"],\n" +
                    "\t\t\t[\"6639.2\", \"0.2\", \"1\"],\n" +
                    "\t\t\t[\"6639.4\", \"2.38349223\", \"2\"],\n" +
                    "\t\t\t[\"6639.6\", \"0.02206889\", \"1\"],\n" +
                    "\t\t\t[\"6639.8\", \"0.69645635\", \"3\"],\n" +
                    "\t\t\t[\"6640\", \"0.15698473\", \"1\"],\n" +
                    "\t\t\t[\"6640.2\", \"0.02704833\", \"2\"],\n" +
                    "\t\t\t[\"6640.4\", \"0.198868\", \"1\"],\n" +
                    "\t\t\t[\"6640.6\", \"0.46629666\", \"3\"],\n" +
                    "\t\t\t[\"6640.8\", \"0.02601555\", \"1\"],\n" +
                    "\t\t\t[\"6641.2\", \"0.42398334\", \"2\"],\n" +
                    "\t\t\t[\"6641.6\", \"2.304\", \"1\"],\n" +
                    "\t\t\t[\"6642\", \"2.5\", \"2\"],\n" +
                    "\t\t\t[\"6642.2\", \"1\", \"1\"],\n" +
                    "\t\t\t[\"6642.4\", \"0.06030782\", \"5\"],\n" +
                    "\t\t\t[\"6642.6\", \"0.80862201\", \"2\"],\n" +
                    "\t\t\t[\"6642.8\", \"0.02699547\", \"2\"],\n" +
                    "\t\t\t[\"6643\", \"0.24538547\", \"3\"],\n" +
                    "\t\t\t[\"6643.2\", \"2.3505\", \"2\"],\n" +
                    "\t\t\t[\"6643.4\", \"0.0039\", \"1\"],\n" +
                    "\t\t\t[\"6643.6\", \"0.3442\", \"1\"]\n" +
                    "\t\t],\n" +
                    "\t\t\"bids\": [\n" +
                    "\t\t\t[\"6637\", \"0.04995325\", \"8\"],\n" +
                    "\t\t\t[\"6636.6\", \"0.02471745\", \"2\"],\n" +
                    "\t\t\t[\"6635.8\", \"0.00242657\", \"1\"],\n" +
                    "\t\t\t[\"6635.6\", \"0.12548393\", \"8\"],\n" +
                    "\t\t\t[\"6635.4\", \"0.001\", \"1\"],\n" +
                    "\t\t\t[\"6635\", \"1.003\", \"2\"],\n" +
                    "\t\t\t[\"6634.6\", \"1.219\", \"3\"],\n" +
                    "\t\t\t[\"6634.4\", \"0.296\", \"1\"],\n" +
                    "\t\t\t[\"6633.8\", \"0.085\", \"1\"],\n" +
                    "\t\t\t[\"6633.6\", \"0.015\", \"1\"],\n" +
                    "\t\t\t[\"6633.4\", \"1.002\", \"3\"],\n" +
                    "\t\t\t[\"6633.2\", \"0.035\", \"1\"],\n" +
                    "\t\t\t[\"6633\", \"0.049005\", \"2\"],\n" +
                    "\t\t\t[\"6632.6\", \"0.17957091\", \"5\"],\n" +
                    "\t\t\t[\"6632.2\", \"2.40244965\", \"3\"],\n" +
                    "\t\t\t[\"6632\", \"0.07849191\", \"1\"],\n" +
                    "\t\t\t[\"6631.8\", \"0.29014965\", \"4\"],\n" +
                    "\t\t\t[\"6631.6\", \"0.16267973\", \"3\"],\n" +
                    "\t\t\t[\"6631.4\", \"6.64130293\", \"2\"],\n" +
                    "\t\t\t[\"6631.2\", \"1.0495\", \"3\"],\n" +
                    "\t\t\t[\"6631\", \"1.0037\", \"2\"],\n" +
                    "\t\t\t[\"6630.4\", \"3.35970639\", \"5\"],\n" +
                    "\t\t\t[\"6630.2\", \"0.63658932\", \"4\"],\n" +
                    "\t\t\t[\"6630\", \"2\", \"1\"],\n" +
                    "\t\t\t[\"6629.8\", \"0.0036\", \"1\"],\n" +
                    "\t\t\t[\"6629.6\", \"0.11294166\", \"2\"],\n" +
                    "\t\t\t[\"6628.8\", \"0.051\", \"1\"],\n" +
                    "\t\t\t[\"6628.4\", \"0.1006\", \"2\"]\n" +
                    "\t\t],\n" +
                    "\t\t\"timestamp\": \"2020-04-02T08:30:15.312Z\"\n" +
                    "\t},\n" +
                    "\t\"msg\": \"成功\",\n" +
                    "\t\"scene\": 3523\n" +
                    "}";

    @Override
    public boolean equals(Object obj) {

        return super.equals(obj);
    }
}
