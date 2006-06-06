
/*
 * Title: Short descriptive title Description: A broad description of the
 * intention Copyright 2005 GigaSpaces Technologies Ltd. All rights reserved.
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT. GIGASPACES WILL NOT BE
 * LIABLE FOR ANY DAMAGE OR LOSS IN CONNECTION WITH THE SOFTWARE.
 */

package org.springmodules.javaspaces.gigaspaces;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import com.j_spaces.core.IJSpace;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springmodules.javaspaces.DelegatingWorker;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.gigaspaces.converter.IPojoToEntryConverter;
import org.springmodules.javaspaces.gigaspaces.GigaSpacesTemplate;
import org.springmodules.javaspaces.gigaspaces.app.JMSReceiver;
import org.springmodules.javaspaces.gigaspaces.app.JMSSender;
import org.springmodules.javaspaces.gigaspaces.app.SimpleBean;
import com.j_spaces.core.IJSpace;
import com.j_spaces.core.client.EntryArrivedRemoteEvent;
import com.j_spaces.core.client.ExternalEntry;
import com.j_spaces.core.client.NotifyModifiers;
import com.j_spaces.core.client.UpdateModifiers;


public class GigaSpacesJMSTest
extends AbstractDependencyInjectionSpringContextTests
{

	JMSSender	jmsSender	= null;
	JMSReceiver	jmsReceiver	= null;
	Thread t;

	protected String[] getConfigLocations()
	{
		return new String[] { "/config/JMS.xml"};
	}

	protected void onSetUp() throws Exception
	{
		jmsSender = (JMSSender) applicationContext.getBean("jmsSender");
		jmsReceiver = (JMSReceiver) applicationContext.getBean("jmsReceiver");
		createReceiverThreads();
	}


	protected void createReceiverThreads() {
		t  = new Thread(jmsReceiver);
		t.start();
	}

	public void testJMS() throws Exception
	{
		System.out.println("Before Sending message... Done!");
		jmsSender.sendMesage();
		System.out.println("Send message... Done!");
		Thread.sleep(1000);
	}

	protected void killWorkerThreads() {
		jmsReceiver.stop();
	}


	protected void onTearDown() throws Exception {
		killWorkerThreads();
	}
}