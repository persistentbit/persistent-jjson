package com.persistentbit.jjson.readers;

import java.lang.reflect.*;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 13:11
 */
public class ReflectionUtils {
    public static Class<?> classFromType(Type t){
        if(t instanceof Class){
            return (Class<?>)t;
        }
        if(t instanceof ParameterizedType){
            return classFromType(((ParameterizedType)t).getRawType());
        }
        if(t instanceof GenericArrayType){
            GenericArrayType gat = (GenericArrayType)t;
            throw new RuntimeException(gat.getTypeName());
        }
        if(t instanceof WildcardType){
            WildcardType wct = (WildcardType)t;
            return classFromType(wct.getUpperBounds()[0]);
        }
        if(t instanceof TypeVariable){
            return Object.class;
        }
        throw new RuntimeException("Don't know how to handle " + t);
    }
}
