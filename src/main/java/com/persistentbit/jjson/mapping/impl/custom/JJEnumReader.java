package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.PList;
import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.description.JJClass;
import com.persistentbit.jjson.mapping.description.JJTypeDescription;
import com.persistentbit.jjson.mapping.description.JJTypeSignature;
import com.persistentbit.jjson.mapping.impl.JJDescriber;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * @author Peter Muys
 * @since 29/08/2016
 */
public class JJEnumReader implements JJObjectReader,JJDescriber {

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

    @Override
    public JJTypeDescription describe(Type t, JJDescriber masterDescriber) {
        PList<String> doc = PList.empty();
        Class<?> cls = ReflectionUtils.classFromType(t);
        PList<String> values = PList.empty();
        for(Field f : cls.getDeclaredFields()){
            if(Modifier.isStatic(f.getModifiers()) && cls.isAssignableFrom(f.getType())){
                values = values.plus(f.getName());
            }
        }
        doc = doc.plus("This is an enum with following possible values: " + values.toString(", "));
        return new JJTypeDescription(new JJTypeSignature(new JJClass(cls),JJTypeSignature.JsonType.jsonString),doc);
    }
}
