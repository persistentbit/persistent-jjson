package com.persistentbit.jjson.mapping;

import com.persistentbit.core.Immutable;
import com.persistentbit.jjson.mapping.impl.*;
import com.persistentbit.jjson.nodes.JJNode;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Function;

/**
 * Mapper between Java Objects and {@link JJNode} json representations.<br>
 * This mapper is a combination of a {@link JJReader} and a {@link JJWriter}  implemented
 * by a {@link JJDefaultReader} and {@link JJDefaultWriter}.<br>
 *
 */
@Immutable
public class JJMapper implements JJReader,JJWriter{
    private final JJDefaultReader reader;
    private final JJDefaultWriter writer;

    /**
     * Construct this mapper with custom {@link JJDefaultReader} and {@link JJDefaultWriter} instances.
     * @param reader The reader to use
     * @param writer The writer to use
     */
    public JJMapper(JJDefaultReader reader, JJDefaultWriter writer) {
        this.reader = Objects.requireNonNull(reader);
        this.writer = Objects.requireNonNull(writer);
    }
    public JJMapper() {
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

    public JJMapper readerWithForClass(Class<?> cls, JJObjectReader ow){
        return new JJMapper(reader.withForClass(cls,ow),writer);
    }

    public JJMapper readerWithAssignableTo(Class<?> clsAssignableTo, JJObjectReader ow){
        return new JJMapper(reader.withAssignableTo(clsAssignableTo,ow),writer);
    }

    public JJMapper readerWithNextSupplier(Function<Class<?>,JJObjectReader>...next){
        return new JJMapper(reader.withNextSupplier(next),writer);
    }

    public JJMapper readerWithPrevSupplier(Function<Class<?>,JJObjectReader>...prev){
        return new JJMapper(reader.withPrevSupplier(prev),writer);
    }

    public JJMapper readerWithFallbackSupplier(Function<Class<?>,JJObjectReader> fallBack){
        return new JJMapper(reader.withFallbackSupplier(fallBack),writer);
    }


    public JJMapper writerWithForClass(Class<?> cls, JJObjectWriter ow){
        return new JJMapper(reader,writer.withForClass(cls,ow));
    }

    public JJMapper writerWithAssignableTo(Class<?> clsAssignableTo, JJObjectWriter ow){
        return new JJMapper(reader,writer.withAssignableTo(clsAssignableTo,ow));
    }

    public JJMapper writerWithNextSupplier(Function<Class<?>,JJObjectWriter>...next){
        return new JJMapper(reader,writer.withNextSupplier(next));
    }

    public JJMapper writerWithPrevSupplier(Function<Class<?>,JJObjectWriter>...prev){
        return new JJMapper(reader,writer.withPrevSupplier(prev));
    }

    public JJMapper writerWithFallbackSupplier(Function<Class<?>,JJObjectWriter> fallBack){
        return new JJMapper(reader,writer.withFallbackSupplier(fallBack));
    }




    public JJMapper withForClass(Class<?> cls, JJObjectReaderWriter rw){
        return new JJMapper(reader.withForClass(cls,rw),writer.withForClass(cls,rw));
    }

    public JJMapper withAssignableTo(Class<?> clsAssignableTo, JJObjectReaderWriter ow){
        return new JJMapper(reader.withAssignableTo(clsAssignableTo,ow),writer);
    }

    public JJMapper withNextSupplier(Function<Class<?>,JJObjectReaderWriter>...next){
        JJMapper res = this;
        for(Function<Class<?>,JJObjectReaderWriter> i : next){
            res = res.readerWithNextSupplier(c ->  i.apply(c));
            res = res.writerWithNextSupplier(c -> i.apply(c));
        }
        return res;

    }

    public JJMapper withPrevSupplier(Function<Class<?>,JJObjectReaderWriter>...prev){
        JJMapper res = this;
        for(Function<Class<?>,JJObjectReaderWriter> i : prev){
            res = res.readerWithPrevSupplier(c ->  i.apply(c));
            res = res.writerWithPrevSupplier(c -> i.apply(c));
        }
        return res;
    }


}
