package com.persistentbit.jjson.readers;



import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 13:11
 */
public class JJSetReader  implements JJObjectReader
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
            throw new RuntimeException("Expected a parameterized Set, not just a Set");
        }
        ParameterizedType pt  = (ParameterizedType)t;
        Type itemType = pt.getActualTypeArguments()[0];
        Object[] elements = (Object[])reader.array(ReflectionUtils.classFromType(itemType),itemType,node);
        return new HashSet(Arrays.asList(elements));
    }


}
