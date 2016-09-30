package com.petsistentbit.jjson.examples;

import com.persistentbit.core.tuples.Tuple2;

/**
 * Created by petermuys on 3/09/16.
 */
public class GenericsTest<GT> {
    public GT gtValue;
    public Tuple2<GT,String> tuple2_GT_String;

    public GenericsTest(GT gtValue, Tuple2<GT, String> tuple2_GT_String) {
        this.gtValue = gtValue;
        this.tuple2_GT_String = tuple2_GT_String;
    }
}
