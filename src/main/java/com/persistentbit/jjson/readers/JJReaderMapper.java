package com.persistentbit.jjson.readers;

import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.Type;

/**
 * @author Peter Muys
 * @since 30/10/2015
 */
public interface JJReaderMapper  {
    interface Result{
        boolean isDone();
        Object getValue();
    }
    Result read(Class<?> cls, Type type, JJNode node, JJReader reader);
}
