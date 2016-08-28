package com.persistentbit.jjson;

import com.persistentbit.core.Immutable;
import com.persistentbit.core.collections.PList;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.readers.JJDefaultReader;
import com.persistentbit.jjson.readers.JJObjectReader;
import com.persistentbit.jjson.readers.JJObjectReaderSupplier;
import com.persistentbit.jjson.readers.JJReader;
import com.persistentbit.jjson.writers.JJDefaultWriter;
import com.persistentbit.jjson.writers.JJObjectWriter;
import com.persistentbit.jjson.writers.JJWriter;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Function;

/**
 * User: petermuys
 * Date: 26/08/16
 * Time: 09:53
 */
@Immutable
public class JJReaderWriter implements JJReader,JJWriter{
    private final JJDefaultReader reader;
    private final JJDefaultWriter writer;

    public JJReaderWriter(JJDefaultReader reader, JJDefaultWriter writer) {
        this.reader = Objects.requireNonNull(reader);
        this.writer = Objects.requireNonNull(writer);
    }
    public JJReaderWriter() {
        this(new JJDefaultReader(),new JJDefaultWriter());
    }



    @Override
    public JJNode write(Object value) {
        return writer.write(value);
    }

    @Override
    public <T> T read(JJNode node, Class<T> cls, Type type) {
        return reader.read(node,cls,type);
    }

    public JJReaderWriter readerWithForClass(Class<?> cls, JJObjectReader ow){
        return new JJReaderWriter(reader.withForClass(cls,ow),writer);
    }

    public JJReaderWriter readerWithAssignableTo(Class<?> clsAssignableTo, JJObjectReader ow){
        return new JJReaderWriter(reader.withAssignableTo(clsAssignableTo,ow),writer);
    }

    public JJReaderWriter readerWithNextSupplier(Function<Class<?>,JJObjectReader>...next){
        return new JJReaderWriter(reader.withNextSupplier(next),writer);
    }

    public JJReaderWriter readerWithPrevSupplier(Function<Class<?>,JJObjectReader>...prev){
        return new JJReaderWriter(reader.withPrevSupplier(prev),writer);
    }

    public JJReaderWriter readerWithFallbackSupplier(Function<Class<?>,JJObjectReader> fallBack){
        return new JJReaderWriter(reader.withFallbackSupplier(fallBack),writer);
    }


    public JJReaderWriter writerWithForClass(Class<?> cls, JJObjectWriter ow){
        return new JJReaderWriter(reader,writer.withForClass(cls,ow));
    }

    public JJReaderWriter writerWithAssignableTo(Class<?> clsAssignableTo, JJObjectWriter ow){
        return new JJReaderWriter(reader,writer.withAssignableTo(clsAssignableTo,ow));
    }

    public JJReaderWriter writerWithNextSupplier(Function<Class<?>,JJObjectWriter>...next){
        return new JJReaderWriter(reader,writer.withNextSupplier(next));
    }

    public JJReaderWriter writerWithPrevSupplier(Function<Class<?>,JJObjectWriter>...prev){
        return new JJReaderWriter(reader,writer.withPrevSupplier(prev));
    }

    public JJReaderWriter writerWithFallbackSupplier(Function<Class<?>,JJObjectWriter> fallBack){
        return new JJReaderWriter(reader,writer.withFallbackSupplier(fallBack));
    }




    public JJReaderWriter withForClass(Class<?> cls, JJObjectReaderWriter rw){
        return new JJReaderWriter(reader.withForClass(cls,rw),writer.withForClass(cls,rw));
    }

    public JJReaderWriter withAssignableTo(Class<?> clsAssignableTo, JJObjectReaderWriter ow){
        return new JJReaderWriter(reader.withAssignableTo(clsAssignableTo,ow),writer);
    }

    public JJReaderWriter withNextSupplier(Function<Class<?>,JJObjectReaderWriter>...next){
        JJReaderWriter res = this;
        for(Function<Class<?>,JJObjectReaderWriter> i : next){
            res = res.readerWithNextSupplier(c ->  i.apply(c));
            res = res.writerWithNextSupplier(c -> i.apply(c));
        }
        return res;

    }

    public JJReaderWriter withPrevSupplier(Function<Class<?>,JJObjectReaderWriter>...prev){
        JJReaderWriter res = this;
        for(Function<Class<?>,JJObjectReaderWriter> i : prev){
            res = res.readerWithPrevSupplier(c ->  i.apply(c));
            res = res.writerWithPrevSupplier(c -> i.apply(c));
        }
        return res;
    }


}
