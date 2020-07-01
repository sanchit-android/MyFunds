package com.sanchit.myfunds.model.mf;

import com.sanchit.myfunds.utils.Constants;
import com.sanchit.myfunds.utils.DateUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MFTrade implements Serializable {
    private static final int SCALE_DISPLAY = 0;

    public Date transactionDate;
    public BigDecimal unitsAlloted;
    public BigDecimal transactionPrice;
    public BigDecimal currentPrice;

    public boolean isBuy = true;

    public Fund fund;

    public MFTrade(Date transactionDate, BigDecimal unitsAlloted, BigDecimal transactionPrice, Fund fund) {
        this.transactionDate = transactionDate;
        this.unitsAlloted = unitsAlloted;
        this.transactionPrice = transactionPrice;
        this.fund = fund;
    }

    public MFTrade(Date transactionDate, BigDecimal unitsAlloted, BigDecimal transactionPrice, String fundName) {
        this.transactionDate = transactionDate;
        this.unitsAlloted = unitsAlloted;
        this.transactionPrice = transactionPrice;
        this.fund = new Fund();
        fund.fundName = fundName;
    }

    public MFTrade(String transactionDate, BigDecimal unitsAlloted, BigDecimal transactionPrice, String fundName) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        Date date = formatter.parse(transactionDate);
        this.transactionDate = date;
        this.unitsAlloted = unitsAlloted;
        this.transactionPrice = transactionPrice;
        this.fund = new Fund();
        fund.fundName = fundName;
    }

    public MFTrade() {
    }

    public String getTransactionDate() {
        return DateUtils.formatDate(transactionDate);
    }

    public void setTransactionDate(String transactionDate) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        Date date = formatter.parse(transactionDate);
        this.transactionDate = date;
    }

    public String getTransactionType() {
        return isBuy ? "Buy" : "Sell";
    }

    public String getUnitsAlloted() {
        return unitsAlloted.toPlainString();
    }

    public String getTransactionPrice() {
        return transactionPrice.toPlainString();
    }

    public String getCurrentPrice() {
        return currentPrice.toPlainString();
    }

    public Fund getFund() {
        return fund;
    }

    public String getValuation() {
        return getValuationValue().toPlainString();
    }

    public BigDecimal getValuationValue() {
        if (currentPrice == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal value = unitsAlloted.multiply(currentPrice);
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN);
    }

    public String getInvestment() {
        return getInvestmentValue().toPlainString();
    }

    public BigDecimal getInvestmentValue() {
        return unitsAlloted.multiply(transactionPrice).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN);
    }

    public boolean isHeader() {
        return false;
    }

    public void setPrices(Map<String, BigDecimal[]> value) {
        currentPrice = value.get(Constants.Duration.T)[1];
    }
}
