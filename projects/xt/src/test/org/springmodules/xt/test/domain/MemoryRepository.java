package org.springmodules.xt.test.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * In memory store.
 *
 * @author Sergio Bossa
 */
public class MemoryRepository {
    
    private Map<String, IEmployee> employeesMap = new HashMap<String, IEmployee>();
    private Map<String, IOffice> officesMap = new HashMap<String, IOffice>();
    private MemoryRepositoryLoader loader;
    
    public MemoryRepository(MemoryRepositoryLoader loader) {
        this.loader = loader;
        this.init();
    }
    
    public void init() {
        this.loader.loadInto(this);
    }
    
    public void addEmployee(IEmployee e) {
        this.employeesMap.put(e.getMatriculationCode(), e);
    }
    
    public void addOffice(IOffice o) {
        this.officesMap.put(o.getOfficeId(), o);
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
}
