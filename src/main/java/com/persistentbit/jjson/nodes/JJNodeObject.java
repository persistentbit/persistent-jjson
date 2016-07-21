package com.persistentbit.jjson.nodes;


import com.persistentbit.core.Tuple2;
import com.persistentbit.core.collections.*;
import com.persistentbit.core.lenses.Lens;
import com.persistentbit.core.lenses.LensImpl;


import java.util.*;

/**
 * A JJNode representing a json Object.
 * @author Peter Muys
 * @since 21/10/2015
 */
public class JJNodeObject implements JJNode,PStreamable<Tuple2<String,JJNode>>
{
    private final IPMap<String,JJNode> items;

    /**
     * Create a new instance with the provided propery map
     * @param items
     */
    public JJNodeObject(IPMap<String, JJNode> items)
    {
        this.items = Objects.requireNonNull(items);
    }


    public JJNodeObject() {
        this(POrderedMap.empty());
    }


    public Optional<JJNode> get(String name){
        return Optional.ofNullable(items.get(name));
    }

    public IPMap<String,JJNode> getValue() {
        return items;
    }

    public JJNodeObject plus(String name, JJNode node){
        return new JJNodeObject(items.put(name,node));
    }

    @Override
    public JType getType()
    {
        return JType.jsonObject;
    }

    @Override
    public String toString() {
        return JJPrinter.print(false,this);
    }

    @Override
    public Optional<JJNodeObject> asObject()
    {
        return Optional.of(this);
    }


    @Override
    public PStream<Tuple2<String,JJNode>> pstream() {
        return items;
    }

    public static Lens<JJNodeObject,JJNodeString> stringLens(String name){
        return createLens(JJNodeString.class,name);
    }
    public static Lens<JJNodeObject,JJNodeNumber> numberLens(String name){
        return createLens(JJNodeNumber.class,name);
    }

    public static Lens<JJNodeObject,JJNodeObject> objectLens(String name){
        return createLens(JJNodeObject.class,name);
    }
    public static Lens<JJNodeObject,JJNodeArray> arrayLens(String name){
        return createLens(JJNodeArray.class,name);
    }
    public static Lens<JJNodeObject,JJNodeBoolean> booleanLens(String name){
        return createLens(JJNodeBoolean.class,name);
    }
    public static Lens<JJNodeObject,JJNode> nodeLens(String name){
        return createLens(JJNode.class,name);
    }

    private static <C extends JJNode> Lens<JJNodeObject,C> createLens(Class<C> cls, String name){
        return new LensImpl<>((p)->orNull(cls,p.get(name).orElse(null)),
                (p,c)-> {
                    IPMap<String,JJNode> nm = p.items;
                    nm = nm.put(name,c);
                    return new JJNodeObject(nm);
                });
    }
    private static <C extends JJNode> C orNull(Class<C> cls, JJNode value){
        if(value == null || value instanceof JJNodeNull){
            return null;
        }
        if(cls != value.getClass()){
            throw new RuntimeException("Expected " + cls + ",  got" + value.getClass() + " : " + value);
        }
        return (C)value;
    }

}
