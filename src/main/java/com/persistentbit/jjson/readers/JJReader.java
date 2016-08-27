package com.persistentbit.jjson.readers;


import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.jjson.nodes.*;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Interface representing a Reader that can translate a {@link JJNode} object to a Java Object.
 *
 * @author Peter Muys
 * @see com.persistentbit.jjson.writers.JJWriter
 * @see JJNode
 * @see JJDefaultReader
 */
@FunctionalInterface
public interface JJReader
{
    default <T>T read(JJNode node, Class<T> cls){
        return read(node,cls,cls);
    }

    <T>T read(JJNode node, Class<T> cls, Type type);
}
