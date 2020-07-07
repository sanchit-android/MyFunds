package com.sanchit.myfunds.model.mf.synopsis2.datalist.factory;

import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.model.mf.synopsis2.AnalysisKeySourcer;
import com.sanchit.myfunds.model.mf.synopsis2.datalist.MFDataListModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public abstract class AbstractFactory {

    protected static final int SCALE_DISPLAY = 0;

    protected List<MFTrade> trades;
    protected String header;
    protected boolean descending = false;
    protected Map<String, Map<String, BigDecimal[]>> priceMap;

    protected AbstractFactory(List<MFTrade> trades, String header, Map<String, Map<String, BigDecimal[]>> priceMap) {
        this.trades = trades;
        this.header = header;
        this.priceMap = priceMap;
    }

    protected AbstractFactory(List<MFTrade> trades, String header) {
        this.trades = trades;
        this.header = header;
    }

    public static AbstractFactory getFor(String type, List<MFTrade> trades, Map<String, Map<String, BigDecimal[]>> priceMap) {
        if ("TOP PERFORMING CATEGORY".equals(type)) {
            return new InceptionPerformanceBasedFactory(trades, type, AnalysisKeySourcer.APP_DEFINED_CATEGORY_KEY).descending();
        } else if ("LEAST PERFORMING CATEGORY".equals(type)) {
            return new InceptionPerformanceBasedFactory(trades, type, AnalysisKeySourcer.APP_DEFINED_CATEGORY_KEY);
        } else if ("TOP PERFORMING FUND".equals(type)) {
            return new InceptionPerformanceBasedFactory(trades, type, AnalysisKeySourcer.FUND_NAME_KEY).descending();
        } else if ("LEAST PERFORMING FUND".equals(type)) {
            return new InceptionPerformanceBasedFactory(trades, type, AnalysisKeySourcer.FUND_NAME_KEY);
        } else if ("TOP FUND - 1 MONTH".equals(type)) {
            return new MultipleDurationPerformanceFactory(trades, type, AnalysisKeySourcer.FUND_NAME_KEY, priceMap).descending();
        } else if ("BOTTOM FUND - 1 MONTH".equals(type)) {
            return new MultipleDurationPerformanceFactory(trades, type, AnalysisKeySourcer.FUND_NAME_KEY, priceMap);
        }
        return new DummyFactory(trades, type);
    }

    public AbstractFactory descending() {
        descending = true;
        return this;
    }

    public abstract List<MFDataListModel> generateData();

}
