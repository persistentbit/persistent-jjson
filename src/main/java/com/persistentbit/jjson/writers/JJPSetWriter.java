package com.persistentbit.jjson.writers;

import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

/**
 * User: petermuys
 * Date: 25/08/16
 * Time: 19:09
 */
public class JJPSetWriter implements JJObjectWriter {
    @Override
    public JJNode write(Object value, JJMasterWriter masterWriter) {
        PSet v = (PSet) value;
        return new JJNodeArray(v.lazy().map(i -> masterWriter.write(i)));

    }
}