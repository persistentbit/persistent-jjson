package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.Tuple2;
import com.persistentbit.core.collections.IPMap;
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
 * Time: 09:00
 */
public class JJPMapReader   implements JJObjectReader {

    private Supplier<IPMap> ipMapSupplier;

    public JJPMapReader(Supplier<IPMap> ipMapSupplier) {
        this.ipMapSupplier = ipMapSupplier;
    }

    @Override
    public Object read(Type type, JJNode node, JJReader reader) {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(type instanceof ParameterizedType == false){
            throw new RuntimeException("Expected a parameterized PMap, not just a PMap");
        }
        ParameterizedType pt  = (ParameterizedType)type;
        Type[] typeArgs = pt.getActualTypeArguments();
        Type keyType = typeArgs[0];
        Type valueType = typeArgs[1];
        Class clsKey = ReflectionUtils.classFromType(keyType);
        Class clsValue = ReflectionUtils.classFromType(valueType);
        JJNodeArray arr = node.asArray().get();
        return  ipMapSupplier.get().plusAll(arr.pstream().map(n -> {
            JJNodeArray itemArr = n.asArray().get();
            Object[] tupleNodes = itemArr.pstream().toArray();
            return new Tuple2(reader.read((JJNode)tupleNodes[0],clsKey,keyType),reader.read((JJNode)tupleNodes[1],clsValue,valueType));
        }));
    }
}
