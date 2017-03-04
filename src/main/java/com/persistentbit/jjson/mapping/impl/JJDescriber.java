package com.persistentbit.jjson.mapping.impl;

import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PStream;
import com.persistentbit.core.tuples.Tuple2;
import com.persistentbit.core.utils.UReflect;
import com.persistentbit.jjson.mapping.description.JJClass;
import com.persistentbit.jjson.mapping.description.JJTypeDescription;
import com.persistentbit.jjson.mapping.description.JJTypeSignature;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static com.persistentbit.core.collections.PStream.from;

/**
 * @author Peter Muys
 * @since 31/08/2016
 */
@FunctionalInterface
public interface JJDescriber {
    JJTypeDescription   describe(Type t,JJDescriber masterDescriber);

    static PMap<String,JJTypeSignature> getGenericsParams(Type t,JJDescriber masterDescriber) {
        if(t instanceof TypeVariable){
            return PMap.<String,JJTypeSignature>empty().put(((TypeVariable) t).getName(),new JJTypeSignature(new JJClass(Object.class), JJTypeSignature.JsonType.jsonObject));
        }
		Class cls = UReflect.classFromType(t);
		if(cls.getTypeParameters().length ==0){
            return PMap.empty();
        }
        if(t == cls){
            return PStream.from(cls.getTypeParameters()).groupByOneValue(i -> i.getName(), i->new JJTypeSignature(new JJClass(Object.class), JJTypeSignature.JsonType.jsonObject));
        }

        ParameterizedType pt = (ParameterizedType)t;
        PStream<Tuple2<TypeVariable,Type>> genParams = from(pt.getActualTypeArguments()).zip(from(cls.getTypeParameters()));

        return genParams.groupByOneValue(i -> i._1.getName(), i -> masterDescriber.describe(i._2,masterDescriber).getTypeSignature());
    }

}
