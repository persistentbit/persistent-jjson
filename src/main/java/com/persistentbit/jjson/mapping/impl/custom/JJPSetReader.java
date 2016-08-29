package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.IPSet;
import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * User: petermuys
 * Date: 26/08/16
 * Time: 08:57
 */
public class JJPSetReader implements JJObjectReader {
    private final Supplier<IPSet>   supplier;

    public JJPSetReader(Supplier<IPSet> supplier) {
        this.supplier = supplier;
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
        return  supplier.get().plusAll(arr.pstream().map(n -> reader.read(n,cls,itemType)));
    }
}
