package com.sanchit.myfunds.model.mf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Fund implements Serializable {
    public String fundID;
    public String fundName;
    public String fundHouse;
    public boolean direct;
    public boolean growth;
    public String schemeType; // Open or Closed Ended
    public String fundCategory;
    public String fundSubCategory;

    public String appDefinedCategory;

    private String mfAPIID;

    private String mfAPI = "https://api.mfapi.in/mf/";

    public String getMfAPI() {
        return mfAPI;
    }

    public String getMfAPIID() {
        return mfAPIID;
    }

    public void setMfAPIID(String mfAPIID) {
        this.mfAPIID = mfAPIID;
        this.mfAPI += mfAPIID;
    }

    public String getFundID() {
        return fundID;
    }

    private List<MFTrade> trades = new ArrayList<>();

    public List<MFTrade> getTrades() {
        return trades;
    }
}
