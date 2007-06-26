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

package org.springmodules.xt.model.event;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springmodules.xt.model.event.support.PayloadEvent;
import org.springmodules.xt.test.event.TestEvent;

/**
 * @author Sergio Bossa
 */
public class FilteringApplicationEventMulticasterTest extends MockObjectTestCase {
   
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
    
    /*public void testPublishAndRouteToMultipleConsumersWithException() {
        Mock consumerMock1 = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedConsumer1 = (FilteringApplicationListener) consumerMock1.proxy();
        Mock consumerMock2 = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedConsumer2 = (FilteringApplicationListener) consumerMock2.proxy();
        
        PayloadEvent event1 = new PayloadEvent(new EventType("test1"), this);
        PayloadEvent event2 = new PayloadEvent(new EventType("test2"), this);
        
        consumerMock1.expects(once()).method("consume").with(same(event1)).will(throwException(new RuntimeException("Test Exception")));
        consumerMock2.expects(once()).method("consume").with(same(event2));
        
        Mock filterMock1 = mock(EventFilter.class);
        EventFilter mockedFilter1 = (EventFilter) filterMock1.proxy();
        Mock filterMock2 = mock(EventFilter.class);
        EventFilter mockedFilter2 = (EventFilter) filterMock2.proxy();
        
        filterMock1.expects(once()).method("accepts").with(same(event1)).will(returnValue(true));
        filterMock1.expects(once()).method("accepts").with(same(event2)).will(returnValue(false));
        filterMock2.expects(once()).method("accepts").with(same(event1)).will(returnValue(false));
        filterMock2.expects(once()).method("accepts").with(same(event2)).will(returnValue(true));
        
        this.multicaster.registerConsumer(mockedConsumer1, mockedFilter1);
        this.multicaster.registerConsumer(mockedConsumer2, mockedFilter2);
        
        this.multicaster.publish(event1);
        this.multicaster.publish(event2);
    }
    
    public void testPublishAndRouteToConsumerWithMultipleFilters() {
        Mock consumerMock1 = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedConsumer1 = (FilteringApplicationListener) consumerMock1.proxy();
        
        PayloadEvent event1 = new PayloadEvent(new EventType("test1"), this);
        
        consumerMock1.expects(once()).method("consume").with(same(event1));
        
        Mock filterMock1 = mock(EventFilter.class);
        EventFilter mockedFilter1 = (EventFilter) filterMock1.proxy();
        Mock filterMock2 = mock(EventFilter.class);
        EventFilter mockedFilter2 = (EventFilter) filterMock2.proxy();
        
        filterMock1.expects(once()).method("accepts").with(same(event1)).will(returnValue(true));
        filterMock2.expects(atMostOnce()).method("accepts").with(same(event1)).will(returnValue(false));
        
        this.multicaster.registerConsumer(mockedConsumer1, mockedFilter1);
        this.multicaster.registerConsumer(mockedConsumer1, mockedFilter2);
        
        this.multicaster.publish(event1);
    }
    
    public void testRegisterListener() {
        Mock consumerMock = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedConsumer = (FilteringApplicationListener) consumerMock.proxy();
        
        Mock filterMock = mock(EventFilter.class);
        EventFilter mockedFilter = (EventFilter) filterMock.proxy();
        
        this.multicaster.registerConsumer(mockedConsumer, mockedFilter);
        assertEquals(mockedConsumer, this.multicaster.getConsumers().get(0));
    }

    public void testRemoveListener() {
        Mock consumerMock = mock(FilteringApplicationListener.class);
        FilteringApplicationListener mockedConsumer = (FilteringApplicationListener) consumerMock.proxy();
        
        Mock filterMock = mock(EventFilter.class);
        EventFilter mockedFilter = (EventFilter) filterMock.proxy();
        
        this.multicaster.registerConsumer(mockedConsumer, mockedFilter);
        assertEquals(mockedConsumer, this.multicaster.getConsumers().get(0));
        
        this.multicaster.removeConsumer(mockedConsumer);
        assertEquals(0, this.multicaster.getConsumers().size());
    }*/
}
