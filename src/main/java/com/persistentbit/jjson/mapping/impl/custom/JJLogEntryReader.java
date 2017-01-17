package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeObject;

import java.lang.reflect.Type;

/**
 * TODO: Add comment
 *
 * @author Peter Muys
 * @since 17/01/2017
 */
public class JJLogEntryReader implements JJObjectReader{



    @Override
    public Object read(Type type, JJNode node, JJReader masterReader) {
        if(node == null || node.asNull().isPresent()){
            return null;
        }
        JJNodeObject obj = node.asObject().orElseThrow();

        Class cls;
        try{
            String clsName = obj.get("type").get().asString().orElseThrow().getValue();
            cls = Class.forName(clsName);
        }catch (Exception e){
            throw new RuntimeException("Can't get class " ,e);
        }

        return masterReader.read(obj.get("entry").get(),cls);
    }
}
