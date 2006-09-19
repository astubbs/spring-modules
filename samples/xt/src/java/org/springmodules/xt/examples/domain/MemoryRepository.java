package org.springmodules.xt.examples.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springmodules.xt.examples.domain.codes.OfficeErrorCodes;
import org.springmodules.xt.examples.domain.util.DomainUtils;
import org.springmodules.xt.model.notification.Message;
import org.springmodules.xt.model.notification.MessageImpl;
import org.springmodules.xt.model.notification.Notification;
import org.springmodules.xt.model.notification.NotificationImpl;
import org.springmodules.xt.model.specifications.composite.CompositeSpecification;
import org.springmodules.xt.model.specifications.composite.CompositeSpecificationImpl;

/**
 * In memory store.
 *
 * @author Sergio Bossa
 */
public class MemoryRepository {
    
    private MemoryRepositoryLoader loader;
    
    private Map<String, IEmployee> employeesMap = new HashMap<String, IEmployee>();
    private Map<String, IOffice> officesMap = new HashMap<String, IOffice>();
    
    private CompositeSpecification<BaseSpecification, IOffice> officeSpecification = new CompositeSpecificationImpl(BaseSpecification.class, "isSatisfiedBy");
    
    public MemoryRepository() {
        this.setRules();
    }
    
    public void init() {
        this.loader.loadInto(this);
    }
    
    public void addEmployee(IEmployee e) {
        this.internalAddEmployee(e);
        for (IOffice o : this.officesMap.values()) {
            if (o.getEmployees().contains(e)) {
                o.addEmployee(e);
            }
        }
    }
    
    public void addOffice(IOffice o) {
        this.internalAddOffice(o);
        for (IEmployee emp : o.getEmployees()) {
            this.internalAddEmployee(emp);
        }
    }
    
    public IEmployee getEmployee(String matriculationCode) {
        IEmployee result = employeesMap.get(matriculationCode);
        if (result != null) {
            return result.copy();
        }
        else {
            return null;
        }
    }
    
    public IOffice getOffice(String officeId) {
        IOffice result = officesMap.get(officeId);
        if (result != null) {
            return result.copy();
        }
        else {
            return null;
        }
    }
    
    public Collection<IOffice> getOffices() {
        List<IOffice> result = new LinkedList<IOffice>(officesMap.values());
        Collections.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                IOffice office1 = (IOffice) o1;
                IOffice office2 = (IOffice) o2;
                return office1.getName().compareTo(office2.getName());
            }
        });
        return this.copyOfficeList(result);
    }
    
    public Collection<IEmployee> getEmployeesByOffice(IOffice o) {
        List<IEmployee> result = new LinkedList<IEmployee>();
        for (IEmployee e : employeesMap.values()) {
            if (o.getEmployees().contains(e)) {
                result.add(e);
            }
        }
        Collections.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                IEmployee emp1 = (IEmployee) o1;
                IEmployee emp2 = (IEmployee) o2;
                return emp1.getSurname().compareTo(emp2.getSurname());
            }
        });
        return this.copyEmployeeList(result);
    }
    
    public Collection<IEmployee> getEmployees() {
        Collection<IEmployee> employees = this.employeesMap.values();
        List<IEmployee> result = new LinkedList<IEmployee>(employees);
        Collections.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                IEmployee emp1 = (IEmployee) o1;
                IEmployee emp2 = (IEmployee) o2;
                return emp1.getSurname().compareTo(emp2.getSurname());
            }
        });
        return this.copyEmployeeList(result);
    }

    public void setLoader(MemoryRepositoryLoader loader) {
        this.loader = loader;
    }
    
    private void setRules() {
        OfficeIdSpecification idSpecification = new OfficeIdSpecification();
        FullOfficeSpecification fullOfficeSpecification = new FullOfficeSpecification();
        Message wrongIdMessage = new MessageImpl(OfficeErrorCodes.WRONG_ID, Message.Type.ERROR, "officeId" ,"Wrong office id");
        Message fullOfficeMessage = new MessageImpl(OfficeErrorCodes.FULL, Message.Type.ERROR, "employees", "Too many employees");
        
        this.officeSpecification.compose(idSpecification).withMessage(wrongIdMessage, false)
                                           .andNot(fullOfficeSpecification).withMessage(fullOfficeMessage, true);
    }
    
    private Collection<IEmployee> copyEmployeeList(Collection<IEmployee> list) {
        Collection<IEmployee> copy = new LinkedList<IEmployee>();
        for (IEmployee emp : list) {
            copy.add(emp.copy());
        }
        return copy;
    }
    
    private Collection<IOffice> copyOfficeList(Collection<IOffice> list) {
        Collection<IOffice> copy = new LinkedList<IOffice>();
        for (IOffice office : list) {
            copy.add(office.copy());
        }
        return copy;
    }
    
    private void internalAddOffice(IOffice o) {
        Notification n = new NotificationImpl();
        if (this.officeSpecification.evaluate(o, n) == false) {
            BusinessException ex = DomainUtils.notificationErrorsToBusinessException(n);
            throw ex;
        }
        else {
            this.officesMap.put(o.getOfficeId(), o);
        }
    }
    
    private void internalAddEmployee(IEmployee e) {
        this.employeesMap.put(e.getMatriculationCode(), e);
    }
}
