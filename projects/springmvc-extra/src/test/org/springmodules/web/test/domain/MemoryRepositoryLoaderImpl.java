package org.springmodules.web.test.domain;

/**
 *
 * @author Sergio Bossa
 */
public class MemoryRepositoryLoaderImpl implements MemoryRepositoryLoader {
    
   public void loadInto(MemoryRepository store) {
       Office o1 = new Office();
       Office o2 = new Office();
       Employee e1 = new Employee();
       Employee e2 = new Employee();
       Employee e3 = new Employee();
       Employee e4 = new Employee();
       
       o1.setOfficeId("1");
       o1.setName("Office 1");
       o2.setOfficeId("2");
       o2.setName("Office 2");
       
       e1.setMatriculationCode("1");
       e1.setFirstname("George");
       e1.setSurname("Orwell");
       e2.setMatriculationCode("2");
       e2.setFirstname("T.S.");
       e2.setSurname("Eliot");
       e3.setMatriculationCode("3");
       e3.setFirstname("Thom");
       e3.setSurname("Yorke");
       e4.setMatriculationCode("4");
       e4.setFirstname("Jimmy");
       e4.setSurname("Page");
       
       o1.addEmployee(e1);
       o1.addEmployee(e3);
       o2.addEmployee(e2);
       o2.addEmployee(e4);
       
       store.addOffice(o1);
       store.addOffice(o2);
       
       store.addEmployee(e1);
       store.addEmployee(e2);
       store.addEmployee(e3);
       store.addEmployee(e4);
   } 
}
