package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author Peter Muys
 * @since 29/08/2016
 */
public class JJEnumReader implements JJObjectReader {

    @Override
    public Object read(Type type, JJNode node, JJReader reader) {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        String name = node.asString().get().getValue();
        Class cls = ReflectionUtils.classFromType(type);
        try {
            return cls.getDeclaredField(name).get(null);
        }catch(Exception e){
            throw new JJsonException("Error reading field " + name + " for enum " + cls.getName(),e);
        }
    }


}
