package org.springmodules.validation.util.condition.range;

import java.util.Comparator;

import org.springframework.util.Assert;

/**
 * @author Uri Boness
 */
public class PersonByAgeComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        Assert.isInstanceOf(Person.class, o1, getClass().getName() + " can only compare Person objects");
        Assert.isInstanceOf(Person.class, o2, getClass().getName() + " can only compare Person objects");
        return compare((Person)o1, (Person)o2);
    }

    public int compare(Person p1, Person p2) {
        return p1.getAge() - p2.getAge();
    }

}
