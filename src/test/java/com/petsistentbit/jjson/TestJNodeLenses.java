package com.petsistentbit.jjson;

import com.persistentbit.jjson.mapping.JJMapper;
import com.persistentbit.jjson.nodes.*;
import org.junit.Test;


/**
 * @author Peter Muys
 * @since 29/08/2016
 */
public class TestJNodeLenses {
    @Test
    public void testJNodeUpdates() {
        JJMapper rw = new JJMapper();
        JJNode json = rw.write(new JJSubTest(0,"unknown")); //creates {"id":0,"name":"unknown"}
        JJNodeObject    root = json.asObject().get();
        System.out.println(JJPrinter.print(false,root));

        root = JJNodeObject.arrayLens("test").set(root,new JJNodeArray(new JJNodeString("item1"),new JJNodeString("item2")));
        System.out.println(JJPrinter.print(false,root));

        //Create a json object : {"greeting":"hello","id":1234}
        JJNodeObject obj = new JJNodeObject().plus("greeting",new JJNodeString("hello")).plus("id",new JJNodeNumber(1234));
        //Create a new Json object from the orginal object
        JJNodeObject changed = obj.plus("greeting",new JJNodeString("Good Morning"));

        System.out.println(JJPrinter.print(false,obj)); //prints {"greeting":"hello","id":1234}
        System.out.println(JJPrinter.print(false,changed)); //prints {"greeting":"Good Morning","id":1234}
        assert obj != changed;  //obj and changed are different instances
        assert obj.get("greeting").get().asString().get().getValue().equals("hello");
        assert changed.get("greeting").get().asString().get().getValue().equals("Good Morning");
    }
}
