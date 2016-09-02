package com.persistentbit.jjson.mapping.impl.custom;


import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.description.JJTypeDescription;
import com.persistentbit.jjson.mapping.description.JJTypeSignature;
import com.persistentbit.jjson.mapping.impl.JJDescriber;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Supplier;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 13:11
 */
public class JJSetReader  implements JJObjectReader,JJDescriber
{
    public final Supplier<Set> supplier;


    public JJSetReader(Supplier<Set> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Object read(Type t, JJNode node, JJReader reader)
    {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(t instanceof ParameterizedType == false){
            throw new JJsonException("Expected a parameterized Set, not just a Set");
        }
        ParameterizedType pt  = (ParameterizedType)t;
        Type itemType = pt.getActualTypeArguments()[0];
        Class cls = ReflectionUtils.classFromType(itemType);
        JJNodeArray arr = node.asArray().get();
        Set result = supplier.get();
        for(JJNode i : arr){
            result.add(reader.read(i,cls,itemType));
        }
        return result;
    }


    @Override
    public JJTypeDescription describe(Type type, JJDescriber masterDescriber) {

        Class cls = ReflectionUtils.classFromType(type);
        return new JJTypeDescription(new JJTypeSignature(cls.getName(), JJTypeSignature.JsonType.jsonSet, JJDescriber.getGenericsParams(type,masterDescriber)));
    }
}
