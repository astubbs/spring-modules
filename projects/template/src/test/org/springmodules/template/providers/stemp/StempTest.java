package org.springmodules.template.providers.stemp;
/**
 * @author Uri Boness
 */

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import junit.framework.*;
import org.springmodules.template.providers.stemp.resolvers.*;
import org.w3c.dom.*;

public class StempTest extends TestCase {

    public void testWithOgnlExpressionResolver() throws Exception {
        String template = "My name is ${person.firstName} ${person.lastName}, and I'm ${person.age} years old.";
        String expectedResult = "My name is John Lennon, and I'm 40 years old.";

        Person person = new Person("John", "Lennon", 40);
        Map model = new HashMap();
        model.put("person", person);

        Stemp stemp = Stemp.compile(new StringReader(template), new OgnlExpressionResolver());

        assertEquals(template, stemp.dump());
        assertEquals(expectedResult, stemp.generate(model));
        assertEquals(3, stemp.getExpressions().length);
    }

    public void testWithDefaultExpressionResolver() throws Exception {

        String template = "My name is ${firstName} ${lastName}, and I'm ${age} years old.";
        String expectedResult = "My name is John Lennon, and I'm 40 years old.";

        Map model = new HashMap();
        model.put("firstName", "John");
        model.put("lastName", "Lennon");
        model.put("age", "40");

        Stemp stemp = Stemp.compile(new StringReader(template));

        assertEquals(template, stemp.dump());
        assertEquals(expectedResult, stemp.generate(model));
        assertEquals(3, stemp.getExpressions().length);
    }

    public void testWithDefaultExpressionResolverAndLineBreaks() throws Exception {

        String template = "My name is ${firstName} ${lastName}, \nand I'm ${age} years old.";
        String expectedResult = "My name is John Lennon, \nand I'm 40 years old.";

        Map model = new HashMap();
        model.put("firstName", "John");
        model.put("lastName", "Lennon");
        model.put("age", "40");

        Stemp stemp = Stemp.compile(new StringReader(template));

        assertEquals(template, stemp.dump());
        assertEquals(expectedResult, stemp.generate(model));
        assertEquals(3, stemp.getExpressions().length);
    }

    public void testWithStempELExpressionResolver() throws Exception {

        String template = "My name is ${person.firstName} ${person.lastName}, and I'm ${person.age} years old.";
        String expectedResult = "My name is John Lennon, and I'm 40 years old.";

        Person person = new Person("John", "Lennon", 40);
        Map model = new HashMap();
        model.put("person", person);

        Stemp stemp = Stemp.compile(new StringReader(template), new StempelExpressionResolver());

        assertEquals(template, stemp.dump());
        assertEquals(expectedResult, stemp.generate(model));
        assertEquals(3, stemp.getExpressions().length);
    }

    public void testWithBeanWrapperExpressionResolver() throws Exception {

        String template = "My name is ${person.firstName} ${person.lastName}, and I'm ${person.age} years old.";
        String expectedResult = "My name is John Lennon, and I'm 40 years old.";

        Person person = new Person("John", "Lennon", 40);
        Map model = new HashMap();
        model.put("person", person);

        Stemp stemp = Stemp.compile(new StringReader(template), new BeanWrapperExpressionResolver());

        assertEquals(template, stemp.dump());
        assertEquals(expectedResult, stemp.generate(model));
        assertEquals(3, stemp.getExpressions().length);
    }

    public void testWithXPathExpressionResolverWithoutConverter() throws Exception {

        String template = "My name is ${/person/firstName} ${/person/lastName}, and I'm ${/person/age} years old.";
        String expectedResult = "My name is John Lennon, and I'm 40 years old.";

        Person person = new Person("John", "Lennon", 40);
        Document document = personToDocument(person);

        Map model = new HashMap();
        model.put("document", document);

        Stemp stemp = Stemp.compile(new StringReader(template), new XPathExpressionResolver());

        assertEquals(template, stemp.dump());
        assertEquals(expectedResult, stemp.generate(model));
        assertEquals(3, stemp.getExpressions().length);
    }

    public void testWithXPathExpressionResolverWithConverter() throws Exception {

        String template = "My name is ${/person/firstName} ${/person/lastName}, and I'm ${/person/age} years old.";
        String expectedResult = "My name is John Lennon, and I'm 40 years old.";

        Person person = new Person("John", "Lennon", 40);

        Map model = new HashMap();
        model.put("person", person);

        Stemp stemp = Stemp.compile(new StringReader(template), new XPathExpressionResolver(createPersonConverter()));

        assertEquals(template, stemp.dump());
        assertEquals(expectedResult, stemp.generate(model));
        assertEquals(3, stemp.getExpressions().length);
    }


    //============================================= Helper Methods =====================================================

    private static Document personToDocument(Person person) throws Exception {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("person");
        doc.appendChild(root);
        Element firstName = doc.createElement("firstName");
        Element lastName = doc.createElement("lastName");
        Element age = doc.createElement("age");
        root.appendChild(firstName);
        root.appendChild(lastName);
        root.appendChild(age);

        firstName.appendChild(doc.createTextNode(person.getFirstName()));
        lastName.appendChild(doc.createTextNode(person.getLastName()));
        age.appendChild(doc.createTextNode(String.valueOf(person.getAge())));

        return doc;
    }

    private static ModelToDomConverter createPersonConverter() {
        return new ModelToDomConverter() {
            public Document convert(Map model) {
                try {
                    Person person = (Person) model.get("person");
                    return personToDocument(person);
                } catch (Exception e) {
                    throw new ModelConversionException("Could not conver person to document", e);
                }
            }
        };
    }


    // test bean
    private static class Person {

        private String firstName;
        private String lastName;
        private int age;

        public Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}