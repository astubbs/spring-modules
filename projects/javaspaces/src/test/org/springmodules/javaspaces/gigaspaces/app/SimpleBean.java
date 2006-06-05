package org.springmodules.javaspaces.gigaspaces.app;



public class SimpleBean extends BaseSimpleBean{
	private String name;
	private int age;
	private Integer testIntegerObject;
	private Long testLongObject;
	private Boolean testBooleanObject = new Boolean(true);
	private Double testDoubleObject;
	private Float testFloatObject;
	public Double getTestDoubleObject()
	{
		return testDoubleObject;
	}

	public void setTestDoubleObject(Double testDoubleObject)
	{
		this.testDoubleObject = testDoubleObject;
	}

	public Float getTestFloatObject()
	{
		return testFloatObject;
	}

	public void setTestFloatObject(Float testFloatObject)
	{
		this.testFloatObject = testFloatObject;
	}
	public void setTestBooleanObject(Boolean testBooleanObject)
	{
		this.testBooleanObject = testBooleanObject;
	}
	public Boolean getTestBooleanObject()
	{
		return testBooleanObject;
	}

	public Integer getTestIntegerObject()
	{
		return testIntegerObject;
	}

	public void setTestIntegerObject(Integer testIntegerObject)
	{
		this.testIntegerObject = testIntegerObject;
	}

	public Long getTestLongObject()
	{
		return testLongObject;
	}

	public void setTestLongObject(Long testLongObject)
	{
		this.testLongObject = testLongObject;
	}

	public SimpleBean() {}

	public SimpleBean(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public int getAge() 			{ return age; }
	public void setAge(int age) 	{ this.age = age; }

	private String getName() 			{ return name; }
	private void setName(String name) 	{ this.name = name; }

	public boolean equals(Object other) {
		if(other == null || !(other instanceof SimpleBean))
			return false;
		else {
			SimpleBean otherBean = (SimpleBean)other;
			return ((otherBean.name != null && otherBean.name.equals(name) || otherBean.name == name )) && (otherBean.age == age)
			&& (otherBean.age == age)
			&& ((otherBean.getTeststring() != null && otherBean.getTeststring().equals(getTeststring()) || otherBean.getTeststring() ==getTeststring()));
		}
	}


}
