package com.persistentbit.jjson.readers;


import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeObject;

import java.lang.reflect.Type;

/**
 * @author Peter Muys
 * @since 28/10/2015
 */
public class JJExceptionReader implements JJObjectReader
{


    @Override
    public Object read(Type type, JJNode node, JJReader reader)
    {
        if(node.getType() ==JJNode.JType.jsonNull ){ return null; }
        JJNodeObject obj = node.asObject().get();
        String className = obj.get("exceptionType").get().asString().get().getValue();
        String message = obj.get("message").get().asString().get().getValue();
        try{
            return getClass().getClassLoader().loadClass(className).getDeclaredConstructor(String.class).newInstance(message);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
