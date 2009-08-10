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
package org.springmodules.email.dispatcher;

import java.util.HashMap;
import java.util.Map;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springmodules.email.Person;
import org.springmodules.email.parser.xml.SaxEmailParser;
import org.springmodules.template.engine.velocity.VelocityTemplateEngine;
import org.springmodules.template.resolver.BasicTemplateResolver;

/**
 * @author Uri Boness
 */
public class JavaMailEmailDispatcherTests extends TestCase {

    private JavaMailEmailDispatcher dispatcher;

    private SimpleSmtpServer server;

    protected void setUp() throws Exception {
        ResourceLoader loader = new ClassPathResourceLoader();
        VelocityTemplateEngine engine = new VelocityTemplateEngine(loader);
        engine.afterPropertiesSet();
        BasicTemplateResolver resolver = new BasicTemplateResolver(engine, loader);

        dispatcher = new JavaMailEmailDispatcher();
        dispatcher.setResourceLoader(loader);
        dispatcher.setTemplateResolver(resolver);
        dispatcher.setHost("localhost");
        dispatcher.setPort(2525);
        dispatcher.afterPropertiesSet();

        server = SimpleSmtpServer.start(2525);
    }

    protected void tearDown() throws Exception {
        server.stop();
    }

    public void testSend() throws Exception {

        Map model = new HashMap();
        model.put("sender", new Person("Daan", "daan@bla.com"));
        model.put("recipient", new Person("Lian", "lian@bla.com"));
        dispatcher.send("email.eml", model);

        assertEquals(1, server.getReceivedEmailSize());
        SmtpMessage message = (SmtpMessage)server.getReceivedEmail().next();
        assertEquals("Daan <daan@bla.com>", message.getHeaderValue("From"));
        assertEquals("Lian <lian@bla.com>", message.getHeaderValue("To"));
        assertEquals("subject", message.getHeaderValue("Subject"));
        assertEquals("No Reply <noreply@bla.org>", message.getHeaderValue("Reply-To"));
    }


    //============================================== Inner Classes =====================================================

    protected class ClassPathResourceLoader implements ResourceLoader {

        public Resource getResource(String location) {
            return new ClassPathResource(location, JavaMailEmailDispatcherTests.class);
        }

        public ClassLoader getClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }
    }
}