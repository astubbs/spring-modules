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

package org.springmodules.email;

import org.springframework.core.io.Resource;

/**
 * A parser that knows how to parse a resource representing and email and build an {@link Email} instance while
 * doing so.
 *
 * @author Uri Boness
 */
public interface EmailParser {

    /**
     * Parses the given resource and returns the {@link Email} that it represents.
     *
     * @param resource The given resource that reprsents an email.
     * @return The email represented by the given resource.
     */
    Email parse(Resource resource);

}
