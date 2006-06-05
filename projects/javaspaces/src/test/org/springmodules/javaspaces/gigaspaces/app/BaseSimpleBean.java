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
		if(other == null || !(other instanceof SimpleBean))
			return false;
		else {
			SimpleBean otherBean = (SimpleBean)other;
			return (otherBean.getTeststring().equals(teststring));
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
