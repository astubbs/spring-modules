package org.springmodules.commons.collections.functors;

import java.util.Map;

import org.apache.commons.collections.Closure;

/**
 * @author Steven Devijver 
 * @since 29-04-2005
 */
public class ApplyPriorityClosure implements Closure {

	private String priority = null;
	
	public ApplyPriorityClosure() { super(); }
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	private String getPriority() {
		return this.priority;
	}
	
	public void execute(Object target) {
		Map order = (Map)target;
		order.put("priority", getPriority());
	}

}
