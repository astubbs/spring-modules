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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.util.Assert;
import org.springmodules.email.Email;
import org.springmodules.email.dispatcher.errorhandler.DispatchingErrorHandler;
import org.springmodules.email.dispatcher.errorhandler.LoggingDispatchingErrorHandler;
import org.springmodules.email.dispatcher.callback.DispatchingCallback;
import org.springmodules.email.dispatcher.callback.EmptyDispatchingCallback;

/**
 * An asynchronous email dispatcher that uses a {@link TaskExecutor} to dispatch the emails. The concrete task
 * executor that is configured determines the asynchronous nature of this dispatcher (It is even possible to
 * make this dispatcher asynchronous by configuring it with {@link org.springframework.core.task.SyncTaskExecutor}).
 * Any mailing error that occurs during dispatch will be handled by the configured {@link DispatchingErrorHandler}. By
 * default the error handler is set to {@link LoggingDispatchingErrorHandler}.
 *
 * @author Uri Boness
 */
public class AsyncEmailDispatcher extends ConfigurableEmailDispatcher {

    private final static Log logger = LogFactory.getLog(AsyncEmailDispatcher.class);

    private final static DispatchingErrorHandler DEFAULT_ERROR_HANDLER = new LoggingDispatchingErrorHandler(logger);

    private final static DispatchingCallback DEFAULT_CALLBACK = new EmptyDispatchingCallback();

    private TaskExecutor taskExecutor;

    private DispatchingErrorHandler errorHandler = DEFAULT_ERROR_HANDLER;

    private DispatchingCallback dispachingCallback = DEFAULT_CALLBACK;

    /**
     * Sends the given email using the configured task executor.
     *
     * @param email The email to be sent.
     */
    public final void send(Email email) {
        taskExecutor.execute(new EmailDispatchingTask(email));
    }

    public void doAfterPropertiesSet() throws Exception {
        super.doAfterPropertiesSet();
        Assert.notNull(taskExecutor, "Property 'taskExecutor' is required");
        Assert.notNull(errorHandler, "Property 'errorHandler' is required");
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Returns the task executor used by this dispatcher.
     *
     * @return The task executor used by this dispatcher.
     */
    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    /**
     * Sets the task executor this dispatcher will use to dispatch the emails.
     *
     * @param taskExecutor The task executor this dispatcher will use to dispatch the emails.
     */
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * Returns the error handler that is used by this dispatcher to handle any email dispatching errors.
     *
     * @return The error handler used by this dispatcher.
     */
    public DispatchingErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Sets the error handler that will be used to handle any error that occur while dispatching the emails.
     *
     * @param errorHandler The error handler this dispatcher should use.
     */
    public void setErrorHandler(DispatchingErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * Sets the given dispatching callbak to be called after email are dispached.
     *
     * @param callback The dispatching callback.
     */
    public void setDispachingCallback(DispatchingCallback callback) {
        this.dispachingCallback = callback;
    }

    //============================================== Inner Classes =====================================================

    public class EmailDispatchingTask implements Runnable {

        private Email email;

        public EmailDispatchingTask(Email email) {
            this.email = email;
        }

        public void run() {
            try {
                getEmailSender().send(getMailSender(), email, getEncoding());
                dispachingCallback.emailDispatched(email, true);
            } catch (MailException me) {
                errorHandler.handle(me, email);
                dispachingCallback.emailDispatched(email, false);
            }
        }

        public Email getEmail() {
            return email;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EmailDispatchingTask that = (EmailDispatchingTask) o;

            if (email != null ? !email.equals(that.email) : that.email != null) return false;

            return true;
        }

        public int hashCode() {
            return (email != null ? email.hashCode() : 0);
        }
    }
}
