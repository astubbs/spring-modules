/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.util.Collection;

/**
 * Serves as a source for annotation handlers (both property and class handlers)
 * 
 * @author Uri Boness
 */
public interface ValidationAnnotationHandlersBundle {

    /**
     * Returns all associated property validation annotation handlers.
     *
     * @return All associated property validation annotation handlers.
     */
    Collection<PropertyValidationAnnotationHandler> getPropertyHandlers();

    /**
     * Returns all associated class validation annotation handlers.
     *
     * @return All associated class validation annotation handlers.
     */
    Collection<ClassValidationAnnotationHandler> getClassHandlers();
    
}
