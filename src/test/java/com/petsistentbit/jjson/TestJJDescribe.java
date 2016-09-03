package com.petsistentbit.jjson;

import com.persistentbit.core.Tuple2;
import com.persistentbit.jjson.mapping.JJMapper;
import com.persistentbit.jjson.mapping.description.JJTypeDescription;
import com.persistentbit.jjson.nodes.JJPrinter;
import com.petsistentbit.jjson.examples.GenericsTest;
import org.testng.annotations.Test;

/**
 * @author Peter Muys
 * @since 31/08/2016
 */
public class TestJJDescribe {
    @Test
    public void test1() {
        JJMapper m = new JJMapper();
        JJTypeDescription td = m.describe(JJTest.class);
        System.out.println(JJPrinter.print(true,m.write(td)));
        System.out.println("All classes:" + td.getAllUsedClassNames());
        //System.out.println(JJPrinter.print(true,m.write(m.describe(JJTest.EnumTest.class))));
        //System.out.println(JJPrinter.print(true,m.write(m.describe(Tuple2.class))));
        System.out.println(JJPrinter.print(true,m.write(m.describe(GenTest.class))));
    }

    @Test
    public void testGenerics() {
        JJMapper m = new JJMapper();
        JJTypeDescription td = m.describe(GenericsTest.class);
        System.out.println(JJPrinter.print(true,m.write(td)));
    }
}
