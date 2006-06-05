/*
 * Copyright 2005 GigaSpaces Technologies Ltd. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */

package org.springmodules.javaspaces.gigaspaces.app;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate102;
import org.springframework.jms.core.MessageCreator;

/**
 * Title:
 * Description:   <p>

 * Copyright:    Copyright 2006 GigaSpaces Technologies Ltd. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 */
public class JMSReceiver implements Runnable {

    private JmsTemplate102 jmsTemplate102;
    private boolean running = true;
    public JmsTemplate102 getJmsTemplate102() {
      return jmsTemplate102;
    }

    public void setJmsTemplate102(JmsTemplate102 jmsTemplate102) {
     this.jmsTemplate102 = jmsTemplate102;
    }

    public void processMessage(){
        Message msg = jmsTemplate102.receive();
      try{
        TextMessage textMessage = (TextMessage) msg;
        if( msg!=null){
        System.out.println(" Message Received -->" +
                    textMessage.getText());
        }


      }catch(Exception e){
            e.printStackTrace();
      }


    }

    public void stop() {
        this.running = false;
    }

    public void run()
    {
        while(running){
            processMessage();
        }

    }
}

