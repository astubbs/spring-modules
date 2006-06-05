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

		Entry entry = new Entry() {};

		Entry e = null;
		do  {
//			e = template.takeIfExists(entry, 5);
			System.out.println("Cleanup: " + e);
		}
		while (e != null);
	}

	public void testSuccessfulInvocations() throws Exception {
		//template.clean();
		ITestBean proxy =  (ITestBean)applicationContext.getBean("proxy");
		for (int i = 0; i < 2; i++) {
			String name = "john" + i;
			proxy.setName(name);
			assertEquals(name, proxy.getName());
		}
	}

//	public void testCheckedException() throws Throwable {
//		testThrowable(new NumberFormatException());
//	}
//
//	public void testUncheckedException() throws Throwable {
//		// TODO why does this get no such class exception?
//		//testThrowable(new NoSuchBeanDefinitionException("", ""));
//		testThrowable(new RuntimeException(""));
//	}

	private void testThrowable(Throwable t) throws Throwable {

		ITestBean proxy =  (ITestBean)applicationContext.getBean("proxy");
		try {
			proxy.exceptional(t);
			fail("Should have aborted with exception");
		}
		catch (Throwable got) {
			if (got.getClass().equals(t.getClass())) {
				// Ok
				System.out.println("Expected exception, " + got);
			}
			else {
				throw got;
			}
		}
	}
	protected String[] getConfigLocations() {
		return new String[] {"common.xml", "gigaspaces_remoting.xml" };
//		return new String[] {"common.xml"};
	}

	public void testLazyTest() throws Throwable {
		///template.clean();
				ITestBean proxy =  (ITestBean)applicationContext.getBean("proxy");
		GigaSpacesInterceptor gigaSpacesInterceptor = (GigaSpacesInterceptor)applicationContext.getBean("javaSpaceInterceptor");
		//JavaSpaceInterceptor gigaSpacesInterceptor = (JavaSpaceInterceptor)applicationContext.getBean("javaSpaceInterceptor");
		gigaSpacesInterceptor.setSynchronous(false);


//		JavaSpaceInterceptor si = new JavaSpaceInterceptor(template);
//		si.setSynchronous(false);
//		ProxyFactory pf = new ProxyFactory(new Class[] { ITestBean.class });
//		pf.addAdvice(new PerformanceMonitorInterceptor());
//		pf.addAdvice(si);
//		ITestBean proxy = (ITestBean) pf.getProxy();


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
