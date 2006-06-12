/*
* Copyright 2006 GigaSpaces, Inc.
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
