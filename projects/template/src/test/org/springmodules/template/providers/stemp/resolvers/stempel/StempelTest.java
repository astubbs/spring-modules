package org.springmodules.template.providers.stemp.resolvers.stempel;
/**
 * @author Uri Boness
 */

import java.util.*;

import junit.framework.*;

public class StempelTest extends TestCase {

    private Map model;

    protected void setUp() throws Exception {

        Person john = new Person();
        john.setName("John");
        john.setAge(40);

        Person person = new Person();
        person.setName("Jimi");
        person.setAge(27);
        Address mainAddress = new Address("street", "city");
        person.setMainAddress(mainAddress);
        Address[] addresses = new Address[] {
            new Address("street2", "city2")
        };
        person.setAddresses(addresses);
        Map attributes = new HashMap();
        attributes.put("music", "rock");
        attributes.put("gender", "male");
        attributes.put(new Integer(3), "star");
        person.setAttributes(attributes);
        List friends = new ArrayList();
        friends.add(john);
        person.setFriends(friends);

        AttributeKey key = new AttributeKey();
        key.setId(3);

        model = new HashMap();
        model.put("person", person);
        model.put("key", key);
    }

    public void testEvaluateOfSimpleProperties() throws Exception {
        String expression = "person.age";
        Object result = Stempel.evaluate(expression, model);
        assertTrue(result instanceof Integer);
        assertEquals(27, ((Integer)result).intValue());
    }

    public void testEvaluateOfChainedSimpleProperties() throws Exception {
        String expression = "person.mainAddress.city";
        Object result = Stempel.evaluate(expression, model);
        assertEquals("city", result);
    }

    public void testEvaluateOfListIndexedProperties() throws Exception {
        String expression = "person.friends[0]";
        Object result = Stempel.evaluate(expression, model);
        assertTrue(result instanceof Person);
        assertEquals("John", ((Person)result).getName());
    }

    public void testEvaluateOfArrayIndexedProperties() throws Exception {
        String expression = "person.addresses[0]";
        Object result = Stempel.evaluate(expression, model);
        assertTrue(result instanceof Address);
        assertEquals("street2", ((Address)result).getStreet());
    }

    public void testEvaluateOfMapProperties() throws Exception {
        String expression = "person.attributes['music']";
        Object result = Stempel.evaluate(expression, model);
        assertEquals("rock", result);
    }

    public void testEvaluateOfMapPropertiesWithQuotes() throws Exception {
        String expression = "person.attributes[\"music\"]";
        Object result = Stempel.evaluate(expression, model);
        assertEquals("rock", result);
    }

    public void testEvaluateOfMapPropertiesWithEmbeddedExpression() throws Exception {
        String expression = "person.attributes[key.id]";
        Object result = Stempel.evaluate(expression, model);
        assertEquals("star", result);
    }

    public static class Person {

        private String name;
        private int age;
        private Address mainAddress;
        private Address[] addresses;
        private Map attributes;
        private List friends;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Map getAttributes() {
            return attributes;
        }

        public void setAttributes(Map attributes) {
            this.attributes = attributes;
        }

        public List getFriends() {
            return friends;
        }

        public void setFriends(List friends) {
            this.friends = friends;
        }

        public Address getMainAddress() {
            return mainAddress;
        }

        public void setMainAddress(Address mainAddress) {
            this.mainAddress = mainAddress;
        }

        public Address[] getAddresses() {
            return addresses;
        }

        public void setAddresses(Address[] addresses) {
            this.addresses = addresses;
        }
    }

    private static class Address {

        private String street;
        private String city;

        public Address() {
        }

        public Address(String street, String city) {
            this.street = street;
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    }

    private static class AttributeKey {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}