package com.persistentbit.jjson.mapping;


import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJPrinter;

/**
 * Represents a translater from a Java Object to a Json structure ({@link JJNode}.<br>
 * This structure can than be used to create a Json file: {@link JJPrinter}
 * @author Peter Muys
 * @see JJNode
 * @see JJReader
 * @see JJPrinter
 */
@FunctionalInterface
public interface JJWriter
{


    /**
     * Convert a java object to a Json representation
     * @param value A java object to convert to json
     * @return the {@link JJNode} json representation of the object
     */
    JJNode write(Object value);
}
