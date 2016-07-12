package com.persistentbit.jjson.readers;



import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.Type;

/**
 * @author Peter Muys
 * @since 23/10/2015
 */
public interface JJObjectReader
{
    boolean canRead(JJNode node);
    Object read(Type type, JJNode node, JJReader reader);
}
