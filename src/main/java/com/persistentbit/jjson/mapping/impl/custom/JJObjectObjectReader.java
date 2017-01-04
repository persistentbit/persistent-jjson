package com.persistentbit.jjson.mapping.impl.custom;


import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJsonException;
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
            return node.asString().orElseThrow().getValue();
        }

        JJNodeObject jobj = node.asObject().orElseThrow();
        String clsName = jobj.get("objectClass").get().asString().orElseThrow().getValue();
        try
        {
            Class<?> cls = getClass().getClassLoader().loadClass(clsName);
            return reader.read(jobj.get("value").get(),cls);
        }
        catch (ClassNotFoundException e)
        {
            throw new JJsonException(e);
        }
    }
}
