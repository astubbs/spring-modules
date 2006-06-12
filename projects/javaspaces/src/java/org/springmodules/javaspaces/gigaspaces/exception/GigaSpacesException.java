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
package org.springmodules.javaspaces.gigaspaces.exception;

/**
 * <p>
 * This class is a GigaSpacesException which meant to be used if something went wrong
 * </p>
 *
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 */
public class GigaSpacesException
		extends RuntimeException
{

	public GigaSpacesException()
	{
		super();
	}

	public GigaSpacesException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public GigaSpacesException(String message)
	{
		super(message);
	}

	public GigaSpacesException(Throwable cause)
	{
		super(cause);
	}

}
