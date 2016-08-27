package com.persistentbit.jjson.writers;

import com.persistentbit.core.collections.PList;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;
import com.persistentbit.jjson.nodes.JJNodeNull;

import java.util.Optional;

/**
 * User: petermuys
 * Date: 25/08/16
 * Time: 18:58
 */
public class JJPListWriter implements JJObjectWriter{
    @Override
    public JJNode write(Object value, JJWriter masterWriter) {
        PList v = (PList)value;
        return new JJNodeArray(v.lazy().map(i -> masterWriter.write(i)));

    }
}
