package com.persistentbit.jjson.mapping.description;

import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.core.utils.BaseValueClass;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Peter Muys
 * @since 31/08/2016
 */
public class JJTypeSignature extends BaseValueClass{
    public enum JsonType{
        jsonArray,jsonBoolean,jsonNull,jsonNumber,jsonObject,jsonString, jsonSet, jsonMap;
        public boolean isJsonPrimitive() {
            return this == jsonString || this == jsonBoolean || this == jsonNull || this == jsonNumber;
        }
        public boolean isJsonCollection() {
            return this == jsonArray || this == jsonSet || this == jsonMap;
        }
    }
    private final String    javaClassName;
    private final JsonType  jsonType;
    private final PMap<String,JJTypeSignature> generics;

    public JJTypeSignature(String javaClassName, JsonType jsonType,  PMap<String,JJTypeSignature> generics) {
        this.javaClassName = javaClassName;
        this.jsonType = jsonType;
        this.generics = Objects.requireNonNull(generics);
    }
    public JJTypeSignature(String javaClassName, JsonType jsonType){
        this(javaClassName,jsonType,PMap.empty());
    }

    public String getJavaClassName() {
        return javaClassName;
    }

    public JsonType getJsonType() {
        return jsonType;
    }

    public PMap<String,JJTypeSignature> getGenerics() {
        return generics;
    }

    public PSet<String> getAllUsedClassNames(){
        Predicate<JJTypeSignature> include = c -> c.getJsonType() == JsonType.jsonObject && c.getJavaClassName().equals(Object.class.getName()) == false;
        PSet<String> res =  getGenerics().values().filter(include).map(JJTypeSignature::getAllUsedClassNames).join((a,b)-> a.plusAll(b)).orElse(PSet.empty());
        if(include.test(this)){
            res = res.plus(javaClassName);
        }
        return res;
    }

}
