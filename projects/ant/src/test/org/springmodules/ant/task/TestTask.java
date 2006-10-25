/**
 * 
 */
package org.springmodules.ant.task;

import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class TestTask extends Task {
	Map properties;

	/**
	 * Injected during test.
	 * @param properties
	 */
	public void setProperties(Map properties) {
		this.properties = properties;
	}

	/* 
	 * Just log something.
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		if (properties==null) log("Null properties");
		else log(properties.toString());
	}

}