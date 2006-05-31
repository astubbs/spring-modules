/*
 * Copyright 2002-2006 the original author or authors.
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
package org.springmodules;

import junit.framework.TestCase;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.AbstractLazyCreationTargetSource;

public class LazyTests extends TestCase {

	public static class SomeObject {
		public SomeObject() {
			System.out.println("proxy created");
		}

		public void initObject()
		{
			System.out.println("super init");
		}
		public void doSmth() {
			System.out.println("do smth");
		}
	}

	public void testLazyTest() {
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setProxyTargetClass(true);

		TargetSource targetSource = new AbstractLazyCreationTargetSource() {
			protected Object createObject() {
				System.out.println("lazy loading");
				SomeObject obj = new SomeObject();
				obj.initObject();
				return obj;
			}

			public Class getTargetClass() {
				return SomeObject.class;
			}
		};

		proxyFactory.setTargetSource(targetSource);

		Object proxy = proxyFactory.getProxy();
		System.out.println("proxy class is " + proxy);
		assertTrue(proxy instanceof SomeObject);
		((SomeObject) proxy).doSmth();
		
		System.out.println("another proxy");
		System.out.println(ProxyFactory.getProxy(ProxyFactory.EMPTY_TARGET_SOURCE));

	}

	public void testCGLibLazyLoaderTest() {
		LazyLoader loader = new LazyLoader() {
			public Object loadObject() throws Exception {
				System.out.println("lazy loading");
				SomeObject obj = new SomeObject();
				obj.initObject();
				return obj;
			}
		};

		//Enhancer enhancer = new Enhancer();
		//enhancer.setSuperclass(SomeObject.class);
		//enhancer.setCallback(loader);
		//Object proxy = enhancer.create();
		Object proxy = Enhancer.create(SomeObject.class, loader);

		assertTrue(proxy instanceof SomeObject);
		System.out.println(proxy.getClass());
		((SomeObject)proxy).doSmth();

	}
}
