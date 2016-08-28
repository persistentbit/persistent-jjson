package com.persistentbit.jjson.readers;

import com.persistentbit.core.collections.IPList;
import com.persistentbit.core.collections.PList;
import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * User: petermuys
 * Date: 25/08/16
 * Time: 19:01
 */
public class JJPListReader  implements JJObjectReader{

    private Supplier<IPList> supplier;

    public JJPListReader(Supplier<IPList> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Object read(Type type, JJNode node, JJReader reader) {
        if(node.getType() == JJNode.JType.jsonNull){
            return null;
        }
        if(type instanceof ParameterizedType == false){
            throw new RuntimeException("Expected a parameterized PList, not just a PList");
        }
        ParameterizedType pt  = (ParameterizedType)type;
        Type itemType = pt.getActualTypeArguments()[0];
        Class cls = ReflectionUtils.classFromType(itemType);
        JJNodeArray arr = node.asArray().get();
        return  supplier.get().plusAll(arr.pstream().map(n -> reader.read(n,cls,itemType)));
    }
}
