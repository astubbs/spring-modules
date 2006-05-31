/**
 * Created on Mar 12, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.jini.entry;

import net.jini.core.entry.Entry;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 * @author Costin Leau
 *
 */
public class EntryMixinAdvisor extends DefaultIntroductionAdvisor {
	
	public EntryMixinAdvisor() {
		// TODO: since the mixin is stateless, we can use a shared(static) instance
		super(new EntryMixin(), Entry.class);
	}
}
