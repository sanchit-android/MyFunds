package com.sanchit.myfunds.model.mf.synopsis2;

import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PerformanceBasedModel extends Synopsis2DataModel {
    private final Comparator<BigDecimal> comparator;
    private final AnalysisKeySourcer analysisKeySourcer;

    public PerformanceBasedModel(String header, int cardColor, List<MFTrade> trades, AnalysisKeySourcer analysisKeySourcer, Comparator<BigDecimal> comparator) {
        super(header, cardColor, trades);
        this.analysisKeySourcer = analysisKeySourcer;
        this.comparator = comparator;
        initData();
    }

    public void initData() {
        Map<String, BigDecimal> valuationMap = new HashMap<>();
        Map<String, BigDecimal> investedMap = new HashMap<>();
        Map<String, BigDecimal> profitMap = new HashMap<>();
        Map<String, Set<String>> keyToFundMap = new HashMap<>();

        for (MFTrade trade : trades) {
            String key = analysisKeySourcer.fetch(trade);

            if (!investedMap.containsKey(key)) {
                investedMap.put(key, new BigDecimal(0));
            }
            BigDecimal invested = investedMap.get(key).add(trade.unitsAlloted.multiply(trade.transactionPrice).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN));
            investedMap.put(key, invested);

            if (!valuationMap.containsKey(key)) {
                valuationMap.put(key, new BigDecimal(0));
            }
            BigDecimal valuation = valuationMap.get(key).add(trade.unitsAlloted.multiply(trade.currentPrice).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN));
            valuationMap.put(key, valuation);

            if (!keyToFundMap.containsKey(key)) {
                keyToFundMap.put(key, new HashSet<>());
            }
            keyToFundMap.get(key).add(trade.fund.fundName);

            if (!profitMap.containsKey(key)) {
                profitMap.put(key, new BigDecimal(0));
            }
            BigDecimal profit = valuationMap.get(key).subtract(investedMap.get(key));
            profitMap.put(key, profit.divide(investedMap.get(key), 4, BigDecimal.ROUND_HALF_DOWN));
        }

        String topDataLabel = null;
        BigDecimal topData = null;

        for (Map.Entry<String, BigDecimal> e : profitMap.entrySet()) {
            if (topData == null || comparator.compare(e.getValue(), topData) > 0) {
                topData = e.getValue();
                topDataLabel = e.getKey();
            }
        }

        name = topDataLabel;
        data = NumberUtils.toPercentage(topData, 1) + " - " + NumberUtils.formatMoney(valuationMap.get(topDataLabel));
    }
}
