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
import java.util.Map;

public class MFTradesCSVReader {

    private final Map<String, Fund> funds;
    Activity activity;

    public MFTradesCSVReader(Activity activity, Map<String, Fund> funds) {
        this.activity = activity;
        this.funds = funds;
    }

    public List<MFTrade> read() {
        List<MFTrade> resultList = new ArrayList<>();

        InputStream inputStream = activity.getResources().openRawResource(R.raw.mf_trades);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                MFTrade trade = new MFTrade();
                trade.unitsAlloted = new BigDecimal(row[3]);
                trade.transactionPrice = new BigDecimal(row[4]);
                trade.fund = funds == null ? new Fund() : funds.get(row[0]);
                trade.setTransactionDate(row[1]);

                resultList.add(trade);
                trade.fund.getTrades().add(trade);
            }
        } catch (IOException | ParseException ex) {
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
