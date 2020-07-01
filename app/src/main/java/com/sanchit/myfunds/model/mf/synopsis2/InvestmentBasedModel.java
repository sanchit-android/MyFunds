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

public class InvestmentBasedModel extends Synopsis2DataModel {
    private final Comparator<BigDecimal> comparator;
    private final AnalysisKeySourcer analysisKeySourcer;

    public InvestmentBasedModel(String header, int cardColor, List<MFTrade> trades, AnalysisKeySourcer analysisKeySourcer, Comparator<BigDecimal> comparator) {
        super(header, cardColor, trades);
        this.analysisKeySourcer = analysisKeySourcer;
        this.comparator = comparator;
        initData();
    }

    public void initData() {
        Map<String, BigDecimal> investedMap = new HashMap<>();
        Map<String, Set<String>> keyToFundMap = new HashMap<>();

        for (MFTrade trade : trades) {
            String key = analysisKeySourcer.fetch(trade);

            if (!investedMap.containsKey(key)) {
                investedMap.put(key, new BigDecimal(0));
            }
            BigDecimal invested = investedMap.get(key).add(trade.unitsAlloted.multiply(trade.transactionPrice).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN));
            investedMap.put(key, invested);

            if (!keyToFundMap.containsKey(key)) {
                keyToFundMap.put(key, new HashSet<>());
            }
            keyToFundMap.get(key).add(trade.fund.fundName);
        }

        String topDataLabel = null;
        BigDecimal topData = null;

        for (Map.Entry<String, BigDecimal> e : investedMap.entrySet()) {
            if (topData == null || comparator.compare(e.getValue(), topData) > 0) {
                topData = e.getValue();
                topDataLabel = e.getKey();
            }
        }

        name = topDataLabel;
        data = NumberUtils.formatMoney(topData);
    }
}
