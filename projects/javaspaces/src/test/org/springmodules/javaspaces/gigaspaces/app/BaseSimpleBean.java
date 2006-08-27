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


public class BaseSimpleBean {
	private String teststring = null;
	private int testint = 1;
	private long testlong = 1;
	private float testfloat = 1;
	private boolean testboolean = false;
	private double testdouble = 1;

	public BaseSimpleBean() {}

	public BaseSimpleBean(String test) {
		this.teststring = test;
	}

	public boolean equals(Object other) {
		if(other == null || !(other instanceof BaseSimpleBean))
			return false;
		else {
			BaseSimpleBean otherBean = (BaseSimpleBean)other;
			return 	(otherBean.teststring != null && otherBean.teststring.equals(teststring) || otherBean.teststring == teststring ) &&
			(otherBean.testboolean == testboolean ) &&
			(otherBean.testdouble == testdouble ) &&
			(otherBean.testfloat == testfloat )&&
			(otherBean.testint == testint) &&
			(otherBean.testlong == testlong );

		}
	}

	public String getTeststring()
	{
		return teststring;
	}

	public void setTeststring(String test)
	{
		this.teststring = test;
	}

	public int getTestint()
	{
		return testint;
	}

	public void setTestint(int testint)
	{
		this.testint = testint;
	}

	public boolean isTestboolean()
	{
		return testboolean;
	}

	public void setTestboolean(boolean testboolean)
	{
		this.testboolean = testboolean;
	}

	public float getTestfloat()
	{
		return testfloat;
	}

	public void setTestfloat(float testfloat)
	{
		this.testfloat = testfloat;
	}

	public long getTestlong()
	{
		return testlong;
	}

	public void setTestlong(long testlong)
	{
		this.testlong = testlong;
	}

	public double getTestdouble()
	{
		return testdouble;
	}

	public void setTestdouble(double testdouble)
	{
		this.testdouble = testdouble;
	}
}
