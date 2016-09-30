package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.IPMap;
import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.tuples.Tuple2;
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
import java.util.function.Supplier;

/**
 * User: petermuys
 * Date: 26/08/16
 * Time: 09:00
 */
public class JJPMapReader   implements JJObjectReader, JJDescriber {

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
            throw new JJsonException("Expected a parameterized PMap, not just a PMap");
        }
        ParameterizedType pt  = (ParameterizedType)type;
        Type[] typeArgs = pt.getActualTypeArguments();
        Type keyType = typeArgs[0];
        Type valueType = typeArgs[1];
        Class clsKey = ReflectionUtils.classFromType(keyType);
        Class clsValue = ReflectionUtils.classFromType(valueType);
        JJNodeArray arr = node.asArray().get();
        return  ipMapSupplier.get().plusAll(arr.pstream().map(n -> {
            JJNodeObject obj = n.asObject().get();
            Object key = reader.read(obj.get("key").get(),clsKey,keyType);
            Object value = reader.read(obj.get("value").get(),clsValue,valueType);
            return new Tuple2(key,value);
        }));
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
