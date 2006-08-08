/*
 * Copyright 2006 the original author or authors.
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

package org.springmodules.web.propertyeditors;

/**
 * Exception thrown by a {@link ReflectivePropertyEditor}.
 *
 * @author Sergio Bossa
 */
public class ReflectivePropertyEditorException extends RuntimeException {
    
    /**
     * Creates a new instance of <code>ReflectivePropertyEditorException</code> without detail message.
     */
    public ReflectivePropertyEditorException() {
    }
    
    /**
     * Constructs an instance of <code>ReflectivePropertyEditorException</code> with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public ReflectivePropertyEditorException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>ReflectivePropertyEditorException</code> with the specified nested exception.
     * 
     * @param ex the nested exception.
     */
    public ReflectivePropertyEditorException(Throwable ex) {
        super(ex);
    }
    
    /**
     * Constructs an instance of <code>ReflectivePropertyEditorException</code> with the specified detail message and nested exception.
     * 
     * @param msg the detail message.
     * @param ex the nested exception.
     */
    public ReflectivePropertyEditorException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
