package com.persistentbit.jjson.writers;


import com.persistentbit.jjson.nodes.JJNode;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 18:30
 */
public interface JJObjectWriter{

    interface JJMasterWriter{
        JJNode write(Object value);
    }

    JJNode write(Object value, JJMasterWriter masterWriter);
}