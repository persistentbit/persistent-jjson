package com.persistentbit.jjson;

import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.readers.JJDefaultReader;
import com.persistentbit.jjson.readers.JJObjectReader;
import com.persistentbit.jjson.readers.JJReader;
import com.persistentbit.jjson.readers.JJReaderMapper;
import com.persistentbit.jjson.writers.JJDefaultWriter;
import com.persistentbit.jjson.writers.JJWriter;
import com.persistentbit.jjson.writers.JJWriterObjectMapper;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * User: petermuys
 * Date: 26/08/16
 * Time: 09:53
 */
public class JJReaderWriter implements JJReader,JJWriter{
    private final JJReader  reader;
    private final JJWriter writer;

    public JJReaderWriter(JJReader reader, JJWriter writer) {
        this.reader = Objects.requireNonNull(reader);
        this.writer = Objects.requireNonNull(writer);
    }
    public JJReaderWriter() {
        this(new JJDefaultReader(),new JJDefaultWriter());
    }


    @Override
    public JJReaderWriter addMapper(JJWriterObjectMapper mapper) {
        writer.addMapper(mapper);
        return this;
    }

    @Override
    public JJNode write(Object value) {
        return writer.write(value);
    }

    @Override
    public <T> T read(JJNode node, Class<T> cls, Type type) {
        return reader.read(node,cls,type);
    }

    @Override
    public JJReaderWriter addGeneralReader(Class<?> cls, JJObjectReader oreader) {
        reader.addGeneralReader(cls,oreader);
        return this;
    }

    @Override
    public JJReaderWriter addReader(Class<?> cls, JJObjectReader oreader) {
        reader.addReader(cls,oreader);
        return this;

    }

    @Override
    public JJReaderWriter addMapper(JJReaderMapper mapper) {
        reader.addMapper(mapper);
        return this;
    }
}
