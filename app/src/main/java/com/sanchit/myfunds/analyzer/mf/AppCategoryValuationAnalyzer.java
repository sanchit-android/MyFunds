package com.sanchit.myfunds.analyzer.mf;

import com.sanchit.myfunds.model.mf.MFAnalysisModel1;
import com.sanchit.myfunds.model.mf.MFTrade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppCategoryValuationAnalyzer {

    public ArrayList<MFAnalysisModel1> generateDataset(List<MFTrade> trades) {
        Map<String, MFAnalysisModel1> map = new HashMap<>();


        BigDecimal portfolioValuation = new BigDecimal(0);
        for (MFTrade trade : trades) {
            if (!map.containsKey(trade.fund.appDefinedCategory)) {
                map.put(trade.fund.appDefinedCategory, new MFAnalysisModel1());
            }

            MFAnalysisModel1 categoryInfo = map.get(trade.fund.appDefinedCategory);
            categoryInfo.Category = trade.fund.appDefinedCategory;
            categoryInfo.investment = categoryInfo.investment.add(trade.unitsAlloted.multiply(trade.transactionPrice).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            categoryInfo.units = categoryInfo.units.add(trade.unitsAlloted);

            portfolioValuation = portfolioValuation.add(trade.unitsAlloted.multiply(trade.transactionPrice));
            categoryInfo.valuation = new BigDecimal(0);

            categoryInfo.getTrades().add(trade);
        }

        for (Map.Entry<String, MFAnalysisModel1> entry : map.entrySet()) {
            MFAnalysisModel1 categoryInfo = entry.getValue();
            categoryInfo.percentage = categoryInfo.investment.multiply(new BigDecimal(100))
                    .divide(portfolioValuation, 2, BigDecimal.ROUND_HALF_DOWN)
                    .toPlainString() + "%";
        }

        ArrayList<MFAnalysisModel1> result = new ArrayList<>(map.values());
        Collections.sort(result, (o1, o2) -> o2.valuation.compareTo(o1.valuation));
        return result;
    }

}
