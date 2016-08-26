package com.persistentbit.jjson.readers;

import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: petermuys
 * Date: 26/08/16
 * Time: 08:57
 */
public class JJPSetReader implements JJObjectReader{
    @Override
    public boolean canRead(JJNode node) {
        return true;
    }

    @Override
    public Object read(Type type, JJNode node, JJReader reader) {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(type instanceof ParameterizedType == false){
            throw new RuntimeException("Expected a parameterized PSet, not just a PSet or PSet<Object>");
        }
        ParameterizedType pt  = (ParameterizedType)type;
        Type itemType = pt.getActualTypeArguments()[0];
        Class cls = ReflectionUtils.classFromType(itemType);
        JJNodeArray arr = node.asArray().get();
        return  PSet.empty().plusAll(arr.pstream().map(n -> reader.read(n,cls,itemType)));
    }
}
