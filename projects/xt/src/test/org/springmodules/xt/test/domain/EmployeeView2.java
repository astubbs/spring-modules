package org.springmodules.xt.test.domain;

/**
 * Employee form view, with nickname setter/getter.
 *
 * @author Sergio Bossa
 */
public interface EmployeeView2 extends EmployeeView {
    
    String getNickname();
    
    void setNickname(String nickname);
}
