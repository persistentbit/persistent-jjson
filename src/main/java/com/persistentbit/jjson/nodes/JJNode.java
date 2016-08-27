package com.persistentbit.jjson.nodes;

import java.util.Optional;

/**
 * JSON Node representation.<br>
 * A json file can be parsed and translated to a JJNode by using {@link JJParser}.<br>
 * A JJNode can be translated to a json file using {@link JJPrinter}.<br>
 *
 * @author Peter Muys
 * @see JJParser
 * @see JJPrinter
 */
public interface JJNode
{
    /**
     * all the Json Node Types
     */
    enum JType {
        jsonArray,jsonBoolean,jsonNull,jsonNumber,jsonObject,jsonString
    }

    /**
     * Get the json Node Type
     * @return The Type
     */
    JType getType();

    /**
     * Get this node typecasted to a JNodeString
     * @return Optional JNodeString
     */
    default Optional<JJNodeString> asString() { return Optional.empty();}

    /**
     * Get this node typecasted to a JNodeBoolean
     * @return Optional JNodeBoolean
     */
    default Optional<JJNodeBoolean> asBoolean() { return Optional.empty();}

    /**
     * Get this node typecasted to a JNodeNull
     * @return Optional JNodeNull
     */
    default Optional<JJNodeNull> asNull() { return Optional.empty();}

    /**
     * Get this node typecasted to a JNodeNumber
     * @return Optional JNodeNumber
     */
    default Optional<JJNodeNumber> asNumber() { return Optional.empty();}

    /**
     * Get this node typecasted to a JJNodeObject
     * @return Optional JJNodeObject
     */
    default Optional<JJNodeObject> asObject() { return Optional.empty();}

    /**
     * Get this node typecasted to a JJNodeArray
     * @return Optional JJNodeArray
     */
    default Optional<JJNodeArray> asArray() { return Optional.empty();}


}
