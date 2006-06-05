/*
 * Copyright 2005 GigaSpaces Technologies Ltd. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */

package org.springmodules.javaspaces.gigaspaces.app;

/**
 * Title:
 * Description:   <p>

 * Copyright:    Copyright 2006 GigaSpaces Technologies Ltd. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 */
public class Composite
{
	private String comstring;
	private int comint;
	public int getComint()
	{
		return comint;
	}
	public void setComint(int comint)
	{
		this.comint = comint;
	}
	public String getComstring()
	{
		return comstring;
	}
	public void setComstring(String comstring)
	{
		this.comstring = comstring;
	}

	public boolean equals(Object other) {
		if(other == null || !(other instanceof Composite))
			return false;
		else {
			Composite composite = (Composite)other;
			return ((composite.comstring != null && composite.comstring.equals(comstring) || composite.comstring == comstring )) && (composite.comint == comint);
		}
	}
}
