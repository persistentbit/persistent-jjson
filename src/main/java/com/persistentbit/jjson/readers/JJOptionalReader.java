package com.persistentbit.jjson.readers;



import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 17:31
 */
public class JJOptionalReader implements JJObjectReader{
    @Override
    public boolean canRead(JJNode node) {
        return true;
    }

    @Override
    public Object read(Type type, JJNode node, JJReader reader) {
        if(node.getType() == JJNode.JType.jsonNull){
            return Optional.empty();
        }
        if(type instanceof ParameterizedType == false){
            throw new RuntimeException("Expected a parameterized Optional, not just a Optional");
        }
        ParameterizedType pt  = (ParameterizedType)type;
        Type itemType = pt.getActualTypeArguments()[0];
        return Optional.of(reader.read(node,ReflectionUtils.classFromType(itemType),itemType));
    }
}
