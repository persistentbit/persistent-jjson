package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.logging.Log;
import com.persistentbit.core.logging.entries.LogEntry;
import com.persistentbit.core.logging.entries.LogEntryEmpty;
import com.persistentbit.core.result.Empty;
import com.persistentbit.core.result.Failure;
import com.persistentbit.core.result.Result;
import com.persistentbit.core.result.Success;
import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeNull;
import com.persistentbit.jjson.nodes.JJNodeObject;
import com.persistentbit.jjson.nodes.JJNodeString;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * TODO: Add comment
 *
 * @author Peter Muys
 * @since 17/01/2017
 */
public class JJResultReaderWriter implements JJObjectReader, JJObjectWriter{
    @Override
    public JJNode write(Object value, JJWriter masterWriter) {
        if(value == null){
            return JJNodeNull.Null;
        }
        Result result =(Result)value;
        result = result.completed();
        JJNodeObject res = new JJNodeObject();
        LogEntry log = result.getLog();
        if(log.isEmpty() == false) {
            res = res.plus("log", masterWriter.write(result.getLog()));
        }
        if(result.isPresent()){
            res = res.plus("type", new JJNodeString("Success"));
            res = res.plus("value",masterWriter.write(result.orElseThrow()));
        } else if(result.isEmpty()){
            Empty empty = (Empty)result;
            res = res.plus("type", new JJNodeString("Empty"));
            res = res.plus("exception", masterWriter.write(empty.getException()));
        } else if(result.isError()){
            Failure failure = (Failure)result;
            res = res.plus("type", new JJNodeString("Failure"));
            res = res.plus("exception", masterWriter.write(failure.getException()));
        } else {
            throw new RuntimeException("Unknown: " + result);
        }
        return res;
    }

    @Override
    public Object read(Type type, JJNode node, JJReader masterReader) {
        return Log.function(type, node).code(l -> {
            if(node.getType() == JJNode.JType.jsonNull) {
                return null;
            }
            if(type instanceof ParameterizedType == false) {
                throw new JJsonException("Expected a parameterized Result, not just a Result");
            }
            ParameterizedType pt       = (ParameterizedType) type;
            Type              itemType = pt.getActualTypeArguments()[0];
            JJNodeObject      obj      = node.asObject().orElseThrow();
            LogEntry          log      =
                obj.get("log").map(logNode -> masterReader.read(logNode, LogEntry.class)).orElse(LogEntryEmpty.inst);

            switch(obj.get("type").get().asString().orElseThrow().getValue()) {
                case "Success":
                    Object value =
                        masterReader.read(obj.get("value").get(), ReflectionUtils.classFromType(itemType), itemType);
                    return new Success(value, log);
                case "Empty":
                    Throwable emptyException =
                        (Throwable) masterReader.read(obj.get("exception").get(), Throwable.class);
                    return new Empty(emptyException, log);
                case "Failure":
                    Throwable failureException =
                        (Throwable) masterReader.read(obj.get("exception").get(), Throwable.class);
                    return new Failure(failureException, log);
                default:
                    throw new RuntimeException("Unknown: " + obj.get("type").get());
            }
        });
    }
}
