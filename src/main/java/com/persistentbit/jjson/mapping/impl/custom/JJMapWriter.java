package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PStream;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;
import com.persistentbit.jjson.nodes.JJNodeObject;

import java.util.Map;

/**
 * @author Peter Muys
 * @since 1/09/2016
 */
public class JJMapWriter  implements JJObjectWriter {
    @Override
    public JJNode write(Object value, JJWriter masterWriter) {
        Map v = (Map) value;

        PStream<Map.Entry> pstream = PStream.from(v.entrySet());
        return new JJNodeArray(
                pstream.map( e ->
                        new JJNodeObject(
                                PMap.<String,JJNode>empty().put("key",masterWriter.write(e.getKey())).put("value",masterWriter.write(e.getValue()))
                        )
                )
        );

    }
}