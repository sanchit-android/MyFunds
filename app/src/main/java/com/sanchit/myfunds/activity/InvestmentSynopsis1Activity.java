package com.sanchit.myfunds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.activity.mf.MFPositionsActivity;
import com.sanchit.myfunds.model.mf.Fund;
import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvestmentSynopsis1Activity extends AppCompatActivity {

    private static final int SCALE_DISPLAY = 0;
    private static final Integer[] BY_INV_CAT_VIEWS = new Integer[]{
            R.id.synopsis_box_least_invested_category_name,
            R.id.synopsis_box_least_invested_category_inv,
            R.id.synopsis_box_least_invested_category_funds_no,
            R.id.synopsis_box_largest_category_name,
            R.id.synopsis_box_largest_category_inv,
            R.id.synopsis_box_largest_category_funds_no
    };
    private static final Integer[] BY_PRFT_CAT_VIEWS = new Integer[]{
            R.id.synopsis_box_least_performing_category_name,
            R.id.synopsis_box_least_performing_category_invested,
            R.id.synopsis_box_least_performing_category_funds_no,
            R.id.synopsis_box_top_profit_category_name,
            R.id.synopsis_box_top_profit_category_invested,
            R.id.synopsis_box_top_profit_category_funds_no
    };

    private Map<String, Fund> funds;
    private List<MFTrade> trades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_investment_synopsis1);

        funds = (Map<String, Fund>) getIntent().getSerializableExtra("funds");
        trades = (List<MFTrade>) getIntent().getSerializableExtra("trades");

        setTopSynopsis();
        setTopInvestedAMC();
        analyzeByCategory((AnalysisKeySourcer) (x -> x.fund.appDefinedCategory), BY_INV_CAT_VIEWS, BY_PRFT_CAT_VIEWS);
    }

    private void analyzeByCategory(AnalysisKeySourcer analysisKeySourcer, Integer[] byInvViews, Integer[] byProfitViews) {
        Map<String, BigDecimal> valuationMap = new HashMap<>();
        Map<String, BigDecimal> investedMap = new HashMap<>();
        Map<String, BigDecimal> profitMap = new HashMap<>();
        Map<String, Set<String>> keyToFundMap = new HashMap<>();

        for (MFTrade trade : trades) {
            String key = analysisKeySourcer.fetch(trade);

            if (!investedMap.containsKey(key)) {
                investedMap.put(key, new BigDecimal(0));
            }
            BigDecimal invested = investedMap.get(key).add(trade.unitsAlloted.multiply(trade.transactionPrice).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN));
            investedMap.put(key, invested);

            if (!valuationMap.containsKey(key)) {
                valuationMap.put(key, new BigDecimal(0));
            }
            BigDecimal valuation = valuationMap.get(key).add(trade.unitsAlloted.multiply(trade.currentPrice).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN));
            valuationMap.put(key, valuation);

            if (!keyToFundMap.containsKey(key)) {
                keyToFundMap.put(key, new HashSet<>());
            }
            keyToFundMap.get(key).add(trade.fund.fundName);

            if (!profitMap.containsKey(key)) {
                profitMap.put(key, new BigDecimal(0));
            }
            BigDecimal profit = valuationMap.get(key).subtract(investedMap.get(key));
            profitMap.put(key, profit.divide(investedMap.get(key), 4, BigDecimal.ROUND_HALF_DOWN));
        }

        String bottomDataLabel = null;
        BigDecimal bottomData = null;

        String topDataLabel = null;
        BigDecimal topData = null;

        for (Map.Entry<String, BigDecimal> e : investedMap.entrySet()) {
            if (bottomData == null || e.getValue().compareTo(bottomData) < 0) {
                bottomData = e.getValue();
                bottomDataLabel = e.getKey();
            }

            if (topData == null || e.getValue().compareTo(topData) > 0) {
                topData = e.getValue();
                topDataLabel = e.getKey();
            }
        }

        ((TextView) findViewById(byInvViews[0])).setText(String.valueOf(bottomDataLabel));
        ((TextView) findViewById(byInvViews[1])).setText(String.valueOf(bottomData));
        ((TextView) findViewById(byInvViews[2])).setText(String.valueOf(keyToFundMap.get(bottomDataLabel).size()));

        ((TextView) findViewById(byInvViews[3])).setText(String.valueOf(topDataLabel));
        ((TextView) findViewById(byInvViews[4])).setText(String.valueOf(topData));
        ((TextView) findViewById(byInvViews[5])).setText(String.valueOf(keyToFundMap.get(topDataLabel).size()));

        bottomDataLabel = null;
        bottomData = null;

        topDataLabel = null;
        topData = null;

        for (Map.Entry<String, BigDecimal> e : profitMap.entrySet()) {
            if (bottomData == null || e.getValue().compareTo(bottomData) < 0) {
                bottomData = e.getValue();
                bottomDataLabel = e.getKey();
            }

            if (topData == null || e.getValue().compareTo(topData) > 0) {
                topData = e.getValue();
                topDataLabel = e.getKey();
            }
        }

        ((TextView) findViewById(byProfitViews[0])).setText(String.valueOf(bottomDataLabel));
        ((TextView) findViewById(byProfitViews[1])).setText(investedMap.get(bottomDataLabel) + " - " + toPercentage(bottomData));
        ((TextView) findViewById(byProfitViews[2])).setText(String.valueOf(keyToFundMap.get(bottomDataLabel).size()));

        ((TextView) findViewById(byProfitViews[3])).setText(String.valueOf(topDataLabel));
        ((TextView) findViewById(byProfitViews[4])).setText(investedMap.get(topDataLabel) + " - " + toPercentage(topData));
        ((TextView) findViewById(byProfitViews[5])).setText(String.valueOf(keyToFundMap.get(topDataLabel).size()));
    }

    private String toPercentage(BigDecimal val) {
        BigDecimal percentage = val.multiply(new BigDecimal(100.00)).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN);
        if (val.compareTo(BigDecimal.ZERO) >= 0) {
            return percentage.toPlainString() + "%";
        } else {
            return "(" + percentage.toPlainString() + ")%";
        }
    }

    private void setTopInvestedAMC() {
        Map<String, BigDecimal> valuationMap = new HashMap<>();
        String maxFundHouse = null;
        BigDecimal maxFundInvested = null;
        for (MFTrade trade : trades) {
            if (!valuationMap.containsKey(trade.fund.fundHouse)) {
                valuationMap.put(trade.fund.fundHouse, new BigDecimal(0));
            }
            BigDecimal newValue = valuationMap.get(trade.fund.fundHouse).add(trade.unitsAlloted.multiply(trade.transactionPrice).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN));

            if (maxFundInvested == null || newValue.compareTo(maxFundInvested) > 0) {
                maxFundInvested = newValue;
                maxFundHouse = trade.fund.fundHouse;
            }

            valuationMap.put(trade.fund.fundHouse, newValue);
        }

        ((TextView) findViewById(R.id.synopsis_box_biggest_fund_house_name)).setText(String.valueOf(maxFundHouse));
        ((TextView) findViewById(R.id.synopsis_box_biggest_fund_house_inv)).setText(String.valueOf(maxFundInvested));

        String leastFundHouse = null;
        BigDecimal leastFundInvested = null;

        for (Map.Entry<String, BigDecimal> e : valuationMap.entrySet()) {
            if (leastFundInvested == null || e.getValue().compareTo(leastFundInvested) < 0) {
                leastFundInvested = e.getValue();
                leastFundHouse = e.getKey();
            }
        }

        ((TextView) findViewById(R.id.synopsis_box_smallest_fund_house_name)).setText(String.valueOf(leastFundHouse));
        ((TextView) findViewById(R.id.synopsis_box_smallest_fund_house_inv)).setText(String.valueOf(leastFundInvested));
    }

    private void setTopSynopsis() {
        Set<String> funds = new HashSet<>();
        BigDecimal invested = BigDecimal.ZERO;
        BigDecimal valuation = BigDecimal.ZERO;

        for (MFTrade trade : trades) {
            funds.add(trade.fund.fundName);
            invested = invested.add(scale(trade.unitsAlloted.multiply(trade.transactionPrice)));
            valuation = valuation.add(scale(trade.unitsAlloted.multiply(trade.currentPrice)));
        }

        ((TextView) findViewById(R.id.synopsis_box1_funds_count)).setText(String.valueOf(funds.size()));
        ((TextView) findViewById(R.id.synopsis_box1_invested)).setText(NumberUtils.formatMoney(invested));
        ((TextView) findViewById(R.id.synopsis_box1_valuation)).setText(NumberUtils.formatMoney(valuation));
    }

    private BigDecimal scale(BigDecimal bd) {
        return bd.setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN);
    }

    public void onClickTopPerformingCategory(View view) {
        String category = ((TextView) findViewById(R.id.synopsis_box_top_profit_category_name)).getText().toString();

        Intent intent = new Intent(getBaseContext(), MFPositionsActivity.class);
        intent.putExtra("funds", (HashMap) funds);
        intent.putExtra("trades", (ArrayList) trades);
        intent.putExtra("filterType", "category");
        intent.putExtra("filterValue", category);
        startActivity(intent);
    }

    interface AnalysisKeySourcer {
        public String fetch(MFTrade trade);
    }
}
