
package org.springmodules.workflow.osworkflow.support;

import java.util.Map;
import java.util.HashMap;

import net.sf.acegisecurity.context.security.SecureContext;
import net.sf.acegisecurity.context.security.SecureContextImpl;
import net.sf.acegisecurity.context.ContextInvalidException;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.GrantedAuthorityImpl;
import net.sf.acegisecurity.providers.UsernamePasswordAuthenticationToken;
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

		SecureContext context = new SecureContextImpl();
		context.setAuthentication(authentication);

		ContextHolder.setContext(context);

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
