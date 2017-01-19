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
/*
    static public JJObjectReader jsonReader = new JJObjectReader() {
        @Override
        public Object read(Type type, JJNode node, JJReader masterReader) {
            JJNodeObject obj = node.asObject().orElseThrow();
            String tn = obj.get("typeName").get().asString().orElseThrow().getValue();
            if(tn == null){
                return new ObjectWithTypeName(tn,null);
            }
            try {
                return new ObjectWithTypeName(tn,masterReader.read(obj.get("value").get(),Class.forName(tn)));
            } catch (ClassNotFoundException e) {
                throw new JJsonException("can't find class '" + tn + "'" ,e);
            }
        }
    };*/
}
