/*
* Copyright 2002-2005 the original author or authors.
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

package org.springmodules.workflow.osworkflow.support;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContext;

/**
 * @author robh
 */
public class AcegiRoleCondition implements Condition {

	public static final String ROLE = "role";

	public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		Object role = args.get(ROLE);

		if (role == null) {
			throw new WorkflowException("Condition [" + getClass().getName() + "] expects argument [" + ROLE + "].");
		}

		SecurityContext securityContext = (SecurityContext) SecurityContextHolder.getContext();
		GrantedAuthority[] authorities = securityContext.getAuthentication().getAuthorities();

		for (int i = 0; i < authorities.length; i++) {
			GrantedAuthority authority = authorities[i];
			if (role.equals(authority.getAuthority())) {
				return true;
			}
		}

		return false;

	}
}
