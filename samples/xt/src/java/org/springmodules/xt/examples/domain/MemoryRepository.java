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
    
    public static final int MAX_EMPLOYEES = 3;
    
    private Map<String, IEmployee> employeesMap = new HashMap<String, IEmployee>();
    private Map<String, IOffice> officesMap = new HashMap<String, IOffice>();
    private MemoryRepositoryLoader loader;
    
    private CompositeSpecification<BaseSpecification, IOffice> officeSpecification = 
            new CompositeSpecificationImpl(BaseSpecification.class, "isSatisfiedBy");
    
    public MemoryRepository() {
        OfficeIdSpecification idSpecification = new OfficeIdSpecification();
        FullOfficeSpecification fullOfficeSpecification = new FullOfficeSpecification(MAX_EMPLOYEES);
        Message wrongIdMessage = new MessageImpl(OfficeErrorCodes.WRONG_ID, Message.Type.ERROR, "officeId" ,"Wrong office id");
        Message fullOfficeMessage = new MessageImpl(OfficeErrorCodes.FULL, Message.Type.ERROR, "employees", "Too many employees");
        
        this.officeSpecification.compose(idSpecification).withMessage(wrongIdMessage, false)
                                           .andNot(fullOfficeSpecification).withMessage(fullOfficeMessage, true);
    }
    
    public void init() {
        this.loader.loadInto(this);
    }
    
    public void addEmployee(IEmployee e) {
        this.employeesMap.put(e.getMatriculationCode(), e);
    }
    
    public void addOffice(IOffice o) {
        Notification n = new NotificationImpl();
        if (this.officeSpecification.evaluate(o, n) == false) {
            BusinessException ex = DomainUtils.notificationErrorsToBusinessException(n);
            throw ex;
        }
        else {
            this.officesMap.put(o.getOfficeId(), o);
        }
    }
    
    public IEmployee getEmployee(String matriculationCode) {
        return (IEmployee) employeesMap.get(matriculationCode);
    }
    
    public IOffice getOffice(String officeId) {
        return (IOffice) officesMap.get(officeId);
    }
    
    public Collection<IOffice> getOffices() {
        List<IOffice> offices = new LinkedList<IOffice>(officesMap.values());
        Collections.sort(offices, new Comparator() {
            public int compare(Object o1, Object o2) {
                IOffice office1 = (IOffice) o1;
                IOffice office2 = (IOffice) o2;
                return office1.getName().compareTo(office2.getName());
            }
        });
        return offices;
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
        return result;
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
        return result;
    }

    public void setLoader(MemoryRepositoryLoader loader) {
        this.loader = loader;
    }
}
