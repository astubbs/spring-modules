/*
 * Copyright 13/09/2005 (C) Our Community Pty. Ltd. All Rights Reserved.
 *
 * $Id: Person.java,v 1.1 2006/05/25 23:02:17 hueboness Exp $
 */
package org.springmodules.validation.valang.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Person {

    int age = 0;
    String firstName = null;
    String size = null;
    Date dateOfBirth = null;

    int minAge = 18;

    public Person(int age, String firstName) {
        setAge(age);
        setFirstName(firstName);
    }

    public Person(String size) {
        setSize(size);
    }

    public Person(Date dateOfBirth) {
        setDateOfBirth(dateOfBirth);
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getMinAge() {
        return this.minAge;
    }

    public Object[] getSizes() {
        return new Object[] { "S", "M", "L", "XL" };
    }

    public Collection getTags() {
        Collection tags = new ArrayList();
        tags.add("tag1");
        tags.add("tag2");
        return tags;
    }

    public Map getMap() {
        Map map = new HashMap();
        map.put("firstName", "Steven"); map.put("sizes", getSizes());
        return map;
    }

}