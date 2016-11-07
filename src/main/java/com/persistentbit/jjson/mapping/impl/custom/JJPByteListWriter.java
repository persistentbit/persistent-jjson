package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.PByteList;
import com.persistentbit.jjson.mapping.JJWriter;
import com.persistentbit.jjson.mapping.impl.JJObjectWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeString;

/**
 * Json {@link PByteList} writer
 *
 * @author petermuys
 * @see JJPByteListReader
 * @since 7/11/16
 */
public class JJPByteListWriter implements JJObjectWriter{

	@Override
	public JJNode write(Object value, JJWriter masterWriter) {
		PByteList v = (PByteList) value;
		return new JJNodeString(v.toBase64String());

	}
}