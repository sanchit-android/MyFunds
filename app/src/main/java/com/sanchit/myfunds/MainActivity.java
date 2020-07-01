package com.sanchit.myfunds;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.myfunds.activity.InvestmentSuggestions1Activity;
import com.sanchit.myfunds.activity.InvestmentSynopsis1Activity;
import com.sanchit.myfunds.activity.InvestmentSynopsis2Activity;
import com.sanchit.myfunds.activity.mf.MFAnalysis1Activity;
import com.sanchit.myfunds.activity.mf.MFPositionsActivity;
import com.sanchit.myfunds.activity.mf.MFTradeAddActivity;
import com.sanchit.myfunds.enricher.mf.MFNAVEnricher;
import com.sanchit.myfunds.model.mf.Fund;
import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.parser.mf.FundsCSVReader;
import com.sanchit.myfunds.parser.mf.MFTradesCSVReader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String _ERROR = "ERR";
    private List<MFTrade> trades;
    private Map<String, Fund> fundMapByID;
    private Map<String, Map<String, BigDecimal[]>> priceMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Fund> funds = getListOfSupportedFunds();
        fundMapByID = generateFundMapByID(funds);

        trades = new MFTradesCSVReader(this, fundMapByID).read();
        loadPrices(funds);
    }

    private void loadPrices(List<Fund> funds) {
        for (final Fund fund : funds) {
            AsyncTask.execute(() -> new MFNAVEnricher().enrich(
                    fund.getTrades(),
                    (model, trade, value) -> {
                        trade.setPrices(value);
                        if (!priceMap.containsKey(trade.fund.fundID)) {
                            priceMap.put(trade.fund.fundID, value);
                        }
                    }
            ));
        }
    }

    private Map<String, Fund> generateFundMapByID(List<Fund> funds) {
        Map<String, Fund> result = new HashMap<>();
        for (Fund fund : funds) {
            result.put(fund.fundID, fund);
        }
        return result;
    }

    private List<Fund> getListOfSupportedFunds() {
        FundsCSVReader reader = new FundsCSVReader(this);
        return reader.read();
    }

    public void onClickGotoPositionButton(View view) {
        Intent intent = new Intent(getBaseContext(), MFPositionsActivity.class);
        intent.putExtra("funds", (HashMap) fundMapByID);
        intent.putExtra("trades", (ArrayList) trades);
        startActivity(intent);
    }

    public void onClickGotoPositionTrial2Button(View view) {
        Intent intent = new Intent(getBaseContext(), MFAnalysis1Activity.class);
        intent.putExtra("funds", (HashMap) fundMapByID);
        startActivity(intent);
    }

    public void onClickGotoAddTradeTrial3Button(View view) {
        Intent intent = new Intent(getBaseContext(), MFTradeAddActivity.class);
        intent.putExtra("funds", (HashMap) fundMapByID);
        startActivity(intent);
    }

    public void onClickGotoSynopsisTrial4Button(View view) {
        if (!dataLoaded()) {
            Toast.makeText(this, "Data Loading in Progress..", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getBaseContext(), InvestmentSynopsis1Activity.class);
        intent.putExtra("funds", (HashMap) fundMapByID);
        intent.putExtra("trades", (ArrayList) trades);
        startActivity(intent);
    }

    private boolean dataLoaded() {
        for (MFTrade trade : trades) {
            if (trade.currentPrice == null) {
                return false;
            }
        }
        return true;
    }

    public void onClickGotoSynopsis2(View view) {
        if (!dataLoaded()) {
            Toast.makeText(this, "Data Loading in Progress..", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getBaseContext(), InvestmentSynopsis2Activity.class);
        intent.putExtra("funds", (HashMap) fundMapByID);
        intent.putExtra("trades", (ArrayList) trades);
        intent.putExtra("prices", (HashMap) priceMap);
        startActivity(intent);
    }

    public void onClickGotoSuggestion1(View view) {
        if (!dataLoaded()) {
            Toast.makeText(this, "Data Loading in Progress..", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getBaseContext(), InvestmentSuggestions1Activity.class);
        intent.putExtra("funds", (HashMap) fundMapByID);
        intent.putExtra("trades", (ArrayList) trades);
        intent.putExtra("prices", (HashMap) priceMap);
        startActivity(intent);
    }
}
