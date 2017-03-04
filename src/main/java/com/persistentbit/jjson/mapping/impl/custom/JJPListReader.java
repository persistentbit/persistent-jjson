package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.IPList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.utils.UReflect;
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
import java.util.function.Supplier;

/**
 * User: petermuys
 * Date: 25/08/16
 * Time: 19:01
 */
public class JJPListReader  implements JJObjectReader,JJDescriber {

    private final Supplier<IPList> supplier;

    public JJPListReader(Supplier<IPList> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Object read(Type type, JJNode node, JJReader reader) {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(type instanceof ParameterizedType == false){
            throw new JJsonException("Expected a parameterized PList, not just a PList");
        }
        ParameterizedType pt       = (ParameterizedType)type;
        Type              itemType = pt.getActualTypeArguments()[0];
		Class             cls      = UReflect.classFromType(itemType);
		JJNodeArray       arr      = node.asArray().orElseThrow();
        return  supplier.get().plusAll(arr.pstream().map(n -> reader.read(n,cls,itemType)));
    }

    @Override
    public JJTypeDescription describe(Type type, JJDescriber masterDescriber) {
        ParameterizedType pt  = (ParameterizedType)type;
        Type itemType = pt.getActualTypeArguments()[0];
        JJTypeSignature itemTypeSig =  masterDescriber.describe(itemType,masterDescriber).getTypeSignature();
        return new JJTypeDescription(new JJTypeSignature(new JJClass(supplier.get().getClass()), JJTypeSignature.JsonType.jsonArray, PMap.<String,JJTypeSignature>empty().put("ITEM",itemTypeSig)));
    }
}
