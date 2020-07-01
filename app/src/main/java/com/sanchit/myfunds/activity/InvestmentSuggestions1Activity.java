package com.sanchit.myfunds.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.adapter.mf.InvestmentSuggestions1Adapter;
import com.sanchit.myfunds.model.mf.suggestion1.Suggestion1DataModel;

import java.util.ArrayList;
import java.util.List;

public class InvestmentSuggestions1Activity extends AppCompatActivity {

    InvestmentSuggestions1Adapter adapter;

    RecyclerView recyclerViewPositive;
    RecyclerView recyclerViewNegative;
    RecyclerView recyclerViewNeutral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_suggestions1);

        recyclerViewPositive = findViewById(R.id.suggestion1_recycler_view_positive);
        recyclerViewNegative = findViewById(R.id.suggestion1_recycler_view_negative);
        recyclerViewNeutral = findViewById(R.id.suggestion1_recycler_view_neutral);

        List<Suggestion1DataModel> data = new ArrayList<>();
        dummy(data);
        adapter = new InvestmentSuggestions1Adapter(this, data);

        recyclerViewPositive.setAdapter(adapter);
        //recyclerViewNegative.setAdapter(adapter);
        //recyclerViewNeutral.setAdapter(adapter);
    }

    private void dummy(List<Suggestion1DataModel> data) {
        for (int i = 0; i < 10; i++) {
            Suggestion1DataModel model = new Suggestion1DataModel();
            model.setText1("Buy or Sell or Hold");
            data.add(model);
        }
    }
}
