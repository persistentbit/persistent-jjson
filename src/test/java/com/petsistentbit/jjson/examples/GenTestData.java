package com.petsistentbit.jjson.examples;

import com.persistentbit.core.tuples.Tuple2;

/**
 * @author Peter Muys
 * @since 31/08/2016
 */
@SuppressWarnings("FieldCanBeLocal")
class GenTestData<A, B>{

	private final Tuple2<A,String> tupleAString;
    private final Tuple2<B,Integer> tupleBInteger;
    private final Tuple2<Float,Double> tupleFloatDouble;

	public GenTestData() {
		this(null,null,null);
    }

	public GenTestData(Tuple2<A, String> tupleAString, Tuple2<B, Integer> tupleBInteger,
					   Tuple2<Float, Double> tupleFloatDouble
	) {
		this.tupleAString = tupleAString;
        this.tupleBInteger = tupleBInteger;
        this.tupleFloatDouble = tupleFloatDouble;
    }
}
