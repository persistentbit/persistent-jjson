package com.persistentbit.jjson.nodes;

import com.persistentbit.core.result.Result;

import java.util.function.Function;

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
    default Result<JJNodeString> asString() {
        return Result.empty();
    }

    /**
     * Get this node typecasted to a JNodeBoolean
     * @return Optional JNodeBoolean
     */
    default Result<JJNodeBoolean> asBoolean() { return Result.empty();}

    /**
     * Get this node typecasted to a JNodeNull
     * @return Optional JNodeNull
     */
    default Result<JJNodeNull> asNull() { return Result.empty();}

    /**
     * Get this node typecasted to a JNodeNumber
     * @return Optional JNodeNumber
     */
    default Result<JJNodeNumber> asNumber() { return Result.empty();}

    /**
     * Get this node typecasted to a JJNodeObject
     * @return Optional JJNodeObject
     */
    default Result<JJNodeObject> asObject() { return Result.empty();}

    /**
     * Get this node typecasted to a JJNodeArray
     * @return Optional JJNodeArray
     */
    default Result<JJNodeArray> asArray() { return Result.empty();}


    <T> T match(
        Function<JJNodeArray, T> anArray,
        Function<JJNodeBoolean, T> aBoolean,
        Function<JJNodeNull, T> aNull,
        Function<JJNodeNumber, T> aNumber,
        Function<JJNodeObject, T> anObject,
        Function<JJNodeString, T> aString
    );

}
