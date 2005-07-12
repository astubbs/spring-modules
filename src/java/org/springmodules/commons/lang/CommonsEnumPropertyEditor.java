/* 
 * Created on July 12, 2005
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
package org.springmodules.commons.lang;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang.enums.EnumUtils;
import org.springframework.util.StringUtils;

/**
 * Converts to and from a commons-lang Enum.
 */
public class CommonsEnumPropertyEditor extends PropertyEditorSupport {
    
    private Class enumClass;
    private boolean allowEmpty;
    
    /**
     * @param enumClass class of the enum
     * @param allowEmpty should a null value be OK?
     */
    public CommonsEnumPropertyEditor(Class enumClass, boolean allowEmpty) {
        if (! Enum.class.isAssignableFrom(enumClass)) {
            throw new IllegalArgumentException("Class must be subclass of commons.Enum");
        }
        this.enumClass = enumClass;
        this.allowEmpty = allowEmpty;
    }

    /**
     * Returns the enum's name, or blank string if no enum has been set.
     * @return enum's name, or blank string if nothing has been set
     * @see java.beans.PropertyEditor#getAsText()
     */
    public String getAsText() {
        if (getValue() != null) {
            return ((Enum)getValue()).getName();
        } else {
            return "";
        }
    }
    
    /**
     * Turns a string to an enum.  The string must be a enum name.
     * @param text the enum name
     * @see java.beans.PropertyEditor#setAsText(java.lang.String)
     */
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            setValue(null);
        } else {
            Enum eNum = EnumUtils.getEnum(enumClass, text);
            if (eNum != null) {
                setValue(eNum);
            } else {
                throw new IllegalArgumentException("Invalid enum name: " + text);
            }
        }
    }
    
}
