package org.springmodules.beans.factory.bfl.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate.HibernateTransactionManager;
import org.springframework.orm.hibernate.LocalSessionFactoryBean;
import org.springmodules.beans.factory.drivers.Alias;
import org.springmodules.beans.factory.drivers.Bean;
import org.springmodules.beans.factory.drivers.BeanReference;
import org.springmodules.beans.factory.drivers.LiteralList;
import org.springmodules.beans.factory.drivers.LiteralProperties;
import org.springmodules.beans.factory.drivers.LiteralValue;
import org.springmodules.beans.factory.drivers.xml.XmlApplicationContextDriver;
import org.springmodules.commons.collections.functors.ApplyPriorityClosure;
import org.springmodules.commons.collections.functors.ChainedClosureFactoryBean;
import org.springmodules.commons.collections.functors.IfParserClosureFactoryBean;

import junit.framework.TestCase;

public class BeanFactoryLanguageParserTests extends TestCase {

	public BeanFactoryLanguageParserTests() {
		super();
	}

	public BeanFactoryLanguageParserTests(String arg0) {
		super(arg0);
	}

	private Collection parse(String bfl, ApplicationContext appCtx) {
		try {
			BeanFactoryLanguageParser parser = new BeanFactoryLanguageParser(new StringReader(bfl));
			parser.setApplicationContext(appCtx);
			return parser.parse();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Collection parse(String bfl) {
		return parse(bfl, null);
	}
	
	private Collection parse(Resource resourse) {
		try {
			return new BeanFactoryLanguageParser(resourse.getInputStream()).parse();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
//	public void testBFLParser1() {
//		Collection coll = parse(
//			"abstractDataSource = new abstract org.springframework.jdbc.datasource.DriverManagerDataSource;\n" +
//			"abstractDataSource.driverClassName = 'org.hsqldb.jdbcDriver';\n" +
//			"abstractDataSource.url = 'jdbc:hsqldb:hsql://localhost:';\n" +
//			"abstractDataSource.username = 'sa';\n" +
//			"abstractDataSource.password = '';\n" +
//			"dataSource = new child of abstractDataSource;\n" +
//			"sessionFactory = new org.springframework.orm.hibernate.LocalSessionFactoryBean;\n" +
//			"sessionFactory.dataSource = dataSource;\n" +
//			"sessionFactory.mappingResources = [ 'domain.hbm.xml' ];\n" +
//			"sessionFactory.hibernateProperties = { 'hibernate.dialect' -> 'sf.hibernate.dialect.HSQLDialect' };\n" +
//			"transactionManager = new org.springframework.orm.hibernate.HibernateTransactionManager;\n" +
//			"transactionManager.sessionFactory = sessionFactory;\n" +
//			"transactionAttributes = new java.util.Properties({ '*' -> 'PROPAGATION_REQUIRED' });\n" +
//			""
//		); 
//		
//		Iterator iter = coll.iterator();
//		BeanReference beanReference = null;
//		
//		beanReference = (BeanReference)iter.next();
//		
//		assertEquals("abstractDataSource", beanReference.getBeanName());
//		assertTrue(beanReference.getBean().isAbstract());
//		assertFalse(beanReference.getBean().isLazy());
//		assertTrue(beanReference.getBean().isSingleton());
//		assertEquals(DriverManagerDataSource.class, beanReference.getBean().getClazz());
//		assertNull(beanReference.getBean().getParent());
//		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
//		
//		assertEquals("org.hsqldb.jdbcDriver", ((LiteralValue)beanReference.getBean().getProperties().get("driverClassName")).getValue());
//		assertEquals("jdbc:hsqldb:hsql://localhost:", ((LiteralValue)beanReference.getBean().getProperties().get("url")).getValue());
//		assertEquals("sa", ((LiteralValue)beanReference.getBean().getProperties().get("username")).getValue());
//		assertEquals("", ((LiteralValue)beanReference.getBean().getProperties().get("password")).getValue());
//		
//		beanReference = (BeanReference)iter.next();
//		
//		assertEquals("dataSource", beanReference.getBeanName());
//		assertFalse(beanReference.getBean().isAbstract());
//		assertFalse(beanReference.getBean().isLazy());
//		assertTrue(beanReference.getBean().isSingleton());
//		assertNull(beanReference.getBean().getClazz());
//		assertEquals("abstractDataSource", ((BeanReference)beanReference.getBean().getParent()).getBeanName());
//		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
//		
//		beanReference = (BeanReference)iter.next();
//		
//		assertEquals("sessionFactory", beanReference.getBeanName());
//		assertFalse(beanReference.getBean().isAbstract());
//		assertFalse(beanReference.getBean().isLazy());
//		assertTrue(beanReference.getBean().isSingleton());
//		assertEquals(LocalSessionFactoryBean.class, beanReference.getBean().getClazz());
//		assertNull(beanReference.getBean().getParent());
//		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
//		
//		assertEquals(1, ((LiteralList)beanReference.getBean().getProperties().get("mappingResources")).getElements().size());
//		assertEquals("dataSource", ((BeanReference)beanReference.getBean().getProperties().get("dataSource")).getBeanName());
//		assertEquals(1, ((LiteralProperties)beanReference.getBean().getProperties().get("hibernateProperties")).getProperties().size());
//		
//		beanReference = (BeanReference)iter.next();
//		
//		assertEquals("transactionManager", beanReference.getBeanName());
//		assertFalse(beanReference.getBean().isAbstract());
//		assertFalse(beanReference.getBean().isLazy());
//		assertTrue(beanReference.getBean().isSingleton());
//		assertEquals(HibernateTransactionManager.class, beanReference.getBean().getClazz());
//		assertNull(beanReference.getBean().getParent());
//		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
//		
//		assertEquals("sessionFactory", ((BeanReference)beanReference.getBean().getProperties().get("sessionFactory")).getBeanName());
//		
//		beanReference = (BeanReference)iter.next();
//		
//		assertEquals("transactionAttributes", beanReference.getBeanName());
//		assertFalse(beanReference.getBean().isAbstract());
//		assertFalse(beanReference.getBean().isLazy());
//		assertTrue(beanReference.getBean().isSingleton());
//		assertEquals(Properties.class, beanReference.getBean().getClazz());
//		assertNull(beanReference.getBean().getParent());
//		assertEquals(1, beanReference.getBean().getConstructorArguments().size());
//		
//		assertFalse(iter.hasNext());
//		
//		ApplicationContext appCtx = new XmlApplicationContextDriver().getApplicationContext(coll);
//	}
	
	public void testBFLParser2() {
		Collection coll2 = parse(new ClassPathResource("org/springmodules/beans/factory/bfl/parser/common_beans.bfl"));
		ApplicationContext appCtx2 = new XmlApplicationContextDriver().getApplicationContext(coll2, null);
		
		Collection coll3 = parse("alias for if = 'test'\n", appCtx2);
		ApplicationContext appCtx3 = new XmlApplicationContextDriver().getApplicationContext(coll3, appCtx2);
		
		Collection coll = parse(
				"import org/springmodules/beans/factory/bfl/parser/common_beans.bfl\n" +
				"lowPriorityRule = new child of if\n" +
				"description for lowPriorityRule = <<< END\n" +
				"handle order with low priority if total amount is lower than 1000 \n" +
				"and customer is not silver or gold.\n" +
				"END\n" +
				"lowPriorityRule.syntax = <<< END\n" +
				"totalAmount < 1000 and lower(customer.qualification) not in 'silver', 'gold'\n" +
				"END\n" +
				"lowPriorityRule.trueClosure = new child of applyPriorityClosure('low')\n" +
				"mediumPriorityRule = new child of if\n" +
				"mediumPriorityRule.syntax = 'totalAmount < 5000 and lower(customer.qualification) not in \\'gold\\''\n" +
				"mediumPriorityRule.trueClosure = new child of applyPriorityClosure('medium')\n" +
				"lowPriorityRule.falseClosure = mediumPriorityRule\n" +
				"highPriorityRule = new child of chain\n" +
				"highPriorityRule.closures = [ new child of applyPriorityClosure('high') ]\n" +
				"mediumPriorityRule.falseClosure = highPriorityRule\n" +
				"start = new child of chain()\n" +
				"start.closures = [ lowPriorityRule ]\n" +
				"alias for start = 'begin'\n" +
				"init method for begin = ''\n" +
				"destroy method for start = ''\n" +
				"begin depends on lowPriorityRule, mediumPriorityRule, highPriorityRule\n" +
				"set autowire for start to none\n" +
				""
		);
		
		Iterator iter = coll.iterator();
		BeanReference beanReference = null;
		Alias alias = null;
		
		beanReference = (BeanReference)iter.next();
		
		assertEquals("if", beanReference.getBeanName());
		assertTrue(beanReference.getBean().isAbstract());
		assertFalse(beanReference.getBean().isLazy());
		assertTrue(beanReference.getBean().isSingleton());
		assertEquals(IfParserClosureFactoryBean.class, beanReference.getBean().getClazz());
		assertNull(beanReference.getBean().getParent());
		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
		
		beanReference = (BeanReference)iter.next();
		
		assertEquals("chain", beanReference.getBeanName());
		assertTrue(beanReference.getBean().isAbstract());
		assertFalse(beanReference.getBean().isLazy());
		assertTrue(beanReference.getBean().isSingleton());
		assertEquals(ChainedClosureFactoryBean.class, beanReference.getBean().getClazz());
		assertNull(beanReference.getBean().getParent());
		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
		
		beanReference = (BeanReference)iter.next();
		
		assertEquals("applyPriorityClosure", beanReference.getBeanName());
		assertTrue(beanReference.getBean().isAbstract());
		assertFalse(beanReference.getBean().isLazy());
		assertTrue(beanReference.getBean().isSingleton());
		assertEquals(ApplyPriorityClosure.class, beanReference.getBean().getClazz());
		assertNull(beanReference.getBean().getParent());
		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
		
		beanReference = (BeanReference)iter.next();
		
		assertEquals("lowPriorityRule", beanReference.getBeanName());
		assertFalse(beanReference.getBean().isAbstract());
		assertFalse(beanReference.getBean().isLazy());
		assertTrue(beanReference.getBean().isSingleton());
		assertNull(beanReference.getBean().getClazz());
		assertEquals("if", beanReference.getBean().getParent().getBeanName());
		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
		assertEquals(beanReference.getBean().getDescription(), "handle order with low priority if total amount is lower than 1000 \nand customer is not silver or gold.\n");
		
		assertEquals("totalAmount < 1000 and lower(customer.qualification) not in 'silver', 'gold'\n",
				((LiteralValue)beanReference.getBean().getProperties().get("syntax")).getValue());
		assertNotNull(beanReference.getBean().getProperties().get("trueClosure"));
		assertInstanceOf(Bean.class, beanReference.getBean().getProperties().get("trueClosure"));
		assertNotNull(beanReference.getBean().getProperties().get("falseClosure"));
		assertInstanceOf(BeanReference.class, beanReference.getBean().getProperties().get("falseClosure"));
		
		beanReference = (BeanReference)iter.next();
		
		assertEquals("mediumPriorityRule", beanReference.getBeanName());
		assertFalse(beanReference.getBean().isAbstract());
		assertFalse(beanReference.getBean().isLazy());
		assertTrue(beanReference.getBean().isSingleton());
		assertNull(beanReference.getBean().getClazz());
		assertEquals("if", beanReference.getBean().getParent().getBeanName());
		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
		
		assertEquals("totalAmount < 5000 and lower(customer.qualification) not in 'gold'",
				((LiteralValue)beanReference.getBean().getProperties().get("syntax")).getValue());
		assertNotNull(beanReference.getBean().getProperties().get("trueClosure"));
		assertInstanceOf(Bean.class, beanReference.getBean().getProperties().get("trueClosure"));
		assertNotNull(beanReference.getBean().getProperties().get("falseClosure"));
		assertInstanceOf(BeanReference.class, beanReference.getBean().getProperties().get("falseClosure"));
		
		beanReference = (BeanReference)iter.next();
		
		assertEquals("highPriorityRule", beanReference.getBeanName());
		assertFalse(beanReference.getBean().isAbstract());
		assertFalse(beanReference.getBean().isLazy());
		assertTrue(beanReference.getBean().isSingleton());
		assertNull(beanReference.getBean().getClazz());
		assertEquals("chain", beanReference.getBean().getParent().getBeanName());
		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
		
		assertEquals(1, ((LiteralList)beanReference.getBean().getProperties().get("closures")).getElements().size());
		
		beanReference = (BeanReference)iter.next();
		
		assertEquals("start", beanReference.getBeanName());
		assertFalse(beanReference.getBean().isAbstract());
		assertFalse(beanReference.getBean().isLazy());
		assertTrue(beanReference.getBean().isSingleton());
		assertNull(beanReference.getBean().getClazz());
		assertEquals("chain", beanReference.getBean().getParent().getBeanName());
		assertEquals(0, beanReference.getBean().getConstructorArguments().size());
		assertEquals(3, beanReference.getBean().getDependsOn().size());
		assertEquals("no", beanReference.getBean().getAutowire());
		
		assertEquals(1, ((LiteralList)beanReference.getBean().getProperties().get("closures")).getElements().size());
		
		alias = (Alias)iter.next();
		
		assertEquals("start", alias.getReferencedBean().getBeanName());
		assertEquals("begin", alias.getAlias());
		
		assertFalse(iter.hasNext());
		
		ApplicationContext appCtx = new XmlApplicationContextDriver().getApplicationContext(coll, null);
	}
	
	private void assertInstanceOf(Class type, Object o) {
		assertTrue(type.isAssignableFrom(o.getClass()));
	}
}
