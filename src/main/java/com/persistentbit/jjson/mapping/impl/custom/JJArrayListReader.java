package com.persistentbit.jjson.mapping.impl.custom;


import com.persistentbit.core.collections.PMap;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter Muys
 * @since 23/10/2015
 */
public class JJArrayListReader implements JJObjectReader,JJDescriber
{



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

        //ParameterizedType pt  = (ParameterizedType)type;
        //Type itemType = pt.getActualTypeArguments()[0];
        //JJTypeSignature itemTypeSig =  masterDescriber.describe(itemType,masterDescriber).getTypeSignature();

        //PStream<Type> genParams = PStream.from(pt.getActualTypeArguments());
        //PMap<String,JJTypeSignature> td = genParams.groupByOneValue(i -> i.getTypeName(), i -> describe(i,masterDescriber).getTypeSignature());
        PMap<String,JJTypeSignature> td = PMap.empty();
        return new JJTypeDescription(new JJTypeSignature(ArrayList.class.getName(), JJNode.JType.jsonArray, td));
    }
}
