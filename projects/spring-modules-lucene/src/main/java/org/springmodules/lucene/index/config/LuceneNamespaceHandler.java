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

package org.springmodules.lucene.index.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * <code>NamespaceHandler</code> allowing for the configuration of entities
 * of the Lucene support including: configuration of Directory and IndexFactory,
 * DocumentHandlerManager and ResourceManager.
 * 
 * The configuration of Directory and {@link org.springmodules.lucene.index.factory.IndexFactory}
 * is made with the &lt;index&gt; tag which configures these two entities. The type of the
 * directory can be both RAM or FS according to the presence of the
 * <code>location</code> attribute of the tag. This attribute specifies the
 * location of a FS directory.
 * 
 * The &lt;index&gt; tag can configure too a concurrent IndexFactory for
 * applications which need to make concurrent access on an index. The
 * <code>concurrent</code> attribute allows to specify the kind of strategy
 * to use. The values lock and channel are possible in order to specify
 * respectively a concurrency management based on a lock and on a channel.
 * For more information, see the javadocs of classes
 * {@link org.springmodules.lucene.index.factory.concurrent.LockIndexFactory}
 * and {@link org.springmodules.lucene.index.factory.concurrent.ChannelIndexFactory}.
 * 
 * The following code shows a sample of use of the &lt;index&gt; tag:
 * 
 * <code>
 * &lt;lucene:index id="indexFactory1" analyzer-ref="analyzer" location="/tmp/lucene" /&gt;
 * 
 * &lt;lucene:index id="indexFactory1" analyzer-ref="analyzer" location="/tmp/lucene"&gt;
 *     &lt;lucene:analyzer&gt;
 *         &lt;bean class="org.apache.lucene.analysis.SimpleAnalyzer"/&gt;
 *     &lt;/lucene:analyzer&gt;
 * &lt;/lucene:index&gt;
 * </code>
 * 
 * The &lt;document-handler&gt; allows to easily configure a
 * {@link org.springmodules.lucene.index.document.handler.DocumentHandlerManager.DocumentHandlerManager}
 * based on identity or file extensions. The selection of the strategy is based on the
 * value of the <code>type</code> attribute, identity or extension. All the default document
 * handlers are automatically registered with this configuration. 
 *  
 * The following code shows a sample of use of the &lt;index&gt; tag:
 * 
 * <code>
 * &lt;lucene:document-handler id="documentHandlerManager" type="extension"/&gt;
 * </code>
 * 
 * The &lt;resource-advise&gt; allows to easily configure strategies of resource management
 * for methods based on its name or a pattern. 
 * 
 * The following code shows a sample of use of the &lt;index&gt; tag:
 * 
 * &lt;bean id="resourceManager" class="org.springmodules.lucene.index.resource.DefaultResourceManager"&gt;
 *     &lt;property name="indexFactory" ref="indexFactory"/&gt;
 * &lt;/bean&gt;
 *
 * &lt;lucene:resource-advice id="resourceAdvice" resource-manager="resourceManager"&gt;
 *     &lt;lucene:attributes&gt;
 *         &lt;lucene:method name="myMethod1" writer-open="true" writer-writing-enabled="true"/&gt;
 *         &lt;lucene:method name="myMethod2" writer-open="true" writer-writing-enabled="true"/&gt;
 *         &lt;lucene:method name="myMethod3" reader-open="true" reader-writing-enabled="true"/&gt;
 *         &lt;lucene:method name="myMethod4" reader-open="true" reader-writing-enabled="true"/&gt;
 *     &lt;/lucene:attributes&gt;
 * &lt;/lucene:resource-advice&gt;
 *
 * For more information, see the javadocs of classes
 * {@link org.springmodules.lucene.index.resource.ResourceInterceptor}.
 * 
 * @author Thierry Templier
 */
public class LuceneNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("index", new IndexBeanDefinitionParser());
		registerBeanDefinitionParser("document-handler", new DocumentHanderBeanDefinitionParser());
		registerBeanDefinitionParser("resource-advice", new ResourceAdviceBeanDefinitionParser());
	}

}
