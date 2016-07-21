package com.persistentbit.jjson.readers;



import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 13:36
 */
public class JJMapReader  implements JJObjectReader
{

    @Override
    public boolean canRead(JJNode node)
    {
        return node.getType() == JJNode.JType.jsonNull || node.getType() == JJNode.JType.jsonArray;
    }

    @Override
    public Object read(Type t, JJNode node, JJReader reader)
    {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(t instanceof ParameterizedType == false){
            throw new RuntimeException("Expected a parameterized Map, not just a Map");
        }
        ParameterizedType pt  = (ParameterizedType)t;
        Type keyType = pt.getActualTypeArguments()[0];
        Type valueType = pt.getActualTypeArguments()[1];
        Class<?> keyClass = ReflectionUtils.classFromType(keyType);
        Class<?> valueClass = ReflectionUtils.classFromType(valueType);
        Map result = new LinkedHashMap<>();
        JJNodeArray arr = node.asArray().get();
        for(JJNode entry : arr){
            JJNodeArray entryArr = entry.asArray().get();
            Object key = reader.read(entryArr.pstream().get(0),keyClass,keyType);
            Object value = reader.read(entryArr.pstream().get(1),valueClass,valueType);
            result.put(key,value);
        }

        return result;
    }


}
