package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.IPMap;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

/**
 * General PMap Writer.<br>
 * Writes out an array of entrie Arrays with [key,value]
 * @author Peter Muys
 * @since 25/08/16
 */
public class JJPMapWriter implements JJObjectWriter{

	@SuppressWarnings("unchecked")
	@Override
	public JJNode write(Object value, JJWriter masterWriter) {
		IPMap<Object, Object> v = (IPMap) value;
		return new JJNodeArray(
									  v.lazy().map(e -> new JJNodeArray(masterWriter.write(e._1), masterWriter.write(e._2)))
		);
	}
}