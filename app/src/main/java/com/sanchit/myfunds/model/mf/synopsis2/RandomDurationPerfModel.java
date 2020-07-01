package com.sanchit.myfunds.model.mf.synopsis2;

import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.utils.Constants;
import com.sanchit.myfunds.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomDurationPerfModel extends Synopsis2DataModel {

    private final Comparator<BigDecimal> comparator;
    private final AnalysisKeySourcer analysisKeySourcer;
    private final String durationKey;
    private final Map<String, Map<String, BigDecimal[]>> priceMap;

    public RandomDurationPerfModel(String header, int cardColor,
                                   List<MFTrade> trades,
                                   Map<String, Map<String, BigDecimal[]>> priceMap,
                                   AnalysisKeySourcer analysisKeySourcer,
                                   Comparator<BigDecimal> comparator,
                                   String durationKey // Constants.Duration
    ) {
        super(header, cardColor, trades);
        this.priceMap = priceMap;
        this.analysisKeySourcer = analysisKeySourcer;
        this.comparator = comparator;
        this.durationKey = durationKey;
        initData();
    }

    public void initData() {
        Map<String, BigDecimal[]> profitMap = new HashMap<>();

        for (MFTrade trade : trades) {
            String key = analysisKeySourcer.fetch(trade);

            if (profitMap.containsKey(key)) {
                continue;
            }
            BigDecimal unitsAlloted = new BigDecimal(1);
            BigDecimal invested = unitsAlloted.multiply(priceMap.get(trade.fund.fundID).get(durationKey)[1]);
            BigDecimal valuation = unitsAlloted.multiply(trade.currentPrice);

            if(Constants.EMPTY_PRICE.equals(invested)) {
                continue;
            }

            BigDecimal profit = valuation.subtract(invested);
            BigDecimal profitPerct = profit.divide(invested, 4, BigDecimal.ROUND_HALF_DOWN);
            profitMap.put(key, new BigDecimal[]{profitPerct, valuation, invested});
        }

        String topDataLabel = null;
        BigDecimal topData = null;

        for (Map.Entry<String, BigDecimal[]> e : profitMap.entrySet()) {
            if (topData == null || comparator.compare(e.getValue()[0], topData) > 0) {
                topData = e.getValue()[0];
                topDataLabel = e.getKey();
            }
        }

        name = topDataLabel;
        data = NumberUtils.toPercentage(topData, 1)
                + "( "
                + profitMap.get(topDataLabel)[2].setScale(3, BigDecimal.ROUND_HALF_UP).toPlainString()
                + " - "
                + profitMap.get(topDataLabel)[1].setScale(3, BigDecimal.ROUND_HALF_UP).toPlainString()
                + " )";
    }
}
