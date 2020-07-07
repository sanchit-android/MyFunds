package com.sanchit.myfunds.adapter.mf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.model.mf.suggestion1.Suggestion1DataModel;

import java.util.List;

public class InvestmentSuggestions1Adapter extends AbstractBasicAdapter<InvestmentSuggestions1Adapter.InvestmentSuggestions1ViewHolder> {

    private final List<Suggestion1DataModel> itemList;

    public InvestmentSuggestions1Adapter(Context context,
                                         List<Suggestion1DataModel> itemList) {
        super(context);
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public InvestmentSuggestions1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cards_mf_suggestion_item, parent, false);
        InvestmentSuggestions1ViewHolder rcv = new InvestmentSuggestions1ViewHolder(layoutView, context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull InvestmentSuggestions1ViewHolder holder, int position) {
        holder.text1.setText(itemList.get(position).getText1());
        holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.color_white, null));
        setAnimation(holder.card, position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class InvestmentSuggestions1ViewHolder extends RecyclerView.ViewHolder {
        private final CardView card;
        private final TextView text1;
        private final Context context;

        public InvestmentSuggestions1ViewHolder(View itemView, Context context) {
            super(itemView);
            this.text1 = (TextView) itemView.findViewById(R.id.mf_suggestion_text1);
            this.card = (CardView) itemView.findViewById(R.id.suggestion1_card_view);
            this.context = context;
        }
    }
}
