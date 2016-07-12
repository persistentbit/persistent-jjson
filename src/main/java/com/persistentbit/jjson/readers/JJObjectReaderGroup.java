package com.persistentbit.jjson.readers;


import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Peter Muys
 * @since 23/10/2015
 */
public class JJObjectReaderGroup implements JJObjectReader
{
    private List<JJObjectReader> readers = new ArrayList<>();

    public JJObjectReaderGroup(JJObjectReader...readers){
        add(readers);
    }
    public JJObjectReaderGroup add(JJObjectReader...readers){
        this.readers.addAll(Arrays.asList(readers));
        return this;
    }

    @Override
    public boolean canRead(JJNode node)
    {
        for(JJObjectReader r : readers){
            if(r.canRead(node)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object read(Type type, JJNode node, JJReader reader)
    {
        for(JJObjectReader r : readers){
            if(r.canRead(node)){
                return r.read(type,node,reader);
            }
        }
        throw new RuntimeException("Can't find reader");
    }
}
