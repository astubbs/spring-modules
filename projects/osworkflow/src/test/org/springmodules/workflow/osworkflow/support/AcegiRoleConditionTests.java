
package org.springmodules.workflow.osworkflow.support;

import java.util.Map;
import java.util.HashMap;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.memory.MemoryPropertySet;
import com.opensymphony.workflow.WorkflowException;
import junit.framework.TestCase;

/**
 * @author robh
 */
public class AcegiRoleConditionTests extends TestCase {

	public void setUp() {
		GrantedAuthority[] roles = new GrantedAuthority[]{new GrantedAuthorityImpl("manager"), new GrantedAuthorityImpl("vp")};

		Authentication authentication = new UsernamePasswordAuthenticationToken(new Object(), new Object(), roles);

		SecurityContext context = new SecurityContextImpl();
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);

	}
	public void testPassesCondition() throws Exception {
    AcegiRoleCondition condition = new AcegiRoleCondition();

		Map transientVars = new HashMap();
		Map args = new HashMap();
		PropertySet ps = new MemoryPropertySet();

		args.put(AcegiRoleCondition.ROLE, "manager");
		assertTrue("Should be in manager role", condition.passesCondition(transientVars, args, ps));

		args.put(AcegiRoleCondition.ROLE, "vp");
		assertTrue("Should be in vp role", condition.passesCondition(transientVars, args, ps));

		args.put(AcegiRoleCondition.ROLE, "pleb");
		assertFalse("Should not be in pleb role", condition.passesCondition(transientVars, args, ps));
	}

	public void testWithRoleArg() throws Exception {
		AcegiRoleCondition condition = new AcegiRoleCondition();

		try {
      condition.passesCondition(new HashMap(), new HashMap(), new MemoryPropertySet());
			fail("Should not be able to test with ROLE attribute");
		} catch(WorkflowException ex) {
			// success
		}
	}
}
