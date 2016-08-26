package com.persistentbit.jjson.readers;


import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.jjson.nodes.*;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Peter Muys
 * @since 23/10/2015
 */
public interface JJReader
{
    default <T>T read(JJNode node, Class<T> cls){
        return read(node,cls,cls);
    }

    <T>T read(JJNode node, Class<T> cls, Type type);
    JJReader addGeneralReader(Class<?> cls, JJObjectReader reader);

    JJReader addReader(Class<?> cls, JJObjectReader reader);
    JJReader addMapper(JJReaderMapper mapper);
}
