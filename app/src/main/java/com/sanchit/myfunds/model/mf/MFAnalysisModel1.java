package com.sanchit.myfunds.model.mf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MFAnalysisModel1 {

    public String Category;
    public BigDecimal valuation;
    public BigDecimal investment;
    public String percentage;
    public BigDecimal units;

    public String returns;
    public boolean updated = false;

    List<MFTrade> trades = new ArrayList<>();
    List<MFPosition> positions = new ArrayList<>();

    public MFAnalysisModel1() {
        this.valuation = new BigDecimal(0);
        this.investment = new BigDecimal(0);
        units = new BigDecimal(0);
    }

    public String getValuation() {
        return valuation.toPlainString();
    }

    public String getInvestment() {
        return investment.toPlainString();
    }

    public String getUnits() {
        return units.toPlainString();
    }

    public String getCategory() {
        return Category;
    }

    public String getPercentage() {
        return percentage;
    }

    public List<MFTrade> getTrades() {
        return trades;
    }

    public List<MFPosition> getPositions() {
        return positions;
    }

    public String getReturns() {
        return returns;
    }
}
