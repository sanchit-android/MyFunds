package com.sanchit.myfunds.connectors.mfapi;

import android.os.AsyncTask;

import com.sanchit.myfunds.enricher.event.OnEnrichmentCompleted;
import com.sanchit.myfunds.model.mf.MFAnalysisModel1;
import com.sanchit.myfunds.model.mf.MFTrade;
import com.sanchit.myfunds.utils.Constants;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class MFAPIConnector extends AsyncTask<String, Void, Map<String, BigDecimal[]>> {

    private static final Map<String, JSONObject> DATA_MAP = new HashMap<>();

    private OnEnrichmentCompleted<Object, Map<String, BigDecimal[]>> callback;
    private MFAnalysisModel1 model;
    private MFTrade trade;

    public MFAPIConnector(MFTrade trade, MFAnalysisModel1 model, OnEnrichmentCompleted<Object, Map<String, BigDecimal[]>> callback) {
        this.trade = trade;
        this.model = model;
        this.callback = callback;
    }

    @Override
    protected Map<String, BigDecimal[]> doInBackground(String... apis) {
        String url = apis[0];
        JSONObject jo = DATA_MAP.get(url);

        try {
            if (jo == null) {
                jo = callEndpoint(url);
            }
            return parseJSONResponse(jo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, BigDecimal[]> parseJSONResponse(JSONObject jo) {
        Map<String, BigDecimal[]> navMap = newNAVMap();
        for (Map.Entry<String, BigDecimal[]> entry : navMap.entrySet()) {
            entry.getValue()[1] = parseTMinusXNAV(jo, entry.getValue()[0].intValue());
        }
        return navMap;
    }

    private JSONObject callEndpoint(String apiURL) throws IOException, ParseException {
        URL url = new URL(apiURL);
        URLConnection connection = url.openConnection();

        InputStream is = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);
        Object obj = new JSONParser().parse(reader);

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
        DATA_MAP.put(apiURL, jo);
        return jo;
    }

    private Map<String, BigDecimal[]> newNAVMap() {
        Map<String, BigDecimal[]> navKeys = new HashMap<>();
        navKeys.put(Constants.Duration.T, new BigDecimal[]{new BigDecimal(1), Constants.EMPTY_PRICE});
        navKeys.put(Constants.Duration.T_1, new BigDecimal[]{new BigDecimal(2), Constants.EMPTY_PRICE});
        navKeys.put(Constants.Duration.T_10, new BigDecimal[]{new BigDecimal(10), Constants.EMPTY_PRICE});
        navKeys.put(Constants.Duration.T_30, new BigDecimal[]{new BigDecimal(30), Constants.EMPTY_PRICE});
        navKeys.put(Constants.Duration.T_90, new BigDecimal[]{new BigDecimal(90), Constants.EMPTY_PRICE});
        navKeys.put(Constants.Duration.T_180, new BigDecimal[]{new BigDecimal(180), Constants.EMPTY_PRICE});
        navKeys.put(Constants.Duration.T_365, new BigDecimal[]{new BigDecimal(365), Constants.EMPTY_PRICE});
        return navKeys;
    }

    private BigDecimal parseLatestNAV(JSONObject jsonObject) {
        JSONArray data = (JSONArray) jsonObject.get("data");
        BigDecimal latestNAV = new BigDecimal(((JSONObject) data.get(0)).get("nav").toString());
        return latestNAV;
    }

    private BigDecimal parseTMinusXNAV(JSONObject jsonObject, int x) {
        JSONArray data = (JSONArray) jsonObject.get("data");
        if (data != null && data.size() >= x) {
            BigDecimal latestNAV = new BigDecimal(((JSONObject) data.get(x - 1)).get("nav").toString());
            return latestNAV;
        }
        return Constants.EMPTY_PRICE;
    }

    @Override
    protected void onPostExecute(Map<String, BigDecimal[]> navMap) {
        callback.updateView(model, trade, navMap);
    }
}