package com.persistentbit.jjson.mapping.description;

import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;

import java.util.Objects;

/**
 * @author Peter Muys
 * @since 31/08/2016
 */
public class JJTypeSignature {
    public enum JsonType{
        jsonArray,jsonBoolean,jsonNull,jsonNumber,jsonObject,jsonString, jsonSet, jsonMap,jsonOptional
    }
    private final String        javaClassName;
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
        return getGenerics().values().map(JJTypeSignature::getAllUsedClassNames).join((a,b)-> a.plusAll(b)).orElse(PSet.empty()).plus(javaClassName);
    }
}
