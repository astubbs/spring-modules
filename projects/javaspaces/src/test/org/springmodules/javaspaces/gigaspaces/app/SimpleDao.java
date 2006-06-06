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
