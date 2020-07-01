package com.sanchit.myfunds.adapter.mf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.model.mf.synopsis2.datalist.MFDataListModel;

import java.util.List;

public class MFDataListingAdapter extends AbstractBasicAdapter<MFDataListingAdapter.MFDataListingViewHolder> {

    private final List<MFDataListModel> itemList;

    public MFDataListingAdapter(Context context,
                                List<MFDataListModel> itemList) {
        super(context);
        this.itemList = itemList;
    }

    @Override
    public MFDataListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cards_mf_data_listing, parent, false);
        MFDataListingViewHolder rcv = new MFDataListingViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(MFDataListingViewHolder holder, int position) {
        if(position % 2 == 0) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.mf_data_listing_alternate_row, null));
        }
        else {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.color_white, null));
        }
        holder.textViewBoxName.setText(itemList.get(position).getName());
        holder.textViewPart1.setText(itemList.get(position).getPart1());
        holder.textViewPart2.setText(itemList.get(position).getPart2());
        String part3 = itemList.get(position).getPart3();
        holder.textViewPart3.setText(part3);
        if (part3.contains("-")) {
            holder.textViewPart3.setTextColor(context.getResources().getColor(R.color.mf_data_listing_negative, null));
        } else {
            holder.textViewPart3.setTextColor(context.getResources().getColor(R.color.mf_data_listing_positive, null));
        }
        setAnimation(holder.card, position);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public static class MFDataListingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CardView card;
        private final TextView textViewBoxName;
        private final TextView textViewPart1;
        private final TextView textViewPart2;
        private final TextView textViewPart3;

        public MFDataListingViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.textViewBoxName = (TextView) itemView.findViewById(R.id.mf_data_listing_name);
            this.textViewPart1 = (TextView) itemView.findViewById(R.id.mf_data_part1);
            this.textViewPart2 = (TextView) itemView.findViewById(R.id.mf_data_part2);
            this.textViewPart3 = (TextView) itemView.findViewById(R.id.mf_data_part3);
            this.card = (CardView) itemView.findViewById(R.id.mf_data_listing_card_view);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
