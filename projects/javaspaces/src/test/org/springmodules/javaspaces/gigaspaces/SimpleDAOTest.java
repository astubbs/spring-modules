package org.springmodules.javaspaces.gigaspaces;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import org.springmodules.javaspaces.gigaspaces.app.ISimpleDao;
import org.springmodules.javaspaces.gigaspaces.app.SimpleBean;

public class SimpleDAOTest extends AbstractDependencyInjectionSpringContextTests {

    protected String[] getConfigLocations() {
        return new String[] {"common.xml", "simple.xml" };
    }

	public void testWriteTake() throws Exception{

		ISimpleDao dao = (ISimpleDao)applicationContext.getBean("simpleDAO");
		SimpleBean bean = new SimpleBean("zvika", 32);
		dao.writeSimple(bean);

		SimpleBean template = new SimpleBean("zvika", 32);
		SimpleBean taken = dao.takeIfExists(bean);

//		SimpleBean template = new SimpleBean("zvika", null);
//		SimpleBean taken = dao.takeIfExists(template);

		assertEquals("written object is not equal to taken one!", bean, taken);
	}
}
