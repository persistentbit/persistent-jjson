package com.persistentbit.jjson.mapping.impl;

import com.persistentbit.core.collections.*;
import com.persistentbit.jjson.mapping.impl.custom.*;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * TODOC
 * @since 27/08/16
 * @author Peter Muys
 */
public class JJObjectReaderSupplier implements Function<Class<?>, JJObjectReader>{

	private PMap<Class<?>, JJObjectReader> cache = PMap.empty();
	private final PList<Function<Class<?>, JJObjectReader>> suppliers;
	private final Function<Class<?>, JJObjectReader>        fallBack;

	public JJObjectReaderSupplier(PList<Function<Class<?>, JJObjectReader>> suppliers,
								  Function<Class<?>, JJObjectReader> fallBack
	) {
		this.suppliers = suppliers;
		this.fallBack = fallBack;
	}

	public JJObjectReaderSupplier(PList<Function<Class<?>, JJObjectReader>> suppliers) {
		this(suppliers, c -> {
			try {
				Field f = c.getDeclaredField("jsonReader");
				return (JJObjectReader) f.get(null);
			} catch(NoSuchFieldException | IllegalAccessException | ClassCastException e) {
				return new JJReflectionObjectReader(c);
			}
		});
	}

	public JJObjectReaderSupplier() {
		this(PList.empty());
	}

	public JJObjectReaderSupplier addCoreReaders() {
		JJObjectReaderSupplier s = this;


		s = s.withForClass(PByteList.class, new JJPByteListReader());

		s = s.withForClass(ArrayList.class, new JJListReader(ArrayList::new));
		s = s.withForClass(LinkedList.class, new JJListReader(LinkedList::new));
		s = s.withAssignableTo(Set.class, new JJSetReader(LinkedHashSet::new));

		s = s.withAssignableTo(HashSet.class, new JJSetReader(HashSet::new));
		s = s.withAssignableTo(TreeSet.class, new JJSetReader(TreeSet::new));

		s =
			s.withAssignableTo(Map.class, new JJMapReader(LinkedHashMap::new));//Linked so order of items stay the same after writing/reading
		s = s.withForClass(HashMap.class, new JJMapReader(HashMap::new));
		s = s.withForClass(TreeMap.class, new JJMapReader(TreeMap::new));
		s = s.withForClass(LinkedHashMap.class, new JJMapReader(LinkedHashMap::new));
		JJDateReader dr = new JJDateReader();
		s = s.withForClass(Date.class, dr);
		s = s.withForClass(Date.class, dr);
		s = s.withForClass(Timestamp.class, dr);
		s = s.withForClass(Time.class, dr);
		s = s.withForClass(LocalTime.class, dr);
		s = s.withForClass(LocalDateTime.class, dr);
		s = s.withForClass(LocalDate.class, dr);
		s = s.withForClass(ZonedDateTime.class, dr);
		s = s.withForClass(Optional.class, new JJOptionalReader());
		s = s.withForClass(PSet.class, new JJPSetReader(PSet::empty));
		s = s.withForClass(POrderedSet.class, new JJPSetReader(POrderedSet::empty));
		s = s.withForClass(PList.class, new JJPListReader(PList::empty));
		s = s.withForClass(PMap.class, new JJPMapReader(PMap::empty));
		s = s.withForClass(POrderedMap.class, new JJPMapReader(POrderedMap::empty));
		s = s.withAssignableTo(Exception.class, new JJExceptionReader());
		s = s.withAssignableTo(Enum.class, new JJEnumReader());
		s = s.withAssignableTo(List.class, new JJListReader(ArrayList::new));

		return s;
	}


	public JJObjectReader apply(Class<?> cls) {
		JJObjectReader ow = cache.get(cls);
		if(ow != null) {
			return ow;
		}
		for(Function<Class<?>, JJObjectReader> s : suppliers) {
			ow = s.apply(cls);
			if(ow != null) {
				cache = cache.put(cls, ow);
				return ow;
			}
		}
		if(fallBack != null) {
			ow = fallBack.apply(cls);
			cache = cache.put(cls, ow);
			return ow;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public JJObjectReaderSupplier withForClass(Class<?> cls, JJObjectReader ow) {
		return withPrevSupplier(c -> cls.equals(c) ? ow : null);
	}

	@SuppressWarnings("unchecked")
	public JJObjectReaderSupplier withAssignableTo(Class<?> clsAssignableTo, JJObjectReader ow) {
		return withNextSupplier(c -> clsAssignableTo.isAssignableFrom(c) ? ow : null);
	}

	public JJObjectReaderSupplier withNextSupplier(Function<Class<?>, JJObjectReader>... next) {
		PList<Function<Class<?>, JJObjectReader>> res = suppliers;
		for(Function<Class<?>, JJObjectReader> s : next) {
			res = res.plus(s);
		}
		return new JJObjectReaderSupplier(res, fallBack);
	}

	public JJObjectReaderSupplier withPrevSupplier(Function<Class<?>, JJObjectReader>... prev) {
		PList<Function<Class<?>, JJObjectReader>> res = PList.empty();
		for(Function<Class<?>, JJObjectReader> s : prev) {
			res = res.plus(s);
		}
		res = res.plusAll(suppliers);
		return new JJObjectReaderSupplier(res, fallBack);
	}

	public JJObjectReaderSupplier withFallbackSupplier(Function<Class<?>, JJObjectReader> fallBack) {
		return new JJObjectReaderSupplier(suppliers, fallBack);
	}
}

