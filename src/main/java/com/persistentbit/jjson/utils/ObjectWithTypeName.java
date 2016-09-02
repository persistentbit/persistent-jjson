package com.persistentbit.jjson.utils;

import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeObject;

import java.lang.reflect.Type;

/**
 * @author Peter Muys
 * @since 1/09/2016
 */
public class ObjectWithTypeName {
    private final String typeName;
    private final Object value;

    public ObjectWithTypeName(Object value){
        this.typeName = value == null ? null: value.getClass().getName();
        this.value = value;
    }

    public ObjectWithTypeName(String typeName, Object value) {
        this.typeName = typeName;
        this.value = value;
    }

    public String getTypeName() {
        return typeName;
    }

    public Object getValue() {
        return value;
    }

    static public JJObjectReader jsonReader = new JJObjectReader() {
        @Override
        public Object read(Type type, JJNode node, JJReader masterReader) {
            JJNodeObject obj = node.asObject().get();
            String tn = obj.get("typeName").get().asString().get().getValue();
            if(tn == null){
                return new ObjectWithTypeName(tn,null);
            }
            try {
                return new ObjectWithTypeName(tn,masterReader.read(obj.get("value").get(),Class.forName(tn)));
            } catch (ClassNotFoundException e) {
                throw new JJsonException("can't find class '" + tn + "'" ,e);
            }
        }
    };
}
