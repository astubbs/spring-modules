package org.springmodules.xt.examples.domain.util;

import org.springmodules.xt.examples.domain.BusinessException;
import org.springmodules.xt.examples.domain.Error;
import org.springmodules.xt.model.notification.Message;
import org.springmodules.xt.model.notification.Notification;

/**
 * Domain related utility methods.
 *
 * @author Sergio Bossa
 */
public class DomainUtils {
    
    public static BusinessException notificationErrorsToBusinessException(Notification notification) {
        BusinessException ex = new BusinessException();
        for (Message m : notification.getMessages(Message.Type.ERROR)) {
            Error error = new Error(m.getCode(), m.getDefaultMessage(), m.getPropertyName());
            ex.addError(error);
        }
        return ex;
    }
    
}
