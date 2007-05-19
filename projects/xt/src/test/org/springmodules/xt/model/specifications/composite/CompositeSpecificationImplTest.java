/*
 * Copyright 2006 the original author or authors.
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

package org.springmodules.xt.model.specifications.composite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springmodules.xt.model.notification.Message;
import org.springmodules.xt.model.notification.MessageImpl;
import org.springmodules.xt.model.notification.Notification;
import org.springmodules.xt.model.notification.NotificationImpl;
import org.springmodules.xt.test.domain.AvailableOfficeSpecification;
import org.springmodules.xt.test.domain.BaseSpecification;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.domain.FullOfficeSpecification;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.domain.IOffice;
import org.springmodules.xt.test.domain.Office;
import org.springmodules.xt.test.domain.OfficeIdSpecification;

/**
 * @author Sergio Bossa
 */
public class CompositeSpecificationImplTest extends TestCase {
    
    private CompositeSpecificationImpl<BaseSpecification, IOffice> compositeSpecification;
    
    public CompositeSpecificationImplTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.compositeSpecification = new CompositeSpecificationImpl<BaseSpecification, IOffice>(BaseSpecification.class,  "isSatisfiedBy");
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of single specification composition.
     */
    public void testCompose() {
        OfficeIdSpecification spec = new OfficeIdSpecification();
        IOffice office1 = new Office();
        
        office1.setOfficeId("o1");
        assertTrue(this.compositeSpecification.compose(spec).evaluate(office1));
        
        office1.setOfficeId("aaa");
        assertFalse(this.compositeSpecification.compose(spec).evaluate(office1));
    }

    /**
     * Test of "and" logical operator.
     */
    public void testAnd() {
        OfficeIdSpecification spec1 = new OfficeIdSpecification();
        AvailableOfficeSpecification spec2 = new AvailableOfficeSpecification();
        FullOfficeSpecification spec3 = new FullOfficeSpecification();
        
        IOffice office1 = new Office();
        IEmployee emp1 = new Employee();
        
        office1.setOfficeId("o1");
        emp1.setMatriculationCode("1");
        office1.addEmployee(emp1);
        
        assertTrue(this.compositeSpecification.compose(spec1).and(spec2).andNot(spec3).evaluate(office1));
        
        // Test also composition with another CompositeSpecificationImpl ...
        CompositeSpecificationImpl<BaseSpecification, IOffice> composite2 = new CompositeSpecificationImpl<BaseSpecification, IOffice>(BaseSpecification.class, "isSatisfiedBy");
        composite2.compose(spec2).andNot(spec3);
        assertTrue(this.compositeSpecification.compose(spec1).and(composite2).evaluate(office1));
    }

    /**
     * Test of "or" logical operator.
     */
    public void testOr() {
        FullOfficeSpecification spec1 = new FullOfficeSpecification();
        AvailableOfficeSpecification spec2 = new AvailableOfficeSpecification();
        
        IOffice office1 = new Office();
        IEmployee emp1 = new Employee();
        
        office1.setOfficeId("o1");
        emp1.setMatriculationCode("1");
        office1.addEmployee(emp1);
        
        assertTrue(this.compositeSpecification.compose(spec1).or(spec2).evaluate(office1));
        
        // Test also composition with another CompositeSpecificationImpl ...
        CompositeSpecificationImpl<BaseSpecification, IOffice> composite2 = new CompositeSpecificationImpl<BaseSpecification, IOffice>(BaseSpecification.class, "isSatisfiedBy");
        composite2.compose(spec2);
        assertTrue(this.compositeSpecification.compose(spec1).or(composite2).evaluate(office1));
    }
    
    /**
     * Test of "and not" logical operator.
     */
    public void testAndNot() {
        OfficeIdSpecification spec1 = new OfficeIdSpecification();
        FullOfficeSpecification spec2 = new FullOfficeSpecification();
        AvailableOfficeSpecification spec3 = new AvailableOfficeSpecification();
        
        IOffice office1 = new Office();
        IEmployee emp1 = new Employee();
        
        office1.setOfficeId("o1");
        emp1.setMatriculationCode("1");
        office1.addEmployee(emp1);
        
        assertTrue(this.compositeSpecification.compose(spec1).andNot(spec2).and(spec3).evaluate(office1));
        
        // Test also composition with another CompositeSpecificationImpl ...
        CompositeSpecificationImpl<BaseSpecification, IOffice> composite2 = new CompositeSpecificationImpl<BaseSpecification, IOffice>(BaseSpecification.class, "isSatisfiedBy");
        composite2.compose(spec2);
        assertTrue(this.compositeSpecification.compose(spec1).andNot(composite2).evaluate(office1));
    }

