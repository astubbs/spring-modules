package org.springmodules.validation.functions;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;

/**
 * A function that accepts a one string argument that indicates a security role. This function
 * returns a <code>true</code> if the current user is in the passed in role, and <code>false</code>
 * otherwise.
 * <br/><br/>
 * This method uses Acegi's <code>SecurityContextHolder.getContext().getAuthentication()</code> to
 * get the current user.
 *<br/><br/>
 * This function may be used to apply different validation rules based on the logged in user roles.
 *
 * @author Uri Boness
 * @since May 25, 2006
 */
public class InRoleFunction extends AbstractFunction {

    public InRoleFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
        definedExactNumberOfArguments(1);
    }

    protected Object doGetResult(Object target) {

        Object role = getArguments()[0].getResult(target);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Boolean.FALSE;
        }

        GrantedAuthority[] authorities = authentication.getAuthorities();

        for (int i=0; i<authorities.length; i++) {
            if (authorities[i].getAuthority().equals(role)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

}
