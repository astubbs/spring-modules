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

package org.springmodules.xt.model.generator.support;

/**
 * Exception thrown when constructor arguments have an illegal position value.
 *
 * @author Sergio Bossa
 */
public class IllegalArgumentPositionException extends RuntimeException {
    
    /**
     * Creates a new instance of <code>IllegalArgumentPositionException</code> without detail message.
     */
    public IllegalArgumentPositionException() {
    }
    
    /**
     * Constructs an instance of <code>IllegalArgumentPositionException</code> with the specified detail message.
     * 
     * 
     * @param msg the detail message.
     */
    public IllegalArgumentPositionException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>IllegalArgumentPositionException</code> with the specified nested exception.
     * 
     * 
     * @param ex the nested exception.
     */
    public IllegalArgumentPositionException(Throwable ex) {
        super(ex);
    }
    
    /**
     * Constructs an instance of <code>IllegalArgumentPositionException</code> with the specified detail message and nested exception.
     * 
     * 
     * @param msg the detail message.
     * @param ex the nested exception.
     */
    public IllegalArgumentPositionException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
