package com.sanchit.myfunds.model.mf.synopsis2;

import com.sanchit.myfunds.model.mf.MFTrade;

import java.util.List;

public abstract class Synopsis2DataModel {
    protected static final int SCALE_DISPLAY = 0;
    protected String name;
    protected String data;
    protected List<MFTrade> trades;
    private String header;
    private int cardColor;

    public Synopsis2DataModel(String header, int cardColor, List<MFTrade> trades) {
        this.header = header;
        this.cardColor = cardColor;
        this.trades = trades;
    }

    public Synopsis2DataModel() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getCardColor() {
        return cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
