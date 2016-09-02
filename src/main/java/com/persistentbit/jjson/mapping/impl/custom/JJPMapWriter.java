package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.IPMap;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;
import com.persistentbit.jjson.nodes.JJNodeObject;

/**
 * User: petermuys
 * Date: 25/08/16
 * Time: 19:17
 */
public class JJPMapWriter  implements JJObjectWriter {
    @Override
    public JJNode write(Object value, JJWriter masterWriter) {
        IPMap<Object,Object> v = (IPMap) value;
        return new JJNodeArray(v.lazy().map(e-> new JJNodeObject(
                PMap.<String,JJNode>empty().put("key",masterWriter.write(e._1)).put("value",masterWriter.write(e._2))
        )));
    }
}