package org.springmodules.javaspaces.gigaspaces.app;

import org.springmodules.javaspaces.gigaspaces.GigaSpacesDaoSupport;

public class SimpleDao extends GigaSpacesDaoSupport implements ISimpleDao {

	public void writeSimple(SimpleBean bean) throws Exception  {
		getGigaSpaceTemplate().write(bean, -1);
	}

	public SimpleBean takeIfExists(SimpleBean template) {
		return (SimpleBean)getGigaSpaceTemplate().takeIfExists(template, -1);
	}

	public SimpleBean writeAndTake(SimpleBean bean) throws Exception {
		writeSimple(bean);
		return takeIfExists(bean);
	}
}
