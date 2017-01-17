package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.logging.entries.LogEntry;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.mapping.impl.JJReflectionObjectWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeObject;
import com.persistentbit.jjson.nodes.JJNodeString;

/**
 * TODO: Add comment
 *
 * @author Peter Muys
 * @since 17/01/2017
 */
public class JJLogEntryWriter implements JJObjectWriter{
    private JJObjectWriter realWriter;

    public JJLogEntryWriter(JJObjectWriter realWriter) {
        this.realWriter = realWriter;
    }
    public JJLogEntryWriter(Class<? extends LogEntry> reflectionClass){
        this(new JJReflectionObjectWriter(reflectionClass));
    }

    @Override
    public JJNode write(Object value, JJWriter masterWriter) {
        JJNodeObject res = new JJNodeObject();
        res = res.plus("type",new JJNodeString(value.getClass().getName()));
        res = res.plus("entry", realWriter.write(value,masterWriter));
        return res;
    }
}
