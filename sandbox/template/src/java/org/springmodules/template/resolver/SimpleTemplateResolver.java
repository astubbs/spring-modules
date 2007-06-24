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

package org.springmodules.template.resolver;

import org.springframework.core.io.Resource;

/**
 * @author Uri Boness
 */
public class SimpleTemplateResolver extends CachingTemplateResolver {

    private String prefix;
    private String suffix;

    public SimpleTemplateResolver() {
        this("", "");
    }

    public SimpleTemplateResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    protected Resource loadTemplateResource(String name, String encoding) {
        return super.loadTemplateResource(prefix + name + suffix, encoding);
    }


    //============================================== Setter/Getter =====================================================

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
