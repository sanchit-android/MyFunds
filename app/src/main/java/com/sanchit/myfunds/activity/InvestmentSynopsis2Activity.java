package com.sanchit.myfunds.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.adapter.mf.InvestmentSynopsis2Adapter;
import com.sanchit.myfunds.model.mf.Fund;
import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.model.mf.synopsis2.AnalysisKeySourcer;
import com.sanchit.myfunds.model.mf.synopsis2.InvestmentBasedModel;
import com.sanchit.myfunds.model.mf.synopsis2.PerformanceBasedModel;
import com.sanchit.myfunds.model.mf.synopsis2.RandomDurationPerfModel;
import com.sanchit.myfunds.model.mf.synopsis2.Synopsis2DataModel;
import com.sanchit.myfunds.model.mf.synopsis2.Synopsis2SummaryModel;
import com.sanchit.myfunds.utils.Constants;
import com.sanchit.myfunds.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvestmentSynopsis2Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Map<String, Fund> funds;
    private List<MFTrade> trades;
    private Map<String, Map<String, BigDecimal[]>> priceMap;

    public List<MFTrade> getTrades() {
        return trades;
    }

    public Map<String, Map<String, BigDecimal[]>> getPriceMap() {
        return priceMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_investment_synopsis2);

        funds = (Map<String, Fund>) getIntent().getSerializableExtra("funds");
        trades = (List<MFTrade>) getIntent().getSerializableExtra("trades");
        priceMap = (Map<String, Map<String, BigDecimal[]>>) getIntent().getSerializableExtra("prices");

        recyclerView = (RecyclerView) findViewById(R.id.synopsis2_my_recycler_view);

        // use a linear layout manager
        //layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        List<Synopsis2DataModel> dataset = new ArrayList<>();
        generate(dataset);
        mAdapter = new InvestmentSynopsis2Adapter(this, dataset);
        recyclerView.setAdapter(mAdapter);

        Synopsis2SummaryModel summaryModel = new Synopsis2SummaryModel(trades, priceMap);
        setSummary(summaryModel);
    }

    private void setSummary(Synopsis2SummaryModel summaryModel) {
        getViewById(R.id.mf_synopsis2_summary_text1).setText(NumberUtils.formatMoney(summaryModel.getValuation()));
        getViewById(R.id.mf_synopsis2_summary_text2).setText(String.valueOf(summaryModel.getCategories()));
        getViewById(R.id.mf_synopsis2_summary_text3).setText(String.valueOf(summaryModel.getFunds()));
        getViewById(R.id.mf_synopsis2_summary_text4).setText(NumberUtils.toPercentage(summaryModel.getOverallPNL(), 2));
        getViewById(R.id.mf_synopsis2_summary_text5).setText(NumberUtils.formatMoney(summaryModel.getInvestment()));
    }

    private TextView getViewById(int p) {
        return (TextView) findViewById(p);
    }

    private void generate(List<Synopsis2DataModel> dataset) {
        dataset.add(new PerformanceBasedModel("TOP PERFORMING CATEGORY", R.color.positive_1, trades, AnalysisKeySourcer.APP_DEFINED_CATEGORY_KEY, Constants.INCREASING_COMPARATOR));
        dataset.add(new PerformanceBasedModel("LEAST PERFORMING CATEGORY", R.color.negative_1, trades, AnalysisKeySourcer.APP_DEFINED_CATEGORY_KEY, Constants.DECREASING_COMPARATOR));
        dataset.add(new PerformanceBasedModel("TOP PERFORMING FUND", R.color.positive_2, trades, AnalysisKeySourcer.FUND_NAME_KEY, Constants.INCREASING_COMPARATOR));
        dataset.add(new PerformanceBasedModel("LEAST PERFORMING FUND", R.color.negative_2, trades, AnalysisKeySourcer.FUND_NAME_KEY, Constants.DECREASING_COMPARATOR));
        dataset.add(new InvestmentBasedModel("TOP INVESTED CATEGORY", R.color.positive_3, trades, AnalysisKeySourcer.APP_DEFINED_CATEGORY_KEY, Constants.INCREASING_COMPARATOR));
        dataset.add(new InvestmentBasedModel("LEAST INVESTED CATEGORY", R.color.negative_3, trades, AnalysisKeySourcer.APP_DEFINED_CATEGORY_KEY, Constants.DECREASING_COMPARATOR));
        dataset.add(new InvestmentBasedModel("TOP INVESTED FUND", R.color.positive_4, trades, AnalysisKeySourcer.FUND_NAME_KEY, Constants.INCREASING_COMPARATOR));
        dataset.add(new InvestmentBasedModel("LEAST INVESTED FUND", R.color.negative_4, trades, AnalysisKeySourcer.FUND_NAME_KEY, Constants.DECREASING_COMPARATOR));
        dataset.add(new RandomDurationPerfModel("TOP FUND - 10 DAYS", R.color.positive_3, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.INCREASING_COMPARATOR, Constants.Duration.T_10));
        dataset.add(new RandomDurationPerfModel("BOTTOM FUND - 10 DAYS", R.color.negative_3, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.DECREASING_COMPARATOR, Constants.Duration.T_10));
        dataset.add(new RandomDurationPerfModel("TOP FUND - 1 MONTH", R.color.positive_2, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.INCREASING_COMPARATOR, Constants.Duration.T_30));
        dataset.add(new RandomDurationPerfModel("BOTTOM FUND - 1 MONTH", R.color.negative_2, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.DECREASING_COMPARATOR, Constants.Duration.T_30));
        dataset.add(new RandomDurationPerfModel("TOP FUND - 3 MONTHS", R.color.positive_1, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.INCREASING_COMPARATOR, Constants.Duration.T_90));
        dataset.add(new RandomDurationPerfModel("BOTTOM FUND - 3 MONTHS", R.color.negative_1, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.DECREASING_COMPARATOR, Constants.Duration.T_90));
        dataset.add(new RandomDurationPerfModel("TOP FUND - 6 MONTHS", R.color.positive_1, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.INCREASING_COMPARATOR, Constants.Duration.T_180));
        dataset.add(new RandomDurationPerfModel("BOTTOM FUND - 6 MONTHS", R.color.negative_1, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.DECREASING_COMPARATOR, Constants.Duration.T_180));
        dataset.add(new RandomDurationPerfModel("TOP FUND - 1 YEAR", R.color.positive_2, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.INCREASING_COMPARATOR, Constants.Duration.T_365));
        dataset.add(new RandomDurationPerfModel("BOTTOM FUND - 1 YEAR", R.color.negative_2, trades, priceMap, AnalysisKeySourcer.FUND_NAME_KEY, Constants.DECREASING_COMPARATOR, Constants.Duration.T_365));
    }
}
