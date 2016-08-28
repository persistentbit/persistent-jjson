package com.persistentbit.jjson.readers;

/**
 * Created by petermuys on 27/08/16.
 */
@FunctionalInterface
public interface JJObjectReaderSupplier {
    JJObjectReader getReader(Class<?> cls);
}
