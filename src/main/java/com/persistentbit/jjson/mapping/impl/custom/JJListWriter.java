package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.PStream;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

import java.util.List;


/**
 * @author Peter Muys
 * @since 1/09/2016
 */
public class JJListWriter implements JJObjectWriter {
    @Override
    public JJNode write(Object value, JJWriter masterWriter) {
        List<?> v = (List<?>)value;
        return new JJNodeArray(PStream.from(v).map(i -> masterWriter.write(i)));

    }
}
