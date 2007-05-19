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

package org.springmodules.xt.model.specifications.adapter;

import junit.framework.*;
import org.springmodules.xt.model.specifications.composite.CompositeSpecification;
import org.springmodules.xt.model.specifications.composite.CompositeSpecificationImpl;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.domain.Office;
import org.springmodules.xt.test.domain.AvailableOfficeSpecification;
import org.springmodules.xt.test.domain.BaseSpecification;
import org.springmodules.xt.test.domain.IOffice;

/**
 * @author Sergio Bossa
 */
public class PredicateCompositeAdapterTest extends TestCase {
    
    public PredicateCompositeAdapterTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of evaluate method, of class org.springmodules.xt.model.specifications.adapter.PredicateCompositeAdapter.
     */
    public void testEvaluate() {
        AvailableOfficeSpecification spec = new AvailableOfficeSpecification();
        CompositeSpecification<BaseSpecification, IOffice> composite = new CompositeSpecificationImpl<BaseSpecification, IOffice>(BaseSpecification.class, "isSatisfiedBy");
        PredicateCompositeAdapter adapter = new PredicateCompositeAdapter(composite);
        
        IOffice office1 = new Office();
        IEmployee emp1 = new Employee();
        
        office1.setOfficeId("o1");
        emp1.setMatriculationCode("1");
        office1.addEmployee(emp1);
        
        composite.compose(spec);
        assertTrue(composite.evaluate(office1));
        assertTrue(adapter.evaluate(office1));
    }
}
