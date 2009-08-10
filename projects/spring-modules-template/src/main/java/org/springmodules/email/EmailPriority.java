/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.email;

/**
 * An enumeration to represent email priorities. This class is structured as a Java 5 enum where each static entry
 * has a rank and a name. The name is typically the name of the static variable (as in Java 5) and the rank represents
 * the actual value of the priority. It is possible to resolve a specific priority by calling the
 * <code>{@link #name(String)}</code> static method (again, as in Java 5). There are 5 possible priorities which is
 * based on the most common email clients support.
 *
 * @author Uri Boness
 */
public class EmailPriority {

    /**
     * Represents the highest priority.
     */
    public final static EmailPriority HIGHEST = new EmailPriority(1, "HIGHEST");

    /**
     * Represents the second highest priority.
     */
    public final static EmailPriority HIGH = new EmailPriority(2, "HIGH");

    /**
     * Represents the normal priority (also used as the default in {@link Email}).
     */
    public final static EmailPriority NORMAL = new EmailPriority(3, "NORMAL");

    /**
     * Represents a low priority.
     */
    public final static EmailPriority LOW = new EmailPriority(4, "LOW");

    /**
     * Represents the lowest possible priority.
     */
    public final static EmailPriority LOWEST = new EmailPriority(5, "LOWEST");

    // all possible priorities.
    private final static EmailPriority[] priorities = {
        HIGHEST, HIGH, NORMAL, LOW, LOWEST
    };

    private final int rank;
    private final String name;

    // constructs a new email priority with a given rank and name.
    private EmailPriority(int rank, String name) {
        this.rank = rank;
        this.name = name;
    }

    /**
     * Returns the rank of this priority. The rank determines the actual value of the priority. Eventually this value
     * will be put in the email "X-Priority" header entry.
     *
     * @return The value of this priority.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Returns the name of this priority. This name identifies this priority and can be used in combination with
     * <code>{@link #name(String)}</code> to resolve priorities based on names.
     *
     * @return The name of this priority.
     */
    public String getName() {
        return name;
    }

    /**
     * Resolves the priority based on its name.
     *
     * @param name The name of the priority.
     * @return The resolved priority
     * @throws IllegalArgumentException if priority could not be resolved.
     */
    public static EmailPriority name(String name) {
        for (int i=0; i<priorities.length; i++) {
            if (priorities[i].getName().equals(name)) {
                return priorities[i];
            }
        }
        throw new IllegalArgumentException("Priority named '" + name + "' does not exist");
    }
}
