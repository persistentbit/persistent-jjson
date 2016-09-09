package com.persistentbit.jjson.mapping.impl.custom;


import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
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
import com.persistentbit.jjson.nodes.JJNodeObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 13:36
 */
public class JJMapReader  implements JJObjectReader,JJDescriber
{
    private final Supplier<Map> supplier;

    public JJMapReader(Supplier<Map> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Object read(Type t, JJNode node, JJReader reader)
    {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(t instanceof ParameterizedType == false){
            throw new JJsonException("Expected a parameterized Map, not just a Map");
        }
        ParameterizedType pt  = (ParameterizedType)t;
        Type keyType = pt.getActualTypeArguments()[0];
        Type valueType = pt.getActualTypeArguments()[1];
        Class<?> keyClass = ReflectionUtils.classFromType(keyType);
        Class<?> valueClass = ReflectionUtils.classFromType(valueType);
        Map result = supplier.get();
        JJNodeArray arr = node.asArray().get();
        for(JJNode entry : arr){

            JJNodeObject obj = entry.asObject().get();
            Object key = reader.read(obj.get("key").get(),keyClass,keyType);
            Object value = reader.read(obj.get("value").get(),valueClass,valueType);
            result.put(key,value);
        }

        return result;
    }

    @Override
    public JJTypeDescription describe(Type t, JJDescriber masterDescriber) {
        Class cls = ReflectionUtils.classFromType(t);


        PMap<String,JJTypeSignature> td = JJDescriber.getGenericsParams(t,masterDescriber);
        JJTypeSignature sig = new JJTypeSignature(new JJClass(cls), JJTypeSignature.JsonType.jsonMap, td);
        PList<String> doc = PList.empty();

        return new JJTypeDescription(sig,doc);
    }
}
