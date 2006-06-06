/*
 * Copyright 2005 GigaSpaces Technologies Ltd. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */
package org.springmodules.javaspaces.gigaspaces;

import java.io.Serializable;

import net.jini.core.entry.Entry;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springmodules.beans.ITestBean;
import org.springmodules.javaspaces.DelegatingWorker;
import org.springmodules.javaspaces.JavaSpaceInterceptor;
import org.springmodules.javaspaces.PutTests.PerformanceMonitorInterceptor;
import org.springmodules.javaspaces.entry.MethodResultEntry;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.util.StopWatch;
import com.j_spaces.core.IJSpace;

import org.springmodules.javaspaces.gigaspaces.GigaSpacesInterceptor;
import org.springmodules.javaspaces.gigaspaces.GigaSpacesTemplate;



public class GigaSpacesRemotingTest extends AbstractDependencyInjectionSpringContextTests {
	//member for gigaspaces template
	private GigaSpacesTemplate template;
	//The delegator worker
	private DelegatingWorker iTestBeanWorker;



	private Thread itbThread;

	protected void onSetUp() throws Exception {
		template = (GigaSpacesTemplate)applicationContext.getBean("gigaSpacesTemplate");
		iTestBeanWorker = (DelegatingWorker)applicationContext.getBean("testBeanWorker");
		createWorkerThreads();
	}

	protected void createWorkerThreads() {
		itbThread = new Thread(iTestBeanWorker);
		itbThread.start();
	}

	protected void killWorkerThreads() {
		iTestBeanWorker.stop();

		// TODO why is this necessary?
		itbThread.stop();
	}

	protected void onTearDown() throws Exception {
		killWorkerThreads();
	}

	public void testSuccessfulInvocations() throws Exception {
		template.clean();
		ITestBean proxy =  (ITestBean)applicationContext.getBean("proxy");
		for (int i = 0; i < 2; i++) {
			String name = "john" + i;
			proxy.setName(name);
			assertEquals(name, proxy.getName());
		}
	}


	protected String[] getConfigLocations() {
		return new String[] {/*"/config/common.xml",*/ "/config/gigaspaces_remoting.xml" };
//		return new String[] {"/config/common.xml"};
	}

	public void testLazyTest() throws Throwable {
		template.clean();
				ITestBean proxy =  (ITestBean)applicationContext.getBean("proxy");
		GigaSpacesInterceptor gigaSpacesInterceptor = (GigaSpacesInterceptor)applicationContext.getBean("javaSpaceInterceptor");
		gigaSpacesInterceptor.setSynchronous(false);
		ITestBean lazyResult = proxy.getSpouse();
		assertTrue(AopUtils.isCglibProxy(lazyResult));
		System.out.println("should not be initialized");
		System.out.println(lazyResult.getClass());
		System.out.println(lazyResult.hashCode());
		System.out.println("should not be initialized");
		System.out.println("The lazy result"+lazyResult);
		System.out.println("should be initialized");
		System.out.println("the name " +lazyResult.getName());
		assertEquals("kerry", lazyResult.getName());
	}

	public void testAsynchInterceptor() throws Throwable {
		template.clean();
		ITestBean proxy =  (ITestBean)applicationContext.getBean("proxy");
		GigaSpacesInterceptor gigaSpacesInterceptor = (GigaSpacesInterceptor)applicationContext.getBean("javaSpaceInterceptor");
		gigaSpacesInterceptor.setSynchronous(false);
		try {
			proxy.getName();
			fail("cglib can't proxy final classes");
		}
		catch (IllegalArgumentException e) {
			// can't proxy strings
			e.printStackTrace();
		}
	}


	public class PerformanceMonitorInterceptor implements MethodInterceptor, Serializable {

		public Object invoke(MethodInvocation invocation) throws Throwable {
			String name = invocation.getMethod().getDeclaringClass().getName() + "." + invocation.getMethod().getName();
			StopWatch sw = new StopWatch(name);
			sw.start(name);
			try {
				return invocation.proceed();
			}
			finally {
				sw.stop();

				System.out.println(sw.shortSummary());
			}
		}


	}
	

}
