package com.persistentbit.jjson.mapping;


import com.persistentbit.jjson.mapping.impl.JJDescriber;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.mapping.impl.JJDefaultReader;

import java.lang.reflect.Type;

/**
 * Interface representing a Reader that can translate a {@link JJNode} object to a Java Object.
 *
 * @author Peter Muys
 * @see JJWriter
 * @see JJNode
 * @see JJDefaultReader
 */

public interface JJReader
{
    default <T>T read(JJNode node, Class<T> cls){
        return read(node,cls,cls);
    }

    <T>T read(JJNode node, Class<T> cls, Type type);
}
