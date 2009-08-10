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

package org.springmodules.xt.model.event.edp;

import org.jmock.integration.junit3.MockObjectTestCase;

/**
 * @author Sergio Bossa
 */
public abstract class DefaultEDPInvokerTest extends MockObjectTestCase {
    /*
    private DefaultEDPInvoker edpInvoker;
    private Mock edpMock;
    private IDomainService edp;
    
    public DefaultEDPInvokerTest(String testName) {
        super(testName);
        
        this.edpInvoker = new DefaultEDPInvoker();
        
        this.edpMock = mock(IDomainService.class);
        this.edp = (IDomainService) edpMock.proxy();
        
        this.edpInvoker.setInvokedBean(this.edp);
        this.edpInvoker.setInvokedMethodName("onEvent");
    }

    protected void setUp() throws Exception {
        this.edpMock.reset();
    }

    public void testFiredEDPPart1() {
        ApplicationUpdateEvent event = new ApplicationUpdateEvent(this);
        IEmployee employee = new Employee();
        IOffice office = new Office();
        event.setEmployee(employee);
        event.setOffice(office);
        
        this.edpMock.expects(exactly(4)).method("onEvent").with(same(event));
        
        this.edpInvoker.onApplicationEvent(event);
        this.edpInvoker.onApplicationEvent(event);
    }
    
    public void testFiredEDPPart2() {
        ApplicationEmployeeUpdateEvent event1 = new ApplicationEmployeeUpdateEvent(this);
        ApplicationOfficeUpdateEvent event2 = new ApplicationOfficeUpdateEvent(this);
        IEmployee employee = new Employee();
        IOffice office = new Office();
        event1.setEmployee(employee);
        event2.setOffice(office);
        
        this.edpMock.expects(once()).method("onEvent").with(same(event1));
        this.edpMock.expects(once()).method("onEvent").with(same(event2));
        
        this.edpInvoker.onApplicationEvent(event1);
        this.edpInvoker.onApplicationEvent(event2);
    }
    
    public void testNotFiredEDP() {
        TestEvent event = new TestEvent(this);
        
        this.edpMock.expects(never()).method("onEvent");
        
        this.edpInvoker.onApplicationEvent(event);
    }*/
}
