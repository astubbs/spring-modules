/* 
 * Created on Oct 25, 2005
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
package org.springmodules.cache.provider;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.util.ObjectUtils;

/**
 * <p>
 * Unit Tests for <code>{@link ReflectionCacheModelEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class ReflectionCacheModelEditorTests extends TestCase {

  public static class LightSaber {

    private String color;

    private boolean on;

    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof LightSaber)) {
        return false;
      }

      LightSaber saber = (LightSaber) obj;

      if (!ObjectUtils.nullSafeEquals(color, saber.color)) {
        return false;
      }
      if (on != saber.on) {
        return false;
      }

      return true;
    }

    public String getColor() {
      return color;
    }

    public boolean isOn() {
      return on;
    }

    public void setColor(String newColor) {
      color = newColor;
    }

    public void setOn(boolean newOn) {
      on = newOn;
    }
  }

  protected class OnPropertyEditor extends PropertyEditorSupport {
    public void setAsText(String newText) throws IllegalArgumentException {
      if ("yes".equals(newText)) {
        setValue(Boolean.TRUE);
      } else {
        setValue(Boolean.FALSE);
      }
    }

  }

  private ReflectionCacheModelEditor editor;

  private LightSaber lightSaber;

  public ReflectionCacheModelEditorTests(String name) {
    super(name);
  }

  public void testSetAsTextString() {
    String color = "red";
    boolean on = true;

    lightSaber.setColor(color);
    lightSaber.setOn(on);

    editor.setAsText("color=" + color + ";on=" + (on ? "true" : "false"));
    assertEquals(lightSaber, editor.getValue());
  }

  public void testSetAsTextStringWhenTextIsEmpty() {
    editor.setAsText("");
    assertEquals(lightSaber, editor.getValue());
  }

  public void testSetAsTextWithCacheModelClassNotSet() {
    editor.setCacheModelClass(null);
    
    try {
      editor.setAsText("");
      fail();
    } catch (IllegalStateException exception) {
      // expecting this exception
    }
  }
  
  public void testSetAsTextWithPropertyEditor() {
    OnPropertyEditor onPropertyEditor = new OnPropertyEditor();
    Map propertyEditors = new HashMap();
    propertyEditors.put("on", onPropertyEditor);
    editor.setCacheModelPropertyEditors(propertyEditors);
    editor.setAsText("on=yes");

    lightSaber.setOn(true);
    assertEquals(lightSaber, editor.getValue());
  }
  
  protected void setUp() {
    lightSaber = new LightSaber();
    editor = new ReflectionCacheModelEditor();
    editor.setCacheModelClass(lightSaber.getClass());
  }
}
