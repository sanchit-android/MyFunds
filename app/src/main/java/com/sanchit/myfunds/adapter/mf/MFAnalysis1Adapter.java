package com.sanchit.myfunds.adapter.mf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.analyzer.mf.AppCategoryValuationAnalyzer;
import com.sanchit.myfunds.enricher.mf.MFNAVEnricher;
import com.sanchit.myfunds.model.mf.MFAnalysisModel1;
import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.model.mf.header.MFAnalysisModel1Header;
import com.sanchit.myfunds.utils.Constants;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MFAnalysis1Adapter extends RecyclerView.Adapter<MFAnalysis1Adapter.ViewHolder> {

    private List<MFTrade> trades;
    private List<MFAnalysisModel1> dataSet;

    private ViewGroup parent;

    private boolean sortAsc = true;

    public MFAnalysis1Adapter(List<MFTrade> data) {
        this.trades = data;
        dataSet = new AppCategoryValuationAnalyzer().generateDataset(trades);
        appendHeader(dataSet);
    }

    private void appendHeader(List<MFAnalysisModel1> dataSet) {
        dataSet.add(0, new MFAnalysisModel1Header());
    }

    @Override
    public MFAnalysis1Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        this.parent = parent;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_mf_position, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        MFAnalysis1Adapter.ViewHolder myViewHolder = new MFAnalysis1Adapter.ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MFAnalysis1Adapter.ViewHolder holder, final int listPosition) {

        TextView textViewCategoryName = holder.textViewCategoryName;
        final TextView textViewCategoryValuation = holder.textViewCategoryValuation;
        TextView textViewCategoryPerc = holder.textViewCategoryPerc;
        TextView textViewCategoryInvestment = holder.textViewCategoryInvestment;
        final TextView textViewCategoryReturns = holder.textViewCategoryReturns;

        MFAnalysisModel1 data = dataSet.get(listPosition);

        textViewCategoryName.setText(data.getCategory());
        textViewCategoryValuation.setText(data.getValuation());
        textViewCategoryPerc.setText(data.getContribution());
        textViewCategoryInvestment.setText(data.getInvestment());
        textViewCategoryReturns.setText(data.getReturns());

        if (data.updated) {
            return;
        }

        Map<String, BigDecimal> latestNAVMap = new HashMap<>();
        new MFNAVEnricher().enrich(data, (model, trade, priceMap) -> {
            BigDecimal value = priceMap.get(Constants.Duration.T)[1];

            BigDecimal currentValue = new BigDecimal(textViewCategoryValuation.getText().toString());
            currentValue = currentValue == null ? new BigDecimal(0) : currentValue;
            BigDecimal newValue = currentValue.add(trade.unitsAlloted.multiply(value).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            textViewCategoryValuation.setText(newValue.toPlainString());

            BigDecimal absoluteReturns = newValue.subtract(((MFAnalysisModel1) model).investment);
            String returnPercentage = absoluteReturns.multiply(new BigDecimal(100)).divide(((MFAnalysisModel1) model).investment, 2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%";
            textViewCategoryReturns.setText(returnPercentage);

            trade.currentPrice = value;
        });
        highlightHeader(holder, listPosition);
    }

    private void highlightHeader(ViewHolder holder, int listPosition) {
        if (listPosition != 0) {
            return;
        }

        TextView textViewCategoryName = holder.textViewCategoryName;
        TextView textViewCategoryValuation = holder.textViewCategoryValuation;
        TextView textViewCategoryPerc = holder.textViewCategoryPerc;
        TextView textViewCategoryInvestment = holder.textViewCategoryInvestment;
        TextView textViewCategoryReturns = holder.textViewCategoryReturns;

        setColor(textViewCategoryName);
        setColor(textViewCategoryValuation);
        setColor(textViewCategoryPerc);
        setColor(textViewCategoryInvestment);
        setColor(textViewCategoryReturns);

        CardView cardView = (CardView) textViewCategoryName.getParent().getParent().getParent();
        cardView.setBackgroundColor(parent.getResources().getColor(R.color.colorPrimaryDark));

        textViewCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void setColor(TextView textView) {
        textView.setTextColor(parent.getResources().getColor(R.color.colorPrimary));
    }

    void update() {
        MFAnalysisModel1 header = dataSet.remove(0);
        Collections.sort(dataSet, new Comparator<MFAnalysisModel1>() {
            @Override
            public int compare(MFAnalysisModel1 o1, MFAnalysisModel1 o2) {
                return sortAsc ? o1.valuation.compareTo(o2.valuation) : o2.valuation.compareTo(o1.valuation);
            }
        });
        dataSet.add(0, header);
        sortAsc = !sortAsc;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCategoryName;
        TextView textViewCategoryValuation;
        TextView textViewCategoryPerc;
        TextView textViewCategoryInvestment;
        TextView textViewCategoryReturns;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewCategoryName = (TextView) itemView.findViewById(R.id.app_category);
            this.textViewCategoryValuation = (TextView) itemView.findViewById(R.id.category_valuation);
            this.textViewCategoryPerc = (TextView) itemView.findViewById(R.id.category_percentage);
            this.textViewCategoryInvestment = (TextView) itemView.findViewById(R.id.category_investment);
            this.textViewCategoryReturns = (TextView) itemView.findViewById(R.id.app_category_returns);
        }
    }

}
