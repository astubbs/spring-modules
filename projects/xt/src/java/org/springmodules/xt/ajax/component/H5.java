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

package org.springmodules.xt.ajax.component;

/**
 * Component representing an h5 heading.
 * @author Peter Bona
 * @author Sergio Bossa
 */
public class H5 extends AbstractHeading {

    /**
     * Construct an empty heading.
     */
    public H5() {
    }
    
    /**
     * Construct an heading with a given content.
     */
    public H5(String content) {
        super(content);
    }
    
	protected int getLevel() {
		return 5;
	}
}
