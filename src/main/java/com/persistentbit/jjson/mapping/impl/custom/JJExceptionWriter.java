package com.persistentbit.jjson.mapping.impl.custom;


import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeNull;
import com.persistentbit.jjson.nodes.JJNodeObject;
import com.persistentbit.jjson.nodes.JJNodeString;

/**
 * @author Peter Muys
 * @since 28/10/2015
 */
public class JJExceptionWriter implements JJObjectWriter
{

    @Override
    public JJNode write(Object value, JJWriter masterWriter)
    {
        if(value == null){
            return JJNodeNull.Null;
        }
        return new JJNodeObject().plus("exceptionType",new JJNodeString(value.getClass().getName())).plus("message",new JJNodeString(((Exception)value).getMessage()));
    }
}
