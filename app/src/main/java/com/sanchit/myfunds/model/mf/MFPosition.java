package com.sanchit.myfunds.model.mf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MFPosition {

    Fund fund;
    BigDecimal Quantity;
    List<MFTrade> trades = new ArrayList<>();

    public MFPosition() {
    }

    public MFPosition(Fund fund, BigDecimal quantity, List<MFTrade> trades) {
        this.fund = fund;
        Quantity = quantity;
        this.trades = trades;
    }

    public MFPosition(Fund fund, BigDecimal quantity) {
        this.fund = fund;
        Quantity = quantity;
    }

    public MFPosition(String fundName, BigDecimal quantity) {
        this.fund = new Fund();
        fund.fundName = fundName;
        Quantity = quantity;
    }

    public String getFundName() {
        return fund.fundName;
    }

    public BigDecimal getQuantity() {
        return Quantity;
    }

    public List<MFTrade> getTrades() {
        return trades;
    }
}
