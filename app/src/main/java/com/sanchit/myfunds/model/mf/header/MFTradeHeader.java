package com.sanchit.myfunds.model.mf.header;

import com.sanchit.myfunds.model.mf.MFTrade;

import java.math.BigDecimal;
import java.util.Date;

public class MFTradeHeader extends MFTrade {

    @Override
    public String getTransactionDate() {
        return "Date";
    }

    @Override
    public String getValuation() {
        return "Valuation";
    }

    @Override
    public String getUnitsAlloted() {
        return "Units";
    }

    @Override
    public String getTransactionPrice() {
        return "Price";
    }

    @Override
    public String getInvestment() {
        return "Investment";
    }

    @Override
    public BigDecimal getInvestmentValue() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getValuationValue() {
        return BigDecimal.ZERO;
    }

    @Override
    public String getTransactionType() {
        return "Side";
    }

    @Override
    public boolean isHeader() {
        return true;
    }
}
