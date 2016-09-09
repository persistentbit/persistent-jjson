package com.persistentbit.jjson.mapping.impl.custom;


import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.description.JJClass;
import com.persistentbit.jjson.mapping.description.JJTypeDescription;
import com.persistentbit.jjson.mapping.description.JJTypeSignature;
import com.persistentbit.jjson.mapping.impl.JJDescriber;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Peter Muys
 * @since 23/10/2015
 */
public class JJListReader implements JJObjectReader,JJDescriber
{

    private final Supplier<List> supplier;

    public JJListReader(Supplier<List> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Object read(Type t, JJNode node, JJReader reader)
    {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(t instanceof ParameterizedType == false){
            throw new JJsonException("Expected a parameterized List, not just a List");
        }

        ParameterizedType pt  = (ParameterizedType)t;
        Type itemType = pt.getActualTypeArguments()[0];
        Class cls = ReflectionUtils.classFromType(itemType);
        JJNodeArray arr = node.asArray().get();
        List result = new ArrayList();
        for(JJNode i : arr){
            result.add(reader.read(i,cls,itemType));
        }
        return result;
    }

    @Override
    public JJTypeDescription describe(Type type, JJDescriber masterDescriber) {

        Class cls = ReflectionUtils.classFromType(type);
        return new JJTypeDescription(new JJTypeSignature(new JJClass(cls), JJTypeSignature.JsonType.jsonArray, JJDescriber.getGenericsParams(type,masterDescriber)));
    }
}
