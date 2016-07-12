package com.persistentbit.jjson.nodes;

import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PStream;
import com.persistentbit.core.collections.PStreamLazy;
import com.persistentbit.core.collections.PStreamable;


import java.util.*;

/**
 * Represents a JJNode for an array.<br>
 * @author Peter Muys
 * @since 21/10/2015
 */
public class JJNodeArray implements Iterable<JJNode>,JJNode,PStreamable
{
    private final PList<JJNode> elements;


    /**
     * create an empty JJNodeArray
     */
    public JJNodeArray() {
        this.elements = PList.empty();
    }


    /**
     * Initialize this node with an Iterable
     * @param nodes the initial nodes
     */
    public JJNodeArray(Iterable<JJNode> nodes){
        this.elements = PStream.from(Objects.requireNonNull(nodes)).plist();
    }


    /**
     * Initialize this node with the provided elements
     * @param elements The array elements
     */
    public JJNodeArray(JJNode...elements){
        this.elements = PList.val(elements);
    }

    @Override
    public <T> PStream<T> asPStream() {
        return (PStream<T>)elements;
    }

    @Override
    public Iterator<JJNode> iterator()
    {
        return elements.iterator();
    }

    /**
     * Create a new JJNodeArray with the provided node added
     * @param node The node to add
     * @return a new JJNodeArray
     */
    public JJNodeArray plus(JJNode node){
        return new JJNodeArray(elements.plus(Objects.requireNonNull(node)));
    }
    @Override
    public String toString() {
        return JJPrinter.print(false,this);
    }


    @Override
    public JType getType()
    {
        return JType.jsonArray;
    }



    @Override
    public Optional<JJNodeArray> asArray()
    {
        return Optional.of(this);
    }
}
