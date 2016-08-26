package com.petsistentbit.jjson;

import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.core.properties.FieldNames;
import com.persistentbit.core.utils.ImTools;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private final PList<String> pList;
    private final PMap<Integer,String>  pMap;
    private final PSet<Double> pSet;
    private final Map<String,String> map;



    public JJTest(List<JJSubTest> subList, Boolean booleanValue, short shortValue, Integer intValue, Long longValue, Float floatValue, double doubleValue, Date dateValue,PList<String> pList,PMap<Integer,String> pMap,PSet<Double> pSet,Map<String,String> map) {
        this.subList = subList;
        this.booleanValue = booleanValue;
        this.shortValue = shortValue;
        this.intValue = intValue;
        this.longValue = longValue;
        this.floatValue = floatValue;
        this.doubleValue = doubleValue;
        this.dateValue = dateValue;
        this.pList = pList;
        this.pMap = pMap;
        this.pSet = pSet;
        this.map = map;
    }

    @Override
    public boolean equals(Object obj) {
        return ImTools.get(JJTest.class).equalsAll(this,obj);
    }

    @Override
    public int hashCode() {
        return ImTools.get(JJTest.class).hashCodeAll(this);
    }
}
