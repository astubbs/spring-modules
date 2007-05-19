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

package org.springmodules.xt.model.notification;

import junit.framework.*;

/**
 *
 * @author Sergio Bossa
 */
public class NotificationImplTest extends TestCase {
    
    private Notification notification;
    
    public NotificationImplTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.notification = new NotificationImpl();
        this.notification.addMessage(new MessageImpl("error",  Message.Type.ERROR, "ERROR"));
        this.notification.addMessage(new MessageImpl("warning", Message.Type.WARNING, "WARNING"));
        this.notification.addMessage(new MessageImpl("info", Message.Type.INFO, "INFO"));
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of addMessage method, of class org.springmodules.xt.model.notification.NotificationImpl.
     */
    public void testAddMessage() {
        // Tested by setUp()
    }

    /**
     * Test of removeMessage method, of class org.springmodules.xt.model.notification.NotificationImpl.
     */
    public void testRemoveMessage() {
        Message msg = new MessageImpl("error", Message.Type.ERROR, "ERROR");
        assertTrue(this.notification.hasMessages(Message.Type.ERROR));
        this.notification.removeMessage(msg);
        assertFalse(this.notification.hasMessages(Message.Type.ERROR));
    }

    /**
     * Test of getMessages method, of class org.springmodules.xt.model.notification.NotificationImpl.
     */
    public void testGetMessages() {
        assertTrue(this.notification.getMessages(Message.Type.ERROR).length == 1);
        assertTrue(this.notification.getMessages(Message.Type.WARNING).length == 1);
        assertTrue(this.notification.getMessages(Message.Type.INFO).length == 1);
    }

    /**
     * Test of hasMessages method, of class org.springmodules.xt.model.notification.NotificationImpl.
     */
    public void testHasMessages() {
        assertTrue(this.notification.hasMessages(Message.Type.ERROR));
        assertTrue(this.notification.hasMessages(Message.Type.WARNING));
        assertTrue(this.notification.hasMessages(Message.Type.INFO));
        assertTrue(this.notification.hasMessages());
    }
    
    /**
     *
     */
    public void testHasAtLeastOneMessage() {
        this.notification = new NotificationImpl();
        this.notification.addMessage(new MessageImpl("error",  Message.Type.ERROR, "ERROR"));
        assertTrue(this.notification.hasMessages());
    }

    /**
     * Test of getAllMessages method, of class org.springmodules.xt.model.notification.NotificationImpl.
     */
    public void testGetAllMessages() {
        assertTrue(this.notification.getAllMessages().length == 3);
    }

    /**
     * Test of addAllMessages method, of class org.springmodules.xt.model.notification.NotificationImpl.
     */
    public void testAddAllMessages() {
        Notification notification2 = new NotificationImpl();
        notification2.addMessage(new MessageImpl("error2", Message.Type.ERROR, "ERROR2"));
        notification2.addMessage(new MessageImpl("warning2", Message.Type.WARNING, "WARNING2"));
        notification2.addMessage(new MessageImpl("info2", Message.Type.INFO, "INFO2"));
        
        this.notification.addAllMessages(notification2);
        
        assertTrue(this.notification.getAllMessages().length == 6);
    }
}
