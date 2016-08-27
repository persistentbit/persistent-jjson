package com.persistentbit.jjson.writers;



import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeNull;

import java.util.Optional;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 19:29
 */
public class JJOptionalWriter implements JJObjectWriter{
    @Override
    public JJNode write(Object value, JJWriter masterWriter) {
        Optional v = (Optional)value;
        if(v.isPresent()){
            return masterWriter.write(v.get());
        }
        return JJNodeNull.Null;
    }
}
