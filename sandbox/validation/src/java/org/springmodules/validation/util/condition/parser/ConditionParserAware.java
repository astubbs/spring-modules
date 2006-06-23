/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.util.condition.parser;

/**
 * Objects that use a {@link ConditionParser} and are aware of it should implement this interface.
 *
 * @author Uri Boness
 */
public interface ConditionParserAware {

    /**
     * Sets the {@link ConditionParser} to be used.
     *
     * @param conditionParser The condition parser to be used.
     */
    void setConditionParser(ConditionParser conditionParser);

    /**
     * Returns the used {@link ConditionParser}.
     *
     * @return The used condition parser.
     */
    ConditionParser getConditionParser();

}
