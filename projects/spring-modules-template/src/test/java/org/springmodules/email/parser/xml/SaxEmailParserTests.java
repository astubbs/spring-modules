package org.springmodules.email.parser.xml;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.email.Attachment;
import org.springmodules.email.Email;
import org.springmodules.email.parser.xml.SaxEmailParser;

/**
 * @author Uri Boness
 */
public class SaxEmailParserTests extends TestCase {

    private SaxEmailParser parser;
    private Resource resource;

    protected void setUp() throws Exception {
        parser = new SaxEmailParser();
        resource = new ClassPathResource("/org/springmodules/email/parser/test.xml");
    }

    public void testParse() throws Exception {

        Email email = parser.parse(resource);
        assertNotNull(email);

        Map headers = email.getHeaders();
        assertNotNull(headers);
        assertEquals(1, headers.size());
        assertTrue(headers.containsKey("test"));
        assertEquals("10", headers.get("test"));

        InternetAddress address = email.getFrom();
        assertNotNull(address);
        assertEquals("From", address.getPersonal());
        assertEquals("from@springmodules.org", address.getAddress());

        InternetAddress[] addresses = email.getTo();
        assertNotNull(addresses);
        assertEquals(2, addresses.length);
        assertEquals("To", addresses[0].getPersonal());
        assertEquals("to1@springmodules.org", addresses[0].getAddress());
        assertNull(addresses[1].getPersonal());
        assertEquals("to2@springmodules.org", addresses[1].getAddress());

        addresses = email.getCc();
        assertNotNull(addresses);
        assertEquals(2, addresses.length);
        assertEquals("Cc", addresses[0].getPersonal());
        assertEquals("cc1@springmodules.org", addresses[0].getAddress());
        assertNull(addresses[1].getPersonal());
        assertEquals("cc2@springmodules.org", addresses[1].getAddress());

        addresses = email.getBcc();
        assertNotNull(addresses);
        assertEquals(2, addresses.length);
        assertEquals("Bcc", addresses[0].getPersonal());
        assertEquals("bcc1@springmodules.org", addresses[0].getAddress());
        assertNull(addresses[1].getPersonal());
        assertEquals("bcc2@springmodules.org", addresses[1].getAddress());

        assertEquals("HIGH", email.getPriority().getName());

        address = email.getReplyTo();
        assertNotNull(address);
        assertEquals("Reply To", address.getPersonal());
        assertEquals("replyto@springmodules.org", address.getAddress());

        assertEquals("subject", email.getSubject());

        assertEquals("Some text body", email.getTextBody());

        assertEquals("<html><body>Some html body</body></html>", email.getHtmlBody());

        Set attachments = email.getAttachments();
        assertFalse(attachments.isEmpty());
        Iterator iter = attachments.iterator();
        Attachment attachment = (Attachment)iter.next();
        assertTrue("attachment1".equals(attachment.getName()) || "attachment2".equals(attachment.getName()));
        assertEquals("test.xml", attachment.getResource().getFilename());
        attachment = (Attachment)iter.next();
        assertTrue("attachment1".equals(attachment.getName()) || "attachment2".equals(attachment.getName()));
        assertEquals("test.xml", attachment.getResource().getFilename());

        attachments = email.getInlineAttachments();
        assertFalse(attachments.isEmpty());
        iter = attachments.iterator();
        attachment = (Attachment)iter.next();
        assertTrue("attachment1".equals(attachment.getName()) || "attachment2".equals(attachment.getName()));
        assertEquals("test.xml", attachment.getResource().getFilename());
        attachment = (Attachment)iter.next();
        assertTrue("attachment1".equals(attachment.getName()) || "attachment2".equals(attachment.getName()));
        assertEquals("test.xml", attachment.getResource().getFilename());
    }
}