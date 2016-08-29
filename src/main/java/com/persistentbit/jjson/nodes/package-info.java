/**
 * <h1>JSON reading/writing</h1>
 *  <p>This package contains all you need to read and write JSON in Java.</p>
 *  <ul>
 *      <li>The java based json representation: JJNode</li>
 *      <li>A json parser: JJParser</li>
 *      <li>A json printer: JJPrinter</li>
 *  </ul>
 *  <h1>JJNode</h1>
 *  <p>The java representation is defined by the interface {@link com.persistentbit.jjson.nodes.JJNode} and the following concrete implementations:</p>
 *  <ul>
 *      <li>{@link com.persistentbit.jjson.nodes.JJNodeObject}: represents a json object value</li>
 *      <li>{@link com.persistentbit.jjson.nodes.JJNodeArray}: represensts a json array value</li>
 *      <li>{@link com.persistentbit.jjson.nodes.JJNodeNumber}: represents a json number value</li>
 *      <li>{@link com.persistentbit.jjson.nodes.JJNodeString}: represents a json string value</li>
 *      <li>{@link com.persistentbit.jjson.nodes.JJNodeBoolean}: represents a json boolean value</li>
 *      <li>{@link com.persistentbit.jjson.nodes.JJNodeNull}: represents a json null value</li>
 *  </ul>
 *  <p>All JJnode implementations are <strong>immutable</strong>.</p>
 *  <p>This means that when you want to change something, a new instance will be created, and the old one will be left as is.</p>
 *  <p>Example:</p>
 *  <pre>{@code
 * //Create a json object : {"greeting":"hello","id":1234}
 * JJNodeObject obj = new JJNodeObject().plus("greeting",new JJNodeString("hello")).plus("id",new JJNodeNumber(1234));
 *
 * //Create a new Json object from the orginal object
 * JJNodeObject changed = obj.plus("greeting",new JJNodeString("Good Morning"));
 *
 * System.out.println(JJPrinter.print(false,obj)); //prints {"greeting":"hello","id":1234}
 *
 * System.out.println(JJPrinter.print(false,changed)); //prints {"greeting":"Good Morning","id":1234}
 *
 * assert obj != changed;  //obj and changed are different instances
 * assert obj.get("greeting").get().asString().get().getValue().equals("hello");
 * assert changed.get("greeting").get().asString().get().getValue().equals("Good Morning");
 *  }</pre>
 *
 */
package com.persistentbit.jjson.nodes;