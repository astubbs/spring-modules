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

package org.springmodules.xt.test.event;

import org.springframework.context.ApplicationEvent;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.domain.IEmployeeUpdateEvent;
import org.springmodules.xt.test.domain.IOffice;
import org.springmodules.xt.test.domain.IOfficeUpdateEvent;

/**
 * @author Sergio Bossa
 */
public class ApplicationUpdateEvent extends ApplicationEvent implements IEmployeeUpdateEvent, IOfficeUpdateEvent {

    private IEmployee employee;
    private IOffice office;
    
    public ApplicationUpdateEvent(Object source) {
        super(source);
    }

    public IEmployee getEmployee() {
        return this.employee;
    }

    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

    public IOffice getOffice() {
        return this.office;
    }

    public void setOffice(IOffice office) {
        this.office = office;
    }
}
