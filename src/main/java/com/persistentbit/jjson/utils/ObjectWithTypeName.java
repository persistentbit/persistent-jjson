package com.persistentbit.jjson.utils;

import com.persistentbit.core.result.Result;

/**
 * @author Peter Muys
 * @since 1/09/2016
 */
public class ObjectWithTypeName {

    private final String         typeName;
    private final Result<Object> value;

    public ObjectWithTypeName(Result<Object> value) {
        this.typeName = value == null ? null: value.getClass().getName();
        this.value = value;
    }

    public ObjectWithTypeName(String typeName, Result<Object> value) {
        this.typeName = typeName;
        this.value = value;
    }

    public String getTypeName() {
        return typeName;
    }

    public Result<Object> getValue() {
        return value;
    }

}
