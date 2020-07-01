package com.sanchit.myfunds.model.mf.header;

import com.sanchit.myfunds.model.mf.MFAnalysisModel1;

public class MFAnalysisModel1Header extends MFAnalysisModel1 {

    public String getValuation() {
        return "Valuation";
    }

    public String getInvestment() {
        return "Investment";
    }

    public String getUnits() {
        return "Units Held";
    }

    public String getCategory() {
        return "Fund Category";
    }

    public String getPercentage() {
        return "% Allocation";
    }

    @Override
    public String getReturns() {
        return "% Returns";
    }
}
