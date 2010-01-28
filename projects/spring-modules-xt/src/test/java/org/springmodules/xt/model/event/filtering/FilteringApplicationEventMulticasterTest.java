/*
 * Copyright 2007 the original author or authors.
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

package org.springmodules.xt.model.event.filtering;

import org.jmock.integration.junit3.MockObjectTestCase;

/**
 * @author Sergio Bossa
 */
public abstract class FilteringApplicationEventMulticasterTest extends MockObjectTestCase {
   /*
    private FilteringApplicationEventMulticaster multicaster;
    
    public FilteringApplicationEventMulticasterTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.multicaster = new FilteringApplicationEventMulticaster();
    }

    public void testMulticastToFilteringListeners() {
        Mock listenerMock1 = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedListener1 = (FilteringApplicationListener) listenerMock1.proxy();
        Mock listenerMock2 = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedListener2 = (FilteringApplicationListener) listenerMock2.proxy();
        Mock listenerMock3 = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedListener3 = (FilteringApplicationListener) listenerMock3.proxy();
        
        PayloadEvent event = new PayloadEvent(this);
        
        listenerMock1.expects(once()).method("getSupportedEventClasses").withNoArguments().will(returnValue(new Class[]{ApplicationEvent.class}));
        listenerMock1.expects(once()).method("accepts").with(same(event)).will(returnValue(true));
        listenerMock1.expects(once()).method("onApplicationEvent").with(same(event));
        listenerMock2.expects(once()).method("getSupportedEventClasses").withNoArguments().will(returnValue(new Class[]{PayloadEvent.class}));
        listenerMock2.expects(once()).method("accepts").with(same(event)).will(returnValue(true));
        listenerMock2.expects(once()).method("onApplicationEvent").with(same(event));
        listenerMock3.expects(once()).method("getSupportedEventClasses").withNoArguments().will(returnValue(new Class[]{TestEvent.class}));
        listenerMock3.expects(never()).method("accepts");
        listenerMock3.expects(never()).method("onApplicationEvent");
        
        this.multicaster.addApplicationListener(mockedListener1);
        this.multicaster.addApplicationListener(mockedListener2);
        this.multicaster.addApplicationListener(mockedListener3);
        
        this.multicaster.multicastEvent(event);
    }
    
    public void testMulticastToStandardListeners() {
        Mock listenerMock1 = mock(ApplicationListener.class);
        ApplicationListener mockedListener1 = (ApplicationListener) listenerMock1.proxy();
        Mock listenerMock2 = mock(ApplicationListener.class);
        ApplicationListener mockedListener2 = (ApplicationListener) listenerMock2.proxy();
        Mock listenerMock3 = mock(ApplicationListener.class);
        ApplicationListener mockedListener3 = (ApplicationListener) listenerMock3.proxy();
        
        PayloadEvent event = new PayloadEvent(this);
        
        listenerMock1.expects(once()).method("onApplicationEvent").with(same(event));
        listenerMock2.expects(once()).method("onApplicationEvent").with(same(event));
        listenerMock3.expects(once()).method("onApplicationEvent").with(same(event));
        
        this.multicaster.addApplicationListener(mockedListener1);
        this.multicaster.addApplicationListener(mockedListener2);
        this.multicaster.addApplicationListener(mockedListener3);
        
        this.multicaster.multicastEvent(event);
    }
    
    public void testMulticastToListenersWithTaskExecutor() throws InterruptedException {
        this.multicaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        
        Mock listenerMock1 = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedListener1 = (FilteringApplicationListener) listenerMock1.proxy();
        Mock listenerMock2 = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedListener2 = (FilteringApplicationListener) listenerMock2.proxy();
        Mock listenerMock3 = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedListener3 = (FilteringApplicationListener) listenerMock3.proxy();
        
        PayloadEvent event = new PayloadEvent(this);
        
        listenerMock1.expects(once()).method("getSupportedEventClasses").withNoArguments().will(returnValue(new Class[]{ApplicationEvent.class}));
        listenerMock1.expects(once()).method("accepts").with(same(event)).will(returnValue(true));
        listenerMock1.expects(once()).method("onApplicationEvent").with(same(event));
        listenerMock2.expects(once()).method("getSupportedEventClasses").withNoArguments().will(returnValue(new Class[]{PayloadEvent.class}));
        listenerMock2.expects(once()).method("accepts").with(same(event)).will(returnValue(true));
        listenerMock2.expects(once()).method("onApplicationEvent").with(same(event));
        listenerMock3.expects(once()).method("getSupportedEventClasses").withNoArguments().will(returnValue(new Class[]{TestEvent.class}));
        listenerMock3.expects(never()).method("accepts");
        listenerMock3.expects(never()).method("onApplicationEvent");
        
        this.multicaster.addApplicationListener(mockedListener1);
        this.multicaster.addApplicationListener(mockedListener2);
        this.multicaster.addApplicationListener(mockedListener3);
        
        this.multicaster.multicastEvent(event);
        
        Thread.sleep(3000);
    }
    
    public void testRemoveFilteringListener() {
        Mock listenerMock = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedListener = (FilteringApplicationListener) listenerMock.proxy();
        
        PayloadEvent event = new PayloadEvent(this);
        
        listenerMock.expects(once()).method("getSupportedEventClasses").withNoArguments().will(returnValue(new Class[]{ApplicationEvent.class}));
        listenerMock.expects(once()).method("accepts").with(same(event)).will(returnValue(true));
        listenerMock.expects(once()).method("onApplicationEvent").with(same(event));
        
        this.multicaster.addApplicationListener(mockedListener);
        
        this.multicaster.multicastEvent(event);
        
        listenerMock.reset();
        
        listenerMock.expects(never()).method("getSupportedEventClasses");
        listenerMock.expects(never()).method("accepts");
        listenerMock.expects(never()).method("onApplicationEvent");
        
        this.multicaster.removeApplicationListener(mockedListener);
        
        this.multicaster.multicastEvent(event);
    }
    
    public void testRemoveStandardListener() {
        Mock listenerMock = mock(ApplicationListener.class);
        ApplicationListener mockedListener = (ApplicationListener) listenerMock.proxy();
        
        PayloadEvent event = new PayloadEvent(this);
        
        listenerMock.expects(once()).method("onApplicationEvent").with(same(event));
        
        this.multicaster.addApplicationListener(mockedListener);
        
        this.multicaster.multicastEvent(event);
        
        listenerMock.reset();
        
        listenerMock.expects(never()).method("onApplicationEvent");
        
        this.multicaster.removeApplicationListener(mockedListener);
        
        this.multicaster.multicastEvent(event);
    }
    */
}
