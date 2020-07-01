package com.sanchit.myfunds.model.mf.synopsis2.datalist.factory;

import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.model.mf.synopsis2.datalist.MFDataListModel;

import java.util.ArrayList;
import java.util.List;

public class DummyFactory extends AbstractFactory {

    protected DummyFactory(List<MFTrade> trades, String header) {
        super(trades, header);
    }

    @Override
    public List<MFDataListModel> generateData() {
        List<MFDataListModel> dataset = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            MFDataListModel model = new MFDataListModel();
            model.setName(header + " " + i);
            model.setPart1(i + "%");
            model.setPart2(i + "%");
            model.setPart3(i + "%");
            dataset.add(model);
        }

        return dataset;
    }

}
