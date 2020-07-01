package com.sanchit.myfunds.activity.mf;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.adapter.mf.MFAnalysis1Adapter;
import com.sanchit.myfunds.model.mf.Fund;
import com.sanchit.myfunds.model.mf.MFPosition;
import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.parser.mf.MFTradesCSVReader;

import java.util.List;
import java.util.Map;

public class MFAnalysis1Activity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static List<MFTrade> trades;
    private static Map<String, Fund> funds;
    private static List<MFPosition> positions;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mf_analysis1);

        funds = (Map<String, Fund>) getIntent().getSerializableExtra("funds");

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        trades = new MFTradesCSVReader(this, funds).read();

        adapter = new MFAnalysis1Adapter(trades);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
