package com.persistentbit.jjson.writers;

import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

/**
 * User: petermuys
 * Date: 25/08/16
 * Time: 19:17
 */
public class JJPMapWriter  implements JJObjectWriter {
    @Override
    public JJNode write(Object value, JJMasterWriter masterWriter) {
        PMap<Object,Object> v = (PMap) value;
        return new JJNodeArray(v.lazy().map(i  -> new JJNodeArray(masterWriter.write(i._1),masterWriter.write(i._2))));

    }
}