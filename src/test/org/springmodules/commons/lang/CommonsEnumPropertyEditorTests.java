/*
 * Copyright (c) 2004 Hawaii Information Consortium
 * 220 S. King St #2190, Honolulu, HI 96813
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Hawaii Information Consortium. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with HIC.
 */
package org.springmodules.commons.lang;

import org.apache.commons.lang.enums.Enum;

import junit.framework.TestCase;

/**
 * 
 */
public class CommonsEnumPropertyEditorTests extends TestCase {
    
    private CommonsEnumPropertyEditor editor;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * Class under test for String getAsText()
     */
    public void testGetAsText() {
        editor = new CommonsEnumPropertyEditor(Country.class, false);
        editor.setValue(Country.US);
        assertEquals("US", editor.getAsText());
        
        editor = new CommonsEnumPropertyEditor(SecretQuestion.class, false);
        editor.setValue(SecretQuestion.LAST_FOUR_SSN);
        assertEquals(SecretQuestion.LAST_FOUR_SSN.getName(), editor.getAsText());
    }

    /*
     * Class under test for void setAsText(String)
     */
    public void testSetAsTextString() {
        editor = new CommonsEnumPropertyEditor(Country.class, false);
        editor.setAsText("US");
        assertEquals(Country.US, editor.getValue());
        
        editor = new CommonsEnumPropertyEditor(SecretQuestion.class, false);
        editor.setAsText(SecretQuestion.LAST_FOUR_SSN.getName());
        assertEquals(SecretQuestion.LAST_FOUR_SSN, editor.getValue());
    }
    
    public void testSetAsTestWithNullString() {
        editor = new CommonsEnumPropertyEditor(Country.class, true);
        editor.setAsText(null);
        assertNull(editor.getValue());
    }
    
    public void testSetAsTestWithEmptyString() {
        editor = new CommonsEnumPropertyEditor(Country.class, true);
        editor.setAsText("");
        assertNull(editor.getValue());
    }
    
    static class Country extends Enum {
    	public static final Country US = new Country("US");
    	public static final Country UK = new Country("United Kingdom");
    	
    	private Country(String name) {
    		super(name);
    	}
    }
    
    static class SecretQuestion extends Enum {
    	public static final SecretQuestion FAVORITE_PET = new SecretQuestion("favoritePet");
    	public static final SecretQuestion LAST_FOUR_SSN = new SecretQuestion("lastFourSsn");
    	
    	private SecretQuestion(String name) {
    		super(name);
    	}
    }

}
