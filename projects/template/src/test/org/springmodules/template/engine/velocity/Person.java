/*
 * Copyright (c) 2007, Your Corporation. All Rights Reserved.
 */

package org.springmodules.template.engine.velocity;

/**
 * @author Uri Boness
 */
public class Person {

    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
