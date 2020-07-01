package com.sanchit.myfunds.utils;

import java.math.BigDecimal;
import java.util.Comparator;

public interface Constants {
    Comparator<BigDecimal> INCREASING_COMPARATOR = ((x, y) -> x.compareTo(y));
    Comparator<BigDecimal> DECREASING_COMPARATOR = ((x, y) -> y.compareTo(x));

    BigDecimal EMPTY_PRICE = new BigDecimal(-1);

    interface Duration {
        String T = "T";
        String T_1 = "T Minus 1";
        String T_10 = "T Minus 10";
        String T_30 = "T Minus 30";
        String T_90 = "T Minus 90";
        String T_180 = "T Minus 180";
        String T_365 = "T Minus 365";
    }
}
