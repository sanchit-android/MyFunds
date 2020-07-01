package com.sanchit.myfunds.activity.mf;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.adapter.mf.MFTradesAdapter;
import com.sanchit.myfunds.model.mf.Fund;
import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.model.mf.header.MFTradeHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MFPositionsActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static List<MFTrade> data;
    private static List<MFTrade> originalData = new ArrayList<>();
    private static Map<String, Fund> funds;
    private RecyclerView.LayoutManager layoutManager;
    private String filterType;
    private String filterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mf_positions);
        getSupportActionBar().setTitle("Trades");

        funds = (Map<String, Fund>) getIntent().getSerializableExtra("funds");
        data = (List<MFTrade>) getIntent().getSerializableExtra("trades");

        filterType = (String) getIntent().getSerializableExtra("filterType");
        filterValue = (String) getIntent().getSerializableExtra("filterValue");

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        originalData.addAll(data);

        data = filterData();

        data.add(0, new MFTradeHeader());

        adapter = new MFTradesAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

    private List<MFTrade> filterData() {
        if (filterType == null || filterType.trim().equals("") || filterValue == null || filterValue.trim().equals("")) {
            return data;
        }

        List<MFTrade> filtered = new ArrayList<>();

        for (MFTrade item : data) {
            if ("category".equals(filterType)) {
                if (item.fund.appDefinedCategory.equals(filterValue)) {
                    filtered.add(item);
                }
            } else {
                filtered.add(item);
            }
        }

        return filtered;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mf_trade_add, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) menu.findItem(R.id.mf_trade_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().equals("")) {
                    data.clear();
                    data.addAll(originalData);
                    //adapter = new MFTradesAdapter(data);
                    recyclerView.invalidate();
                    adapter.notifyDataSetChanged();
                }
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();

                data.clear();
                for (MFTrade trade : originalData) {
                    if (trade.fund.fundName.toLowerCase().contains(query.toLowerCase())) {
                        data.add(trade);
                    }
                }
                recyclerView.invalidate();
                adapter.notifyDataSetChanged();

                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }

    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_add_trade:
                Intent intent = new Intent(getBaseContext(), MFTradeAddActivity.class);
                intent.putExtra("funds", (HashMap) funds);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
