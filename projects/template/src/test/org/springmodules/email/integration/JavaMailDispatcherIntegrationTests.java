package org.springmodules.email.integration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.email.EmailDispatcher;
import org.springmodules.email.EmailUtils;

/**
 * @author Uri Boness
 */
public class JavaMailDispatcherIntegrationTests extends TestCase {

    private ApplicationContext appContext;
    private EmailDispatcher dispatcher;
    private Properties properties;

    public void test() throws Exception {
//        doTest();
    }

    protected void doTest() throws Exception {

        appContext = new ClassPathXmlApplicationContext("appContext.xml", getClass());
        dispatcher = (EmailDispatcher)appContext.getBean("emailDispatcher");
        properties = (Properties)appContext.getBean("emailDetails");

        Map model = new HashMap();
        model.put("from", EmailUtils.createAddress(get("from.name"), get("from.email")));
        InternetAddress[] addresses = new InternetAddress[] {
            EmailUtils.createAddress(get("to.name1"), get("to.email1")),
            EmailUtils.createAddress(get("to.name2"), get("to.email2"))
        };
        model.put("tos", addresses);

        addresses = new InternetAddress[] {
            EmailUtils.createAddress(get("cc.name"), get("cc.email"))
        };
        model.put("ccs", addresses);

        addresses = new InternetAddress[] {
            EmailUtils.createAddress(get("bcc.name"), get("bcc.email"))
        };
        model.put("bccs", addresses);
        model.put("subject", get("subject"));

        dispatcher.send("test-email", model);
    }

    protected String get(String key) {
        return properties.getProperty(key);
    }

}
