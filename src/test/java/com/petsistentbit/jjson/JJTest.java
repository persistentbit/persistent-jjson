package com.petsistentbit.jjson;

import java.util.Date;
import java.util.List;

/**
 * User: petermuys
 * Date: 22/07/16
 * Time: 11:04
 */
public class JJTest {
    private final List<JJSubTest>   subList;
    private final Boolean booleanValue;
    private final short shortValue;
    private final Integer intValue;
    private final Long  longValue;
    private final Float floatValue;
    private final double doubleValue;
    private final Date dateValue;


    public JJTest(List<JJSubTest> subList, Boolean booleanValue, short shortValue, Integer intValue, Long longValue, Float floatValue, double doubleValue, Date dateValue) {
        this.subList = subList;
        this.booleanValue = booleanValue;
        this.shortValue = shortValue;
        this.intValue = intValue;
        this.longValue = longValue;
        this.floatValue = floatValue;
        this.doubleValue = doubleValue;
        this.dateValue = dateValue;
    }
}
