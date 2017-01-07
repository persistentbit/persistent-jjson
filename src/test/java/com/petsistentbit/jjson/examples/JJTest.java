package com.petsistentbit.jjson.examples;

import com.persistentbit.core.collections.PByteList;
import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.core.tuples.Tuple2;
import com.persistentbit.core.utils.BaseValueClass;
import com.petsistentbit.jjson.JJSubTest;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Peter Muys
 * @since 22/07/16
 */
public class JJTest extends BaseValueClass{
    public enum EnumTest{
        enum1,enum2, lastEnum
    }


	private final List<JJSubTest>       subList;
	private final Boolean               booleanValue;
	private final short                 shortValue;
	private final Integer               intValue;
	private final Long                  longValue;
	private final Float                 floatValue;
	private final double                doubleValue;
	private final Date                  dateValue;
	private final PList<String>         pList;
	private final PMap<Integer,String>  pMap;
	private final PSet<Double>          pSet;
	private final Map<String, String>   map;
	private final EnumTest              enumTest;
	private final Tuple2<Integer,Float> tuple;
	private final PList<Byte>           bytes;
	private final PByteList             byteList;


    public JJTest(List<JJSubTest> subList, Boolean booleanValue, short shortValue, Integer intValue, Long longValue, Float floatValue, double doubleValue, Date dateValue,PList<String> pList,PMap<Integer,String> pMap,PSet<Double> pSet,Map<String,String> map,EnumTest enumTest,Tuple2<Integer,Float> tuple, PList<Byte> bytes, PByteList byteList) {
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
        this.enumTest = enumTest;
        this.tuple = tuple;
        this.byteList = byteList;
        this.bytes = bytes;
    }
}
