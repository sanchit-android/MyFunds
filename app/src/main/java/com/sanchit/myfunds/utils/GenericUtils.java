package com.sanchit.myfunds.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GenericUtils {

    public static LinkedHashMap<String, String> sortMap(LinkedHashMap<String, String> map) {
        List<Map.Entry<String, String>> capitalList = new LinkedList<>(map.entrySet());

        Collections.sort(capitalList, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : capitalList) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}
