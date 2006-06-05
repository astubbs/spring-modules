/*
 * Copyright 2006 GigaSpaces Technologies. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
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
