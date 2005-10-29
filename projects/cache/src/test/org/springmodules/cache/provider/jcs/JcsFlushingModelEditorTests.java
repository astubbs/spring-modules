/* 
 * Created on Oct 28, 2005
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
package org.springmodules.cache.provider.jcs;

import junit.framework.TestCase;

import org.springmodules.cache.provider.jcs.JcsFlushingModel.CacheStruct;

/**
 * <p>
 * Unit Tests for <code>{@link JcsFlushingModelEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JcsFlushingModelEditorTests extends TestCase {

  private JcsFlushingModelEditor editor;

  public JcsFlushingModelEditorTests(String name) {
    super(name);
  }

  protected void setUp() {
    editor = new JcsFlushingModelEditor();
  }

  public void testSetAsTextWithText() {
    editor.setAsText("cacheName=starwars;groups=rebels,empire");
    JcsFlushingModel expected = new JcsFlushingModel(new CacheStruct(
        "starwars", "rebels,empire"));
    assertEquals(expected, editor.getValue());
  }

  public void testSetAsTextWithEmptyText() {
    editor.setAsText("");
    assertEquals(new JcsFlushingModel(), editor.getValue());
  }

  public void testSetAsTextWithTextEndingWithDelimeter() {
    editor.setAsText("cacheName=main;groups=group1,group2|");
    JcsFlushingModel expected = new JcsFlushingModel(new CacheStruct("main",
        "group1,group2"));
    assertEquals(expected, editor.getValue());
  }

  public void testSetAsTextWithTextEqualToNull() {
    editor.setAsText(null);
    assertEquals(new JcsFlushingModel(), editor.getValue());
  }

  public void testSetAsTextWithTextHavingOnlyDelimiter() {
    editor.setAsText("|");
    assertEquals(new JcsFlushingModel(), editor.getValue());
  }

}
