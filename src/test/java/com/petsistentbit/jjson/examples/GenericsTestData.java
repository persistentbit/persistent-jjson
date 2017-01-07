package com.petsistentbit.jjson.examples;

import com.persistentbit.core.tuples.Tuple2;

/**
 * Created by petermuys on 3/09/16.
 */
@SuppressWarnings("FieldCanBeLocal")
public class GenericsTestData<GT>{

	private final GT gtValue;
    private final Tuple2<GT,String> tuple2_GT_String;

	public GenericsTestData(GT gtValue, Tuple2<GT, String> tuple2_GT_String) {
		this.gtValue = gtValue;
        this.tuple2_GT_String = tuple2_GT_String;
    }
}
