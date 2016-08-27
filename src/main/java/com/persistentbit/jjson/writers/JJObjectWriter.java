package com.persistentbit.jjson.writers;


import com.persistentbit.jjson.nodes.JJNode;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 18:30
 */
@FunctionalInterface
public interface JJObjectWriter{

    JJNode write(Object value, JJWriter masterWriter);
}