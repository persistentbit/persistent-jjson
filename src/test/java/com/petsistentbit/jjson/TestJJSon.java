package com.petsistentbit.jjson;

import com.persistentbit.core.collections.PList;
import com.persistentbit.core.collections.PMap;
import com.persistentbit.core.collections.PSet;
import com.persistentbit.jjson.JJReaderWriter;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJPrinter;
import com.persistentbit.jjson.readers.JJDefaultReader;
import com.persistentbit.jjson.readers.JJReader;
import com.persistentbit.jjson.writers.JJDefaultWriter;
import com.persistentbit.jjson.writers.JJWriter;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: petermuys
 * Date: 25/08/16
 * Time: 18:07
 */

public class TestJJSon {

    @Test
    public void test1() {

        JJTest t1 = new JJTest(
                Arrays.asList(new JJSubTest(1,"Peter"),new JJSubTest(2,"Muys")),
                true,
                (short)1234,
                56578910,
                99999999999L,
                1.23f,
                4.5678,
                new Date(),
                PList.forString().plusAll("Hello","World","From","Peter"),
                PMap.<Integer,String>empty().put(1,"Peter").put(2,"Muys"),
                PSet.<Double>empty().plusAll(1.2,2.3,3.4),
                PMap.<String,String>empty().put("prop1","value1").put("prop2","value2").map()
        );
        JJReaderWriter  rw = new JJReaderWriter();
        JJNode node = rw.write(t1);
        System.out.println(JJPrinter.print(true,node));
        JJTest t2 = rw.read(node,JJTest.class);
        JJNode node2 = rw.write(t2);
        System.out.println(JJPrinter.print(true,node2));
        assert node.equals(node2);
        assert t1.equals(t2);

    }
}
