/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.template.providers.webmacro;

import java.io.*;

import junit.framework.*;
import org.springmodules.template.providers.webmacro.TemplateSourceResolverTemplateProvider;
import org.springmodules.template.*;
import org.easymock.*;
import org.webmacro.*;
import org.webmacro.Template;
import org.webmacro.util.*;

/**
 * Tests the {@link TemplateSourceResolverTemplateProvider} class.
 *
 * @author Uri Boness
 */
public class TemplateSourceResolverTemplateProviderTest extends TestCase {

    private TemplateSourceResolverTemplateProvider provider;

    private TemplateSourceResolver resolver;
    private MockControl resolverControl;

    private TemplateSource source;
    private MockControl sourceControl;

    private Template template;
    private MockControl templateControl;

    protected void setUp() throws Exception {
        setupMocks();
        provider = new TemplateSourceResolverTemplateProvider();
        initProvider(provider);
    }

    protected void initProvider(TemplateSourceResolverTemplateProvider provider) throws Exception {
        provider.setTemplateSourceResolver(resolver);
        provider.init(Broker.getBroker(), new Settings());
    }

    protected void setupMocks() {
        resolverControl = MockControl.createControl(TemplateSourceResolver.class);
        resolver = (TemplateSourceResolver)resolverControl.getMock();

        sourceControl = MockControl.createControl(TemplateSource.class);
        source = (TemplateSource)sourceControl.getMock();

        templateControl = MockControl.createControl(Template.class);
        template = (Template)templateControl.getMock();
    }

    protected void verifyMocks() {
        resolverControl.verify();
        sourceControl.verify();
        templateControl.verify();
    }

    protected void replayMocks() {
        resolverControl.replay();
        sourceControl.replay();
        templateControl.replay();
    }

    public void testInternalLoad() throws Exception {

        final InputStream is = new ByteArrayInputStream(new byte[0]);
        sourceControl.expectAndReturn(source.getInputStream(), is);
        resolverControl.expectAndReturn(resolver.resolveTemplateSource("name"), source);

        provider = new TemplateSourceResolverTemplateProvider() {
            protected Template createTemplate(InputStream in, String name) {
                assertSame(is, in);
                assertEquals("name", name);
                return template;
            }
        };
        initProvider(provider);

        template.parse();

        replayMocks();

        Template loadedTemplate = provider.internalLoad("name");

        assertSame(loadedTemplate, template);

        verifyMocks();
    }

    public void testGetWithCacheMiss() throws Exception {

        provider = new TemplateSourceResolverTemplateProvider() {
            protected Template getCachedTemplate(String name) {
                assertEquals("name", name);
                return null;
            }

            protected void cacheTemplate(String name, Template t) {
                assertEquals("name", name);
                assertSame(template, t);
            }

            protected Template internalLoad(String name) throws ResourceException {
                assertEquals("name", name);
                return template;
            }
        };
        initProvider(provider);

        replayMocks();

        Object o = provider.get("name");

        assertTrue(o instanceof Template);
        assertSame(template, o);

        verifyMocks();
    }

    public void testGetWithCacheHit() throws Exception {

        provider = new TemplateSourceResolverTemplateProvider() {
            protected Template getCachedTemplate(String name) {
                assertEquals("name", name);
                return template;
            }

            protected void cacheTemplate(String name, Template t) {
                fail("no nead to re-cache cached templates");
            }

            protected Template internalLoad(String name) throws ResourceException {
                fail("no need to re-load cached templates");
                return null;
            }
        };
        initProvider(provider);

        replayMocks();

        Object o = provider.get("name");

        assertTrue(o instanceof Template);
        assertSame(template, o);

        verifyMocks();
    }

    public void testTemplateCaching() throws Exception {

        assertNull("initialy the template is not cahced", provider.getCachedTemplate("name"));

        provider.cacheTemplate("name", template);

        Template cachedTemplate = provider.getCachedTemplate("name");

        assertNotNull(cachedTemplate);
        assertSame(template, cachedTemplate);        
    }
}