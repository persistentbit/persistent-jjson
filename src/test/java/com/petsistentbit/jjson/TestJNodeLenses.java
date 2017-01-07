package com.petsistentbit.jjson;

import com.persistentbit.core.testing.TestCase;
import com.persistentbit.core.testing.TestRunner;
import com.persistentbit.jjson.mapping.JJMapper;
import com.persistentbit.jjson.nodes.*;


/**
 * @author Peter Muys
 * @since 29/08/2016
 */
public class TestJNodeLenses {

	static final TestCase testJJNodeLenses = TestCase.name("Test JJNode Lens updates").code(tr -> {
		JJMapper rw = new JJMapper();
        JJNode json = rw.write(new JJSubTest(0,"unknown")); //creates {"id":0,"name":"unknown"}
        JJNodeObject    root = json.asObject().orElseThrow();
		//tr.info(JJPrinter.print(false,root));

        root = JJNodeObject.arrayLens("test").set(root,new JJNodeArray(new JJNodeString("item1"),new JJNodeString("item2")));
		//tr.info(JJPrinter.print(false,root));

        //Create a json object : {"greeting":"hello","id":1234}
        JJNodeObject obj = new JJNodeObject().plus("greeting",new JJNodeString("hello")).plus("id",new JJNodeNumber(1234));
        //Create a new Json object from the orginal object
        JJNodeObject changed = obj.plus("greeting",new JJNodeString("Good Morning"));

		//tr.info(JJPrinter.print(false,obj)); //prints {"greeting":"hello","id":1234}
		//tr.info(JJPrinter.print(false,changed)); //prints {"greeting":"Good Morning","id":1234}
		tr.isTrue(obj != changed);  //obj and changed are different instances
		tr.isEquals(obj.get("greeting").get().asString().orElseThrow().getValue(), "hello");
		tr.isEquals(changed.get("greeting").get().asString().orElseThrow().getValue(), "Good Morning");
	});

	public void testAll() {
		TestRunner.runAndPrint(TestJNodeLenses.class);
	}

	public static void main(String[] args) {
		new TestJNodeLenses().testAll();
	}
}
