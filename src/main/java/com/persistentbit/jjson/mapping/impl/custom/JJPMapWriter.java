package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.IPMap;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

/**
 * User: petermuys
 * Date: 25/08/16
 * Time: 19:17
 */
public class JJPMapWriter  implements JJObjectWriter {
    @Override
    public JJNode write(Object value, JJWriter masterWriter) {
        IPMap<Object,Object> v = (IPMap) value;
        return new JJNodeArray(v.lazy().map(i  -> new JJNodeArray(masterWriter.write(i._1),masterWriter.write(i._2))));

    }
}