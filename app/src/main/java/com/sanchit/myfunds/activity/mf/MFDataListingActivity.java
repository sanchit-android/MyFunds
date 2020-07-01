package com.sanchit.myfunds.activity.mf;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.adapter.mf.MFDataListingAdapter;
import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.model.mf.synopsis2.Synopsis2SummaryModel;
import com.sanchit.myfunds.model.mf.synopsis2.datalist.MFDataListModel;
import com.sanchit.myfunds.model.mf.synopsis2.datalist.MFDataListSummaryModel;
import com.sanchit.myfunds.model.mf.synopsis2.datalist.factory.AbstractFactory;
import com.sanchit.myfunds.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MFDataListingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MFTrade> trades;
    private Map<String, Map<String, BigDecimal[]>> priceMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mf_data_listing);

        String header = (String) getIntent().getSerializableExtra("header");
        trades = (List<MFTrade>) getIntent().getSerializableExtra("trades");
        priceMap = (Map<String, Map<String, BigDecimal[]>>) getIntent().getSerializableExtra("prices");

        recyclerView = (RecyclerView) findViewById(R.id.mf_data_listing_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        List<MFDataListModel> dataset = AbstractFactory.getFor(header, trades).generateData();
        mAdapter = new MFDataListingAdapter(this, dataset);
        recyclerView.setAdapter(mAdapter);

        MFDataListSummaryModel summaryModel = new MFDataListSummaryModel(trades, priceMap);
        setSummary(summaryModel);
    }

    private void setSummary(MFDataListSummaryModel summaryModel) {
        getViewById(R.id.mf_data_listing_summary_text1).setText(NumberUtils.formatMoney(summaryModel.getValuation()));
        getViewById(R.id.mf_data_listing_summary_text2A).setText(NumberUtils.toPercentage(summaryModel.getDayPNL(), 2));
        getViewById(R.id.mf_data_listing_summary_text2B).setText(NumberUtils.toPercentage(summaryModel.getUnrealizedPNL(), 2));
        getViewById(R.id.mf_data_listing_summary_text2C).setText(NumberUtils.toPercentage(summaryModel.getOverallPNL(), 2));
        getViewById(R.id.mf_data_listing_summary_text3).setText(NumberUtils.formatMoney(summaryModel.getInvestment()));
    }

    private TextView getViewById(int p) {
        return (TextView) findViewById(p);
    }
}
