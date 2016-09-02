package com.persistentbit.jjson.mapping.impl.custom;



import com.persistentbit.core.collections.PList;
import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.description.JJTypeDescription;
import com.persistentbit.jjson.mapping.description.JJTypeSignature;
import com.persistentbit.jjson.mapping.impl.JJDescriber;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 17:31
 */
public class JJOptionalReader implements JJObjectReader,JJDescriber {

    @Override
    public Object read(Type type, JJNode node, JJReader reader) {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(type instanceof ParameterizedType == false){
            throw new JJsonException("Expected a parameterized Optional, not just a Optional");
        }
        ParameterizedType pt  = (ParameterizedType)type;
        Type itemType = pt.getActualTypeArguments()[0];
        JJNodeObject obj = node.asObject().get();
        if(obj.get("optionalValue").isPresent()){
            return Optional.of(reader.read(obj.get("optionalValue").get(),ReflectionUtils.classFromType(itemType),itemType));
        }
        return Optional.empty();
    }

    @Override
    public JJTypeDescription describe(Type t, JJDescriber masterDescriber) {
        PList<String> doc = PList.empty();
        return new JJTypeDescription(new JJTypeSignature(Optional.class.getName(), JJTypeSignature.JsonType.jsonOptional,JJDescriber.getGenericsParams(t,masterDescriber)),doc);
    }
}
