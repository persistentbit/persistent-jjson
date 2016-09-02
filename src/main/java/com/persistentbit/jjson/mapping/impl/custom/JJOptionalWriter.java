package com.persistentbit.jjson.mapping.impl.custom;



import com.persistentbit.core.collections.PMap;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeNull;
import com.persistentbit.jjson.nodes.JJNodeObject;

import java.util.Optional;

/**
 * User: petermuys
 * Date: 24/10/15
 * Time: 19:29
 */
public class JJOptionalWriter implements JJObjectWriter {
    @Override
    public JJNode write(Object value, JJWriter masterWriter) {
        Optional v = (Optional)value;
        if(v == null){
            return JJNodeNull.Null;
        }
        if(v.isPresent()){
            return new JJNodeObject(PMap.<String,JJNode>empty().put("optionalValue",masterWriter.write(v.get())));
        }
        return new JJNodeObject(PMap.<String,JJNode>empty().put("optionalEmpty",JJNodeNull.Null));
    }
}
