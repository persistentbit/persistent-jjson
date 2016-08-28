package com.persistentbit.jjson.writers;

import java.util.function.Function;

/**
 * Created by petermuys on 27/08/16.
 */
@FunctionalInterface
public interface JJObjectWriterSupplier {

    JJObjectWriter getWriterForClass(Class<?> cls);
}
