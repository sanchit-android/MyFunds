package com.sanchit.myfunds.enricher.mf;

import com.sanchit.myfunds.connectors.mfapi.MFAPIConnector;
import com.sanchit.myfunds.enricher.event.OnEnrichmentCompleted;
import com.sanchit.myfunds.model.mf.Fund;
import com.sanchit.myfunds.model.mf.MFAnalysisModel1;
import com.sanchit.myfunds.model.mf.MFTrade;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MFNAVEnricher {

    public void enrich(MFAnalysisModel1 model, OnEnrichmentCompleted<Object, Map<String, BigDecimal[]>> callback) {
        for (MFTrade trade : model.getTrades()) {
            String apiURL = trade.fund.getMfAPI();
            new MFAPIConnector(trade, model, callback).execute(apiURL);
        }
    }

    public void enrich(List<MFTrade> trades, OnEnrichmentCompleted<Object, Map<String, BigDecimal[]>> callback) {
        for (MFTrade trade : trades) {
            String apiURL = trade.fund.getMfAPI();
            new MFAPIConnector(trade, null, callback).execute(apiURL);
        }
    }

    public void enrich(Fund fund, OnEnrichmentCompleted<Object, Map<String, BigDecimal[]>> callback) {
        String apiURL = fund.getMfAPI();
        new MFAPIConnector(null, null, callback).execute(apiURL);
    }
}
