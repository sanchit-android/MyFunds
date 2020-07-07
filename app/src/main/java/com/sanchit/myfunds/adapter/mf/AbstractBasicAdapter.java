package com.sanchit.myfunds.adapter.mf;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.myfunds.R;

public abstract class AbstractBasicAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected final Context context;
    protected int firstPosition = 0;
    protected int lastPosition = -1;

    public AbstractBasicAdapter(Context context) {
        this.context = context;
    }

    protected void setAnimation(CardView viewToAnimate, int position) {
        if (position >= lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_enter_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
        if (position < lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_enter_right);
            viewToAnimate.startAnimation(animation);
            firstPosition = position;
        }
    }
}
