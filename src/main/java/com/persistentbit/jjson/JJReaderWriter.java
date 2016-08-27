package com.persistentbit.jjson;

import com.persistentbit.core.Immutable;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.readers.JJDefaultReader;
import com.persistentbit.jjson.readers.JJObjectReader;
import com.persistentbit.jjson.readers.JJReader;
import com.persistentbit.jjson.writers.JJDefaultWriter;
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



    public JJReaderWriter withWriteMapper(Function<Object,Object> mapper) {
        return new JJReaderWriter(reader,writer.withMapper(mapper));
    }

    @Override
    public JJNode write(Object value) {
        return writer.write(value);
    }

    @Override
    public <T> T read(JJNode node, Class<T> cls, Type type) {
        return reader.read(node,cls,type);
    }

    public JJReaderWriter withGeneralReader(Class<?> cls, JJObjectReader oreader) {
        reader.addGeneralReader(cls,oreader);
        return this;
    }

    public JJReaderWriter addReader(Class<?> cls, JJObjectReader oreader) {
        reader.addReader(cls,oreader);
        return this;

    }

    public JJReaderWriter addMapper(JJReaderMapper mapper) {
        reader.addMapper(mapper);
        return this;
    }
}
