package com.persistentbit.jjson.mapping.impl.custom;

import com.persistentbit.core.collections.IPMap;
import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.tuples.Tuple2;
import com.persistentbit.core.utils.ReflectionUtils;
import com.persistentbit.jjson.mapping.JJReader;
import com.persistentbit.jjson.mapping.description.JJClass;
import com.persistentbit.jjson.mapping.description.JJTypeDescription;
import com.persistentbit.jjson.mapping.description.JJTypeSignature;
import com.persistentbit.jjson.mapping.impl.JJDescriber;
import com.persistentbit.jjson.mapping.impl.JJObjectReader;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * General PMap Reader, reading an array of entry arrays [key,value]
 *
 * @author Peter Muys
 * @since 26/08/16
 */
public class JJPMapReader implements JJObjectReader, JJDescriber{

	private final Supplier<IPMap> ipMapSupplier;

	public JJPMapReader(Supplier<IPMap> ipMapSupplier) {
		this.ipMapSupplier = ipMapSupplier;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object read(Type type, JJNode node, JJReader reader) {
		if (node.getType() == JJNode.JType.jsonNull) {
			return null;
		}
		if (type instanceof ParameterizedType == false) {
			throw new JJsonException("Expected a parameterized PMap, not just a PMap");
		}
		ParameterizedType pt        = (ParameterizedType) type;
		Type[]            typeArgs  = pt.getActualTypeArguments();
		Type              keyType   = typeArgs[0];
		Type              valueType = typeArgs[1];
		Class             clsKey    = ReflectionUtils.classFromType(keyType);
		Class             clsValue  = ReflectionUtils.classFromType(valueType);
		JJNodeArray       arr       = node.asArray().orElseThrow();
		return ipMapSupplier.get().plusAll(arr.pstream().map(n -> {
			JJNodeArray entryArr = n.asArray().orElseThrow(() -> new RuntimeException("Expected an array"));
			Object      key      = reader.read(entryArr.pstream().get(0), clsKey, keyType);
			Object      value    = reader.read(entryArr.pstream().get(1), clsValue, valueType);
			return new Tuple2(key, value);
		}));
	}

	@Override
	public JJTypeDescription describe(Type t, JJDescriber masterDescriber) {
		Class cls = ReflectionUtils.classFromType(t);


		PMap<String, JJTypeSignature> td  = JJDescriber.getGenericsParams(t, masterDescriber);
		JJTypeSignature               sig = new JJTypeSignature(new JJClass(cls), JJTypeSignature.JsonType.jsonMap, td);
		PList<String>                 doc = PList.empty();

		return new JJTypeDescription(sig, doc);
	}
}
