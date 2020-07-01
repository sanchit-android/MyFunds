package com.sanchit.myfunds.model.mf.synopsis2;

import com.sanchit.myfunds.model.mf.MFTrade;

public interface AnalysisKeySourcer {
    AnalysisKeySourcer APP_DEFINED_CATEGORY_KEY = x -> x.fund.appDefinedCategory;
    AnalysisKeySourcer FUND_NAME_KEY = x -> x.fund.fundName;

    String fetch(MFTrade trade);
}
