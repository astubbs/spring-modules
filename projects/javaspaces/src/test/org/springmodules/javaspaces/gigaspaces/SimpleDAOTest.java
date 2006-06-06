/*
 * Copyright 2005 GigaSpaces Technologies Ltd. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */
package org.springmodules.javaspaces.gigaspaces;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import org.springmodules.javaspaces.gigaspaces.app.ISimpleDao;
import org.springmodules.javaspaces.gigaspaces.app.SimpleBean;
import com.j_spaces.core.IJSpace;

public class SimpleDAOTest extends AbstractDependencyInjectionSpringContextTests {

    protected String[] getConfigLocations() {
        return new String[] {"/config/simple.xml" ,"/config/common.xml"};
    }

	public void testWriteTake() throws Exception{

		ISimpleDao dao = (ISimpleDao)applicationContext.getBean("simpleDAO");
		SimpleBean bean = new SimpleBean("zvika", 32);
		dao.writeSimple(bean);

		SimpleBean template = new SimpleBean("zvika", 32);
		SimpleBean taken = dao.takeIfExists(bean);

		assertEquals("written object is not equal to taken one!", bean, taken);
	}
}
