/*
 * Copyright 2006-2007 the original author or authors.
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

package org.springmodules.xt.ajax.validation.support.internal;

import java.io.Serializable;
import java.util.List;
import org.springframework.validation.ObjectError;

/**
 * Serializable container of {@link org.springframework.validation.ObjectError}s
 * to store in HTTP session.
 *
 * @author Sergio Bossa
 */
public class ErrorsContainer implements Serializable {
    
    private static final long serialVersionUID = 26L;
    
    private ObjectError[] errors;
    
    public ErrorsContainer(ObjectError[] errors) {
        this.errors = errors;
    }

    public ObjectError[] getErrors() {
        return this.errors;
    }
}
