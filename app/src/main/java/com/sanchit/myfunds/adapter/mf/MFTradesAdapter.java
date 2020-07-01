package com.sanchit.myfunds.adapter.mf;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.activity.mf.MFPositionsActivity;
import com.sanchit.myfunds.model.mf.MFTrade;

import java.math.BigDecimal;
import java.util.List;

public class MFTradesAdapter extends AbstractBasicAdapter<MFTradesAdapter.ViewHolder> {

    private static final int SCALE_DISPLAY = 0;
    private final Activity parent;
    private List<MFTrade> dataSet;

    public MFTradesAdapter(List<MFTrade> data, MFPositionsActivity activity) {
        super(activity.getBaseContext());
        this.dataSet = data;
        this.parent = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_mf_trade, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {

        TextView textViewFundPurchaseDate = holder.textViewFundPurchaseDate;
        TextView textViewFundValuation = holder.textViewFundValuation;
        TextView textViewFundQuantity = holder.textViewFundQuantity;
        TextView textViewFundPurchasePrice = holder.textViewFundPurchasePrice;
        TextView textViewFundInvestment = holder.textViewFundInvestment;
        TextView textViewFundSide = holder.textViewFundSide;

        textViewFundPurchaseDate.setText(dataSet.get(listPosition).getTransactionDate());
        textViewFundValuation.setText(dataSet.get(listPosition).getValuation());
        textViewFundQuantity.setText(dataSet.get(listPosition).getUnitsAlloted());
        textViewFundPurchasePrice.setText(dataSet.get(listPosition).getTransactionPrice());
        textViewFundInvestment.setText(dataSet.get(listPosition).getInvestment());
        textViewFundSide.setText(dataSet.get(listPosition).getTransactionType());

        setValuationTone(dataSet.get(listPosition).getValuationValue(), dataSet.get(listPosition).getInvestmentValue(), textViewFundValuation);
        highlightHeader(holder, dataSet.get(listPosition));
        setAnimation(holder.card, listPosition);
    }

    private void highlightHeader(ViewHolder holder, MFTrade trade) {
        if (!trade.isHeader()) {
            return;
        }

        TextView textViewFundPurchaseDate = holder.textViewFundPurchaseDate;
        TextView textViewFundValuation = holder.textViewFundValuation;
        TextView textViewFundQuantity = holder.textViewFundQuantity;
        TextView textViewFundPurchasePrice = holder.textViewFundPurchasePrice;
        TextView textViewFundInvestment = holder.textViewFundInvestment;
        TextView textViewFundSide = holder.textViewFundSide;

        setColor(textViewFundPurchaseDate);
        setColor(textViewFundValuation);
        setColor(textViewFundQuantity);
        setColor(textViewFundPurchasePrice);
        setColor(textViewFundInvestment);
        setColor(textViewFundSide);

        textViewFundInvestment.setTextColor(parent.getResources().getColor(R.color.colorPrimaryDark));
        textViewFundInvestment.setText("   ");
        //textViewFundInvestment.setHeight(0);

        CardView cardView = (CardView) textViewFundPurchaseDate.getParent().getParent();
        cardView.setBackgroundColor(parent.getResources().getColor(R.color.colorPrimaryDark));
    }

    private void setColor(TextView textView) {
        textView.setTextColor(parent.getResources().getColor(R.color.colorPrimary));
        textView.setTypeface(null, Typeface.NORMAL);
        //textView.setTypeface(null, Typeface.BOLD);
    }

    private void setValuationTone(BigDecimal valuation, BigDecimal investment, TextView textViewFundValuation) {
        if (valuation.compareTo(investment) > 0) {
            textViewFundValuation.setTextColor(parent.getResources().getColor(R.color.green_800));
        } else if (valuation.compareTo(investment) == 0) {
            textViewFundValuation.setTextColor(parent.getResources().getColor(R.color.black));
        } else {
            textViewFundValuation.setTextColor(parent.getResources().getColor(R.color.red_A700));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView card;
        TextView textViewFundSide;
        TextView textViewFundQuantity;
        TextView textViewFundPurchasePrice;
        TextView textViewFundInvestment;
        TextView textViewFundPurchaseDate;
        TextView textViewFundValuation;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewFundPurchaseDate = (TextView) itemView.findViewById(R.id.fund_purchase_date);
            this.textViewFundValuation = (TextView) itemView.findViewById(R.id.fund_valuation);
            this.textViewFundQuantity = (TextView) itemView.findViewById(R.id.fund_quantity);
            this.textViewFundPurchasePrice = (TextView) itemView.findViewById(R.id.fund_purchase_price);
            this.textViewFundInvestment = (TextView) itemView.findViewById(R.id.fund_investment);
            this.textViewFundSide = (TextView) itemView.findViewById(R.id.fund_purchase_side);
            this.card = (CardView) itemView.findViewById(R.id.trades_card_view);
        }
    }
}
