/*
 * Created on Nov 29, 2005
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.cache.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.springmodules.util.Objects;

/**
 * <p>
 * Unit Tests for <code>{@link Reflections}</code>.
 * </p>
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public class ReflectionsTest extends TestCase {

  protected static class TestBean {

    private static String field4;

    public static final String getField4() {
      return field4;
    }

    public static final void setField4(String newField4) {
      field4 = newField4;
    }

    private String field1;

    private String field2;

    private String field3;

    private transient String field5;

    public final String getField1() {
      return field1;
    }

    public final String getField2() {
      return field2;
    }

    public final String getField3() {
      return field3;
    }

    public final String getField5() {
      return field5;
    }

    public final void setField1(String newField1) {
      field1 = newField1;
    }

    public final void setField2(String newField2) {
      field2 = newField2;
    }

    public final void setField3(String newField3) {
      field3 = newField3;
    }

    public final void setField5(String newField5) {
      field5 = newField5;
    }

  }

  public ReflectionsTest(String name) {
    super(name);
  }

  public void testReflectionHashCodeWithCollection() {
    int expected = 31 * 7 + "Han Solo".hashCode();
    expected = 31 * expected + "Leia Organa".hashCode();

    List list = new ArrayList();
    list.add("Han Solo");
    list.add("Leia Organa");
    int actual = Reflections.reflectionHashCode(list);

    assertEquals(expected, actual);
  }

  public void testReflectionHashCodeWithJavaBean() {
    int expected = 31 * 7 + "field1".hashCode();
    expected = 31 * expected + "field2".hashCode();
    expected = 31 * expected + "field3".hashCode();

    TestBean bean = new TestBean();
    bean.setField1("field1");
    bean.setField2("field2");
    bean.setField3("field3");
    TestBean.setField4("field4");
    bean.setField5("field5");

    int actual = Reflections.reflectionHashCode(bean);
    assertEquals(expected, actual);
  }

  public void testReflectionHashCodeWithMap() {
    int h1 = 31 * 7 + "Jedi".hashCode();
    h1 = 31 * h1 + "Luke Skywalker".hashCode();

    int h2 = 31 * 7 + "Sith".hashCode();
    h2 = 31 * h2 + "Darth Vader".hashCode();

    int expected = 31 * 7 + h1;
    expected = 31 * expected + h2;

    Map map = new HashMap();
    map.put("Jedi", "Luke Skywalker");
    map.put("Sith", "Darth Vader");
    int actual = Reflections.reflectionHashCode(map);

    assertEquals(expected, actual);
  }

  public void testReflectionHashCodeWithObjectArray() {
    int expected = 31 * 7 + "Han Solo".hashCode();
    expected = 31 * expected + "Leia Organa".hashCode();

    Object[] array = { "Han Solo", "Leia Organa" };
    int actual = Reflections.reflectionHashCode(array);

    assertEquals(expected, actual);
  }

  public void testReflectionHashCodeWithObjectEqualToNull() {
    assertEquals(0, Reflections.reflectionHashCode(null));
  }

  public void testReflectionHashCodeWithPrimitiveArray() {
    boolean[] array = new boolean[] { true, false };
    int expected = Objects.nullSafeHashCode(array);
    int actual = Reflections.reflectionHashCode(array);

    assertEquals(expected, actual);
  }

  public void testReflectionHashCodeWithPrimitiveWrapper() {
    int expected = Boolean.TRUE.hashCode();
    int actual = Reflections.reflectionHashCode(Boolean.TRUE);

    assertEquals(expected, actual);
  }

  public void testReflectionHashCodeWithString() {
    int expected = "X-Wing".hashCode();
    int actual = Reflections.reflectionHashCode("X-Wing");

    assertEquals(expected, actual);
  }

	public void testReflectionHashCodeWithDate() {
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		int expected = Reflections.reflectionHashCode(now);
		int actual = now.hashCode();

		// test same value
		assertEquals(expected, actual);

		// test different values
		cal.add(Calendar.DATE, 1);
		Date tomorrow = cal.getTime();
		actual = tomorrow.hashCode();

		assertTrue("hasCode <" + expected + "> should be different than <" + actual
				+ ">", expected != actual);
	}

	public void testReflectionHashCodeWithTimestamp() {
		Calendar cal = Calendar.getInstance();
		Timestamp now = new Timestamp(cal.getTimeInMillis());
		int expected = Reflections.reflectionHashCode(now);
		int actual = now.hashCode();

		// test same value
		assertEquals(expected, actual);

		// test different values
		cal.add(Calendar.DATE, 1);
		Timestamp tomorrow = new Timestamp(cal.getTimeInMillis());
		actual = tomorrow.hashCode();

		assertTrue("hasCode <" + expected + "> should be different than <" + actual
				+ ">", expected != actual);
	}

	public void testReflectionHashCodeWithClass() {
		Class clazz = String.class;
		int expected = Reflections.reflectionHashCode(clazz);
		int actual = clazz.hashCode();

		// test same value
		assertEquals(expected, actual);

		// test different values
		actual = Integer.class.hashCode();

		assertTrue("hasCode <" + expected + "> should be different than <" + actual
				+ ">", expected != actual);
	}

}
