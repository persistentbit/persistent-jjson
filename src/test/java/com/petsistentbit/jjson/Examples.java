package com.petsistentbit.jjson;

import com.persistentbit.jjson.mapping.JJMapper;
import com.persistentbit.jjson.nodes.JJNode;
import com.persistentbit.jjson.nodes.JJNodeObject;
import com.persistentbit.jjson.nodes.JJParser;
import com.persistentbit.jjson.nodes.JJPrinter;
import com.petsistentbit.jjson.examples.Address;
import com.petsistentbit.jjson.examples.Person;
import com.petsistentbit.jjson.examples.PersonName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Muys
 * @since 29/08/2016
 */
public class Examples {



    public void mapper() {
        //create a default mapper
        JJMapper    mapper = new JJMapper();

        //Create a test java object
        Map<Address.AddressType,Address> addresses = new HashMap<>();
        addresses.put(Address.AddressType.home,new Address("homestreet 1","Gent","Belgium"));
        addresses.put(Address.AddressType.work,new Address("workstreet 2","Nazareth","Belgium"));
        Person person = new Person(1234,new PersonName("Peter","Muys"), Arrays.asList("+32.9.010203","+32 498.12.34.56"), Person.Gender.male,addresses);

        //Convert the person object to a JJNOde json representation object.

        JJNode rootNode = mapper.write(person);

        //cast  the general JJNode to a json object node
        JJNodeObject personObject = rootNode.asObject().orElseThrow();

        String jsonString = JJPrinter.print(true,personObject);

        System.out.println(jsonString);

        JJNode fromJsonString = JJParser.parse(jsonString).orElseThrow();

        Person personFromJSon = mapper.read(fromJsonString,Person.class);

        assert personFromJSon.getGender() == Person.Gender.male;
        assert personFromJSon.getId() == 1234;
        assert personFromJSon.getName().getFirstName().equals("Peter");
        assert personFromJSon.getName().getLastName().equals("Muys");
    }
}
