package com.persistentbit.jjson.readers;


import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter Muys
 * @since 23/10/2015
 */
public class JJArrayListReader implements JJObjectReader
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
            throw new RuntimeException("Expected a parameterized List, not just a List");
        }
        ParameterizedType pt  = (ParameterizedType)t;
        Type itemType = pt.getActualTypeArguments()[0];

        Object[] arr = (Object[])reader.array(ReflectionUtils.classFromType(itemType),itemType,node);
        List result  =   new ArrayList(arr.length);
        for(Object o : arr){
            result.add(o);
        }
        return result;
    }


}
