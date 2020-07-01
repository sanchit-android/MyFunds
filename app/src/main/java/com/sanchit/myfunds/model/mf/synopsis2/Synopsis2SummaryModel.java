package com.sanchit.myfunds.model.mf.synopsis2;

import com.sanchit.myfunds.model.mf.MFTrade;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Synopsis2SummaryModel {
    private BigDecimal investment = BigDecimal.ZERO;
    private BigDecimal valuation = BigDecimal.ZERO;

    private int funds = 0;
    private int categories = 0;

    public Synopsis2SummaryModel(List<MFTrade> trades, Map<String, Map<String, BigDecimal[]>> priceMap) {
        Set<String> uniqueFunds = new HashSet<>();
        Set<String> uniqueCategories = new HashSet<>();
        for (MFTrade trade : trades) {
            investment = getInvestment().add(trade.unitsAlloted.multiply(trade.transactionPrice));
            valuation = getValuation().add(trade.unitsAlloted.multiply(trade.currentPrice));
            uniqueFunds.add(trade.fund.fundName);
            uniqueCategories.add(trade.fund.appDefinedCategory);
        }
        funds = uniqueFunds.size();
        categories = uniqueCategories.size();
    }

    public BigDecimal getInvestment() {
        return investment;
    }

    public BigDecimal getValuation() {
        return valuation;
    }

    public int getFunds() {
        return funds;
    }

    public int getCategories() {
        return categories;
    }

    public BigDecimal getOverallPNL() {
        return (valuation.subtract(investment)).divide(investment, 4, BigDecimal.ROUND_HALF_UP);
    }
}
