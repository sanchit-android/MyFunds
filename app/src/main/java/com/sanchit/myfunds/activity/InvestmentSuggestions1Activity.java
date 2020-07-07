package com.sanchit.myfunds.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.sanchit.myfunds.R;
import com.sanchit.myfunds.adapter.mf.InvestmentSuggestions1Adapter;
import com.sanchit.myfunds.analyzer.mf.AppCategoryValuationAnalyzer;
import com.sanchit.myfunds.model.mf.Fund;
import com.sanchit.myfunds.model.mf.MFAnalysisModel1;
import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.model.mf.suggestion1.Suggestion1DataModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvestmentSuggestions1Activity extends AppCompatActivity {

    InvestmentSuggestions1Adapter adapter;

    RecyclerView recyclerViewPositive;
    RecyclerView recyclerViewNegative;
    RecyclerView recyclerViewNeutral;
    private XYPlot plot;

    private Map<String, Fund> funds;
    private List<MFTrade> trades;
    private Map<String, Map<String, BigDecimal[]>> priceMap;

    private List<MFAnalysisModel1> dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_suggestions1);

        funds = (Map<String, Fund>) getIntent().getSerializableExtra("funds");
        trades = (List<MFTrade>) getIntent().getSerializableExtra("trades");
        priceMap = (Map<String, Map<String, BigDecimal[]>>) getIntent().getSerializableExtra("prices");

        recyclerViewPositive = findViewById(R.id.suggestion1_recycler_view_positive);
        recyclerViewNegative = findViewById(R.id.suggestion1_recycler_view_negative);
        recyclerViewNeutral = findViewById(R.id.suggestion1_recycler_view_neutral);

        List<Suggestion1DataModel> data = new ArrayList<>();
        dummy(data);
        adapter = new InvestmentSuggestions1Adapter(this, data);

        recyclerViewPositive.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerViewNegative.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerViewNeutral.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        recyclerViewPositive.setAdapter(adapter);
        //recyclerViewNegative.setAdapter(adapter);
        //recyclerViewNeutral.setAdapter(adapter);

        generateScatter();
    }

    private void generateScatter() {
        plot = (XYPlot) findViewById(R.id.suggestion1_plot);
        dataSet = new AppCategoryValuationAnalyzer().generateDataset(trades);
        XYSeries series = generateScatterData("series1");
        //plot.setDomainBoundaries(0, 23, BoundaryMode.FIXED);
        //plot.setRangeBoundaries(-11, 25, BoundaryMode.FIXED);

        LineAndPointFormatter seriesFormat = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);
        //seriesFormat.setInterpolationParams(new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        plot.addSeries(series, seriesFormat);
        plot.getLegend().setVisible(false);

        // reduce the number of range labels
        plot.setLinesPerRangeLabel(1);
    }

    private XYSeries generateScatterData(String title) {
        SimpleXYSeries series = new SimpleXYSeries(title);
        for (MFAnalysisModel1 item : dataSet) {
            series.addLast(item.getContributionRaw().doubleValue(), item.getReturnsRaw().doubleValue());
        }
        return series;
    }

    private void dummy(List<Suggestion1DataModel> data) {
        for (int i = 0; i < 10; i++) {
            Suggestion1DataModel model = new Suggestion1DataModel();
            model.setText1("Buy or Sell or Hold");
            data.add(model);
        }
    }
}
