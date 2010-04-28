package org.springmodules.validation.bean.context.web;
/**
 * 
 * @author Uri Boness
 */

import junit.framework.*;
import org.springmodules.validation.bean.context.web.ValidationContextUrlMappingArrayPropertyEditor;

public class ValidationContextUrlMappingArrayPropertyEditorTests extends TestCase {

    private ValidationContextUrlMappingArrayPropertyEditor editor;

    protected void setUp() throws Exception {
        editor = new ValidationContextUrlMappingArrayPropertyEditor();
    }

    public void testSetAsText() throws Exception {
        editor.setAsText(
                "/user/*=ctx1,ctx2\n" +
                "/admin/*=ctx3"
        );

        ValidationContextUrlMapping[] mappings = (ValidationContextUrlMapping[])editor.getValue();

        assertNotNull(mappings);
        assertEquals(2, mappings.length);

        assertEquals("/user/*", mappings[0].getUrlPattern());
        assertEquals(2, mappings[0].getContextTokens().length);
        assertEquals("ctx1", mappings[0].getContextTokens()[0]);
        assertEquals("ctx2", mappings[0].getContextTokens()[1]);

        assertEquals("/admin/*", mappings[1].getUrlPattern());
        assertEquals(1, mappings[1].getContextTokens().length);
        assertEquals("ctx3", mappings[1].getContextTokens()[0]);
    }

    public void testSetAsText_WithEmptyContexts() throws Exception {
        editor.setAsText(
                "/user/*=\n" +
                "/admin/*=ctx3"
        );

        ValidationContextUrlMapping[] mappings = (ValidationContextUrlMapping[])editor.getValue();

        assertNotNull(mappings);
        assertEquals(2, mappings.length);

        assertEquals("/user/*", mappings[0].getUrlPattern());
        assertEquals(0, mappings[0].getContextTokens().length);

        assertEquals("/admin/*", mappings[1].getUrlPattern());
        assertEquals(1, mappings[1].getContextTokens().length);
        assertEquals("ctx3", mappings[1].getContextTokens()[0]);
    }
}