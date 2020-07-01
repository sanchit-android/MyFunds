package com.sanchit.myfunds.model.mf.synopsis2.datalist;

import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.utils.Constants;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MFDataListSummaryModel {
    private BigDecimal investment = BigDecimal.ZERO;
    private BigDecimal valuation = BigDecimal.ZERO;
    private BigDecimal valuation_T_minus_1 = BigDecimal.ZERO;

    public MFDataListSummaryModel(List<MFTrade> trades, Map<String, Map<String, BigDecimal[]>> priceMap) {
        Set<String> uniqueFunds = new HashSet<>();
        Set<String> uniqueCategories = new HashSet<>();
        for (MFTrade trade : trades) {
            investment = getInvestment().add(trade.unitsAlloted.multiply(trade.transactionPrice));
            valuation = getValuation().add(trade.unitsAlloted.multiply(trade.currentPrice));
            valuation_T_minus_1 = valuation_T_minus_1.add(trade.unitsAlloted.multiply(priceMap.get(trade.fund.fundID).get(Constants.Duration.T_1)[1]));
            uniqueFunds.add(trade.fund.fundName);
            uniqueCategories.add(trade.fund.appDefinedCategory);
        }
    }

    public BigDecimal getInvestment() {
        return investment;
    }

    public BigDecimal getValuation() {
        return valuation;
    }

    public BigDecimal getOverallPNL() {
        return (valuation.subtract(investment)).divide(investment, 4, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getUnrealizedPNL() {
        return (valuation.subtract(investment)).divide(investment, 4, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getDayPNL() {
        return (valuation.subtract(valuation_T_minus_1)).divide(valuation_T_minus_1, 4, BigDecimal.ROUND_HALF_UP);
    }
}
