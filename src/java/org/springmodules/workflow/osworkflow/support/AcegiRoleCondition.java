
package org.springmodules.workflow.osworkflow.support;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.context.security.SecureContext;

/**
 * @author robh
 */
public class AcegiRoleCondition implements Condition {

	private static final String ROLE_KEY = "role";

	public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		if (!args.containsKey(ROLE_KEY)) {
			throw new WorkflowException("Condition [" + getClass().getName() + "] expects argument [" + ROLE_KEY + "].");
		}

		Object role = args.get(ROLE_KEY);

		if (role != null) {
			SecureContext secureContext = (SecureContext) ContextHolder.getContext();
			GrantedAuthority[] authorities = secureContext.getAuthentication().getAuthorities();

			for (int i = 0; i < authorities.length; i++) {
				GrantedAuthority authority = authorities[i];
				if (role.equals(authority.getAuthority())) {
					return true;
				}
			}

			return false;
		}
		else {
			return true;
		}
	}
}
