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
 * Represents a translater from a Java Object to a Json structure ({@link JJNode}.<br>
 * This structure can than be used to create a Json file: {@link JJPrinter}
 * @author Peter Muys
 * @see JJNode
 * @see com.persistentbit.jjson.readers.JJReader
 * @see JJPrinter
 */
public interface JJWriter
{


    /**
     * Convert a java object to a Json representation
     * @param value
     * @return
     */
    JJNode write(Object value);
}
