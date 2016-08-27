package com.persistentbit.jjson.readers;


import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeObject;

import java.lang.reflect.Type;

/**
 * @author Peter Muys
 * @since 29/10/2015
 */
public class JJObjectObjectReader implements JJObjectReader
{



    @Override
    public Object read(Type type, JJNode node, JJReader reader)
    {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(node.asString().isPresent()){
            return node.asString().get().getValue();
        }

        JJNodeObject jobj = node.asObject().get();
        String clsName = jobj.get("objectClass").get().asString().get().getValue();
        try
        {
            Class<?> cls = getClass().getClassLoader().loadClass(clsName);
            return reader.read(jobj.get("value").get(),cls);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}
