package com.sanchit.myfunds.enricher.event;

import com.sanchit.myfunds.model.mf.MFTrade;

public interface OnEnrichmentCompleted<T, V> {

    void updateView(T model, MFTrade trade, V value);

}
