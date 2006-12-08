package org.springmodules.xt.test.domain;

import org.springmodules.xt.model.introductor.annotation.MapToTargetField;
import org.springmodules.xt.model.introductor.annotation.OverrideTarget;

/**
 * Employee form view, with annotations.
 *
 * @author Sergio Bossa
 */
public interface EmployeeView4 extends EmployeeView {
    
    @MapToTargetField
    String getNickname();
    
    @MapToTargetField
    void setNickname(String nickname);
}
