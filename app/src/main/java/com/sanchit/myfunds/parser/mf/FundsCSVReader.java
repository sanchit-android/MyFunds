package com.sanchit.myfunds.parser.mf;

import android.app.Activity;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.model.mf.Fund;
import com.sanchit.myfunds.model.mf.MFTrade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FundsCSVReader {

    Activity activity;

    public FundsCSVReader(Activity activity) {
        this.activity = activity;
    }

    public List<Fund> read() {
        List<Fund> resultList = new ArrayList<>();

        InputStream inputStream = activity.getResources().openRawResource(R.raw.mutual_funds);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                Fund fund = new Fund();
                fund.fundID = row[0];
                fund.fundHouse = row[1];
                fund.fundName = row[2];
                fund.direct = "Direct".equals(row[3]);
                fund.growth = "Growth".equals(row[4]);
                fund.schemeType = row[5];
                fund.fundCategory = row[6];
                fund.fundSubCategory = row[7];
                fund.setMfAPIID(row[8]);
                fund.appDefinedCategory = row[9];
                resultList.add(fund);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }

}
