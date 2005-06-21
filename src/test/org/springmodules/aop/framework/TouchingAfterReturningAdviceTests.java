package org.springmodules.aop.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

public class TouchingAfterReturningAdviceTests extends TestCase {

	public TouchingAfterReturningAdviceTests() {
		super();
	}
	
	private static final String BEAN_NAME = "Bean name";
	
	private Bean getBean() {
		Bean bean = new DefaultBean();
		bean.setBeans(new Bean[] {
			new DefaultBean() {
				{
					setBean(new DefaultBean());
					setBeans(new Bean[] { new DefaultBean() });
				}
			},
			new DefaultBean() {
				{
					setBeans(new Bean[] { new DefaultBean() });
				}
			}
		});
		bean.setBean(bean);
		bean.setOtherBeans(Arrays.asList(bean.getBeans()));
		return bean;
	}

	
	public void testTouchingAdvice() {
		Bean bean = (Bean)getProxy(getBean(), new String[] { "autoName" }, new String[] { "autoName"}, new String[] { "getBean" });
		bean.getBean();
		assertEquals(BEAN_NAME, bean.getName());
		
		bean = (Bean)getProxy(getBean(), new String[] { "autoName" }, new String[] { "#returned.{autoName}" }, new String[] { "getBeans" });
		Bean[] beans = bean.getBeans();
		assertEquals(BEAN_NAME, beans[0].getName());
		assertEquals(BEAN_NAME, beans[1].getName());
		
		bean = (Bean)getProxy(getBean(), new Object[] { "autoName" }, new String[] { "#returned.{autoName}" }, new String[] { "getOtherBeans" });
		List otherBeans = bean.getOtherBeans();
		assertEquals(BEAN_NAME, ((Bean)otherBeans.get(0)).getName());
		assertEquals(BEAN_NAME, ((Bean)otherBeans.get(1)).getName());

		Map map = new HashMap();
		List list = new ArrayList();
		list.add("autoName");
		map.put("beans", list);
		bean = (Bean)getProxy(getBean(), new Object[] { "autoName", map  }, new String[] { "#returned.{beans.{autoName}}" }, new String[] { "getBeans" });
		beans = bean.getBeans();
		assertEquals(BEAN_NAME, beans[0].getName());
		assertEquals(BEAN_NAME, beans[1].getName());
		assertEquals(BEAN_NAME, beans[0].getBeans()[0].getName());
		assertEquals(BEAN_NAME, beans[1].getBeans()[0].getName());
	}

	
	
	private Object getProxy(Object target, Object[] properties, String[] ognl, String[] mappedNames) {
		TouchingAfterReturningAdvice advice = new TouchingAfterReturningAdvice();
		advice.setProperties(properties);
		advice.setOgnl(ognl);
		NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor(advice);
		advisor.setMappedNames(mappedNames);
		ProxyFactory pf = new ProxyFactory(target);
		pf.addAdvisor(advisor);
		return pf.getProxy();
	}
	
	
	
	public interface Bean {
		public Bean[] getBeans();
		public void setBeans(Bean[] beans);
		public List getOtherBeans();
		public void setOtherBeans(List otherBeans);
		public Bean getBean();
		public void setBean(Bean bean);
		public String getName();
		public String getAutoName();
	}
	
	public static class DefaultBean implements Bean {
		private Bean[] beans = null;
		private Bean bean = null;
		private List otherBeans = null;
		public String name = null;
		
		public Bean getBean() {
			return this.bean;
		}
		
		public Bean[] getBeans() {
			return this.beans;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getAutoName() {
			this.name = BEAN_NAME;
			return this.name;
		}
		
		public List getOtherBeans() {
			return this.otherBeans;
		}
		
		public void setBean(Bean bean) {
			this.bean = bean;
		}
		
		public void setBeans(Bean[] beans) {
			this.beans = beans;
		}
		
		public void setOtherBeans(List otherBeans) {
			this.otherBeans = otherBeans;
		}
	}
}
