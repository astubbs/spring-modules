/*
 * Copyright 2006 GigaSpaces Technologies. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */
package org.springmodules.javaspaces.gigaspaces;

import java.util.logging.Logger;

import com.j_spaces.core.Constants;

/**
 * Title:
 * Description:  This class is a tracer for the gigaspaces project of spring <p>

 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 */
public final class SpringTracer {
	public static Logger getLogger()
	{
		return logger;
	}
	private static final String GIGASPACES_SPRING_INTEGRATION = "org.springmodules.javaspaces.gigaspaces.SpringTracer";
	private static final Logger logger = Logger.getLogger(GIGASPACES_SPRING_INTEGRATION);

}
