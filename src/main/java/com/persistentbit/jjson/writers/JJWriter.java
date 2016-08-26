package com.persistentbit.jjson.writers;


import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.jjson.nodes.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author Peter Muys
 * @since 22/10/2015
 */
public interface JJWriter
{
    JJWriter addMapper(JJWriterObjectMapper mapper);
    JJNode write(Object value);
}