    /**
     * Test of "or not" logical operator.
     */
    public void testOrNot() {
        FullOfficeSpecification spec1 = new FullOfficeSpecification();
        
        IOffice office1 = new Office();
        IEmployee emp1 = new Employee();
        
        office1.setOfficeId("o1");
        emp1.setMatriculationCode("1");
        office1.addEmployee(emp1);
        
        assertTrue(this.compositeSpecification.compose(spec1).orNot(spec1).evaluate(office1));
        
        // Test also composition with another CompositeSpecificationImpl ...
        CompositeSpecificationImpl<BaseSpecification, IOffice> composite2 = new CompositeSpecificationImpl<BaseSpecification, IOffice>(BaseSpecification.class, "isSatisfiedBy");
        composite2.compose(spec1);
        assertTrue(this.compositeSpecification.compose(spec1).orNot(composite2).evaluate(office1));
    }
    
    /**
     * Test error notifications.
     */
    public void testWithErrorMessage() {
        OfficeIdSpecification spec1 = new OfficeIdSpecification();
        AvailableOfficeSpecification spec2 = new AvailableOfficeSpecification();
        
        IOffice office1 = new Office();
        IEmployee emp1 = new Employee();
        
        office1.setOfficeId("aaa");
        emp1.setMatriculationCode("1");
        office1.addEmployee(emp1);
        
        Notification notification = new NotificationImpl();
        
        assertFalse(this.compositeSpecification.compose(spec1).withMessage(new MessageImpl("wrong.office.id", Message.Type.ERROR, "Wrong Office Id"), false)
                                                                       .and(spec2).withMessage(new MessageImpl("office.unavailable", Message.Type.ERROR, "Office Not Available"), false)
                                                                       .evaluate(office1, notification));
                                                    
        assertTrue(notification.hasMessages(Message.Type.ERROR));
        assertFalse(notification.hasMessages(Message.Type.WARNING));
        assertFalse(notification.hasMessages(Message.Type.INFO));
        assertTrue(notification.getMessages(Message.Type.ERROR).length == 1);
        assertEquals("wrong.office.id", notification.getMessages(Message.Type.ERROR)[0].getCode());
        assertEquals("Wrong Office Id",notification.getMessages(Message.Type.ERROR)[0].getDefaultMessage());
    }
    
    /**
     * Test warning notifications.
     */
    public void testWithWarningMessage() {
        OfficeIdSpecification spec1 = new OfficeIdSpecification();
        AvailableOfficeSpecification spec2 = new AvailableOfficeSpecification();
        
        IOffice office1 = new Office();
        IEmployee emp1 = new Employee();
        
        office1.setOfficeId("aaa");
        emp1.setMatriculationCode("1");
        office1.addEmployee(emp1);
        
        Notification notification = new NotificationImpl();
        
        assertFalse(this.compositeSpecification.compose(spec1).withMessage(new MessageImpl("wrong.office.id", Message.Type.WARNING, "Wrong Office Id"), false)
                                                                       .and(spec2).withMessage(new MessageImpl("office.unavailable", Message.Type.WARNING, "Office Not Available"), false)
                                                                       .evaluate(office1, notification));
                                                    
        assertFalse(notification.hasMessages(Message.Type.ERROR));
        assertTrue(notification.hasMessages(Message.Type.WARNING));
        assertFalse(notification.hasMessages(Message.Type.INFO));
        assertTrue(notification.getMessages(Message.Type.WARNING).length == 1);
        assertEquals("wrong.office.id", notification.getMessages(Message.Type.WARNING)[0].getCode());
        assertEquals("Wrong Office Id", notification.getMessages(Message.Type.WARNING)[0].getDefaultMessage());
    }
    
    /**
     * Test info notifications.
     */
    public void testWithInfoMessage() {
        OfficeIdSpecification spec1 = new OfficeIdSpecification();
        AvailableOfficeSpecification spec2 = new AvailableOfficeSpecification();
        
        IOffice office1 = new Office();
        IEmployee emp1 = new Employee();
        
        office1.setOfficeId("o1");
        emp1.setMatriculationCode("1");
        office1.addEmployee(emp1);
        
        Notification notification = new NotificationImpl();
        
        assertTrue(this.compositeSpecification.compose(spec1).withMessage(new MessageImpl("good.office.id", Message.Type.INFO, "Good Office Id"), true)
                                                                       .and(spec2).withMessage(new MessageImpl("office.available", Message.Type.INFO, "Office Available"), true)
                                                                       .evaluate(office1, notification));
                                                    
        assertFalse(notification.hasMessages(Message.Type.ERROR));
        assertFalse(notification.hasMessages(Message.Type.WARNING));
        assertTrue(notification.hasMessages(Message.Type.INFO));
        assertTrue(notification.getMessages(Message.Type.INFO).length == 2);
    }
}
