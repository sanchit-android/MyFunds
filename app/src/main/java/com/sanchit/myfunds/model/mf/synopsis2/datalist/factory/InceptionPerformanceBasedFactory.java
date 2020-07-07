package com.sanchit.myfunds.model.mf.synopsis2.datalist.factory;

import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.model.mf.synopsis2.AnalysisKeySourcer;
import com.sanchit.myfunds.model.mf.synopsis2.datalist.MFDataListModel;
import com.sanchit.myfunds.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InceptionPerformanceBasedFactory extends AbstractFactory {

    private final AnalysisKeySourcer keySourcer;

    public InceptionPerformanceBasedFactory(List<MFTrade> trades, String header, AnalysisKeySourcer keySourcer) {
        super(trades, header);
        this.keySourcer = keySourcer;
    }

    @Override
    public List<MFDataListModel> generateData() {
        Map<String, BigDecimal> valuationMap = new HashMap<>();
        Map<String, BigDecimal> investedMap = new HashMap<>();
        Map<String, BigDecimal> profitMap = new LinkedHashMap<>();
        Map<String, Set<String>> keyToFundMap = new HashMap<>();

        for (MFTrade trade : trades) {
            String key = keySourcer.fetch(trade);

            if (!investedMap.containsKey(key)) {
                investedMap.put(key, new BigDecimal(0));
            }
            BigDecimal invested = investedMap.get(key).add(trade.unitsAlloted.multiply(trade.transactionPrice).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN));
            investedMap.put(key, invested);

            if (!valuationMap.containsKey(key)) {
                valuationMap.put(key, new BigDecimal(0));
            }
            BigDecimal valuation = valuationMap.get(key).add(trade.unitsAlloted.multiply(trade.currentPrice).setScale(SCALE_DISPLAY, BigDecimal.ROUND_HALF_DOWN));
            valuationMap.put(key, valuation);

            if (!keyToFundMap.containsKey(key)) {
                keyToFundMap.put(key, new HashSet<>());
            }
            keyToFundMap.get(key).add(trade.fund.fundName);

            if (!profitMap.containsKey(key)) {
                profitMap.put(key, new BigDecimal(0));
            }
            BigDecimal profit = valuationMap.get(key).subtract(investedMap.get(key));
            profitMap.put(key, profit.divide(investedMap.get(key), 4, BigDecimal.ROUND_HALF_DOWN));
        }

        LinkedHashMap<String, BigDecimal> sortedMap;
        if (!descending) {
            sortedMap = profitMap.entrySet().stream().sorted((Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        } else {
            sortedMap = profitMap.entrySet().stream().sorted((Map.Entry.<String, BigDecimal>comparingByValue().reversed()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }

        List<MFDataListModel> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : sortedMap.entrySet()) {
            MFDataListModel data = new MFDataListModel();
            data.setName(entry.getKey());
            data.setPart1(NumberUtils.formatMoney(investedMap.get(entry.getKey())));
            data.setPart2(NumberUtils.formatMoney(valuationMap.get(entry.getKey())));
            data.setPart3(NumberUtils.toPercentage(entry.getValue(), 2));
            result.add(data);
        }
        return result;
    }

}
