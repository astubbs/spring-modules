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

package org.springmodules.email.dispatcher.errorhandler;

import org.springmodules.email.Email;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;

/**
 * A dispatching error handler that logs the error using commons logging logger.
 *
 * @author Uri Boness
 */
public class LoggingDispatchingErrorHandler implements DispatchingErrorHandler {

    private final static Log DEFAULT_LOGGER = LogFactory.getLog(LoggingDispatchingErrorHandler.class);

    private final static short INFO_LEVEL = 0;
    private final static short DEBUG_LEVEL = 1;
    private final static short TRACE_LEVEL = 2;
    private final static short ERROR_LEVEL = 3;
    private final static short WARN_LEVEL = 4;
    private final static short FATAL_LEVEL = 5;

    private Log logger;

    private short level;

    /**
     * Constructs a new LoggingDispatchingErrorHandler with a default logger (bound to this class). The log level is
     * set to <code>error</code>
     */
    public LoggingDispatchingErrorHandler() {
        this(DEFAULT_LOGGER);
    }

    /**
     * Constructs a new LoggingDispatchingErrorHandler with a given logger. The log level is set to <code>error</code>
     *
     * @param logger The logger to use for logging.
     */
    public LoggingDispatchingErrorHandler(Log logger) {
        this(logger, ERROR_LEVEL);
    }

    /**
     * Constructs a new LoggingDispatchingErrorHandler with a given logger and logging level.
     *
     * @param logger The logger to be used.
     * @param level The log level to be used.
     */
    public LoggingDispatchingErrorHandler(Log logger, short level) {
        this.logger = logger;
        this.level = level;
    }

    /**
     * Logs the given mail dispatching error using the configured logger and log level. The logged message is
     * constructed by the {@link #getErrorMessage(Throwable, org.springmodules.email.Email)} method.
     *
     * @see LoggingDispatchingErrorHandler#handle(MailException, org.springmodules.email.Email)
     */
    public final void handle(MailException error, Email email) {
        switch (level) {
            case INFO_LEVEL:
                if (logger.isInfoEnabled()) {
                    logger.info(getErrorMessage(error, email), error);
                }
                break;
           case DEBUG_LEVEL:
                if (logger.isDebugEnabled()) {
                    logger.debug(getErrorMessage(error, email), error);
                }
                break;
           case TRACE_LEVEL:
                if (logger.isTraceEnabled()) {
                    logger.trace(getErrorMessage(error, email), error);
                }
                break;
           case WARN_LEVEL:
                if (logger.isWarnEnabled()) {
                    logger.warn(getErrorMessage(error, email), error);
                }
                break;
           case FATAL_LEVEL:
                if (logger.isFatalEnabled()) {
                    logger.fatal(getErrorMessage(error, email), error);
                }
                break;
           default:
                if (logger.isErrorEnabled()) {
                    logger.error(getErrorMessage(error, email), error);
                }
        }
    }

    /**
     * Constructs the log message based on the given error and email. This method can be overriden to providen custom
     * log messages.
     *
     * @param error The error that occured.
     * @param email The email that failed to be sent.
     * @return The message that should be logged.
     */
    protected String getErrorMessage(Throwable error, Email email) {
        return "Could not send email message '" + email + "' due to the following error: '" + error.getMessage() + "'";
    }

}
