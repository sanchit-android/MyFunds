package com.sanchit.myfunds.adapter.mf;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.activity.InvestmentSynopsis2Activity;
import com.sanchit.myfunds.activity.mf.MFDataListingActivity;
import com.sanchit.myfunds.model.mf.synopsis2.Synopsis2DataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvestmentSynopsis2Adapter extends AbstractBasicAdapter<InvestmentSynopsis2Adapter.InvestmentSynopsis2ViewHolder> {

    private final List<Synopsis2DataModel> itemList;

    public InvestmentSynopsis2Adapter(Context context,
                                      List<Synopsis2DataModel> itemList) {
        super(context);
        this.itemList = itemList;
    }


    @Override
    public InvestmentSynopsis2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cards_mf_synopsis2, null);
        InvestmentSynopsis2ViewHolder rcv = new InvestmentSynopsis2ViewHolder(layoutView, context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(InvestmentSynopsis2ViewHolder holder, int position) {
        holder.textViewBoxHeader.setText(itemList.get(position).getHeader());
        holder.textViewBoxName.setText(itemList.get(position).getName());
        holder.textViewBoxData.setText(itemList.get(position).getData());
        holder.card.setCardBackgroundColor(context.getResources().getColor(itemList.get(position).getCardColor()));
        setAnimation(holder.card, position);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    public static class InvestmentSynopsis2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CardView card;
        private final TextView textViewBoxHeader;
        private final TextView textViewBoxName;
        private final TextView textViewBoxData;
        private final Context context;

        public InvestmentSynopsis2ViewHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.textViewBoxHeader = (TextView) itemView.findViewById(R.id.synopsis2_box_header);
            this.textViewBoxName = (TextView) itemView.findViewById(R.id.synopsis2_box_name);
            this.textViewBoxData = (TextView) itemView.findViewById(R.id.synopsis2_box_data);
            this.card = (CardView) itemView.findViewById(R.id.synopsis2_card_view);
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            String header = ((TextView) v.findViewById(R.id.synopsis2_box_header)).getText().toString();
            Intent intent = new Intent(context, MFDataListingActivity.class);

            intent.putExtra("header", (String) header);
            intent.putExtra("trades", (ArrayList) ((InvestmentSynopsis2Activity) context).getTrades());
            intent.putExtra("prices", (HashMap) ((InvestmentSynopsis2Activity) context).getPriceMap());
            context.startActivity(intent);
        }
    }
}
