/*
 * Copyright (c) 2007, Your Corporation. All Rights Reserved.
 */

package org.springmodules.email.dispatcher;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springmodules.email.Email;
import org.springmodules.email.dispatcher.callback.DispatchingCallback;
import org.springmodules.email.dispatcher.emailsender.EmailSender;
import org.springmodules.email.dispatcher.errorhandler.DispatchingErrorHandler;

/**
 * @author Uri Boness
 */
public class AsyncEmailDispatcherTests extends TestCase {

    private AsyncEmailDispatcher dispatcher;

    private MailSender mailSender;
    private MockControl mailSenderControl;


    private EmailSender emailSender;
    private MockControl emailSenderControl;

    private DispatchingErrorHandler errorHandler;
    private MockControl errorHandlerControl;

    private DispatchingCallback callback;
    private MockControl callbackControl;

    protected void setUp() throws Exception {

        mailSenderControl = MockControl.createControl(MailSender.class);
        mailSender = (MailSender)mailSenderControl.getMock();

        emailSenderControl = MockControl.createControl(EmailSender.class);
        emailSender = (EmailSender)emailSenderControl.getMock();

        errorHandlerControl = MockControl.createControl(DispatchingErrorHandler.class);
        errorHandler = (DispatchingErrorHandler)errorHandlerControl.getMock();

        callbackControl = MockControl.createControl(DispatchingCallback.class);
        callback = (DispatchingCallback)callbackControl.getMock();

        dispatcher = new AsyncEmailDispatcher();
        dispatcher.setMailSender(mailSender);
        dispatcher.setEmailSender(emailSender);
        dispatcher.setErrorHandler(errorHandler);
        dispatcher.setDispachingCallback(callback);
        dispatcher.setEncoding("UTF-8");
    }

    public void testSend() throws Exception {
        Email email = new Email();

        TestExecutor executor = new TestExecutor(email);
        dispatcher.setTaskExecutor(executor);

        callback.emailDispatched(email, true);
        callbackControl.replay();

        emailSender.send(mailSender, email, "UTF-8");
        emailSenderControl.replay();

        dispatcher.send(email);

        assertTrue(executor.wasCalled());
        callbackControl.verify();
        emailSenderControl.verify();
    }

    public void testSend_WithError() throws Exception {
        Email email = new Email();

        TestExecutor executor = new TestExecutor(email);
        dispatcher.setTaskExecutor(executor);

        MailException error = new MailException("Error") {};

        errorHandler.handle(error, email);
        errorHandlerControl.replay();

        callback.emailDispatched(email, false);
        callbackControl.replay();

        emailSender.send(mailSender, email, "UTF-8");
        emailSenderControl.setThrowable(error);
        emailSenderControl.replay();

        dispatcher.send(email);

        callbackControl.verify();
        emailSenderControl.verify();
        errorHandlerControl.verify();
    }

    //============================================== Inner Classes =====================================================

    private class TestExecutor implements TaskExecutor {

        private Email expectedEmail;

        private boolean called;

        public TestExecutor(Email expectedEmail) {
            this.expectedEmail = expectedEmail;
        }

        public void execute(Runnable task) {
            assertTrue(task instanceof AsyncEmailDispatcher.EmailDispatchingTask);
            assertSame(expectedEmail, ((AsyncEmailDispatcher.EmailDispatchingTask)task).getEmail());
            called = true;
            task.run();
        }

        public boolean wasCalled() {
            return called;
        }
    }

}