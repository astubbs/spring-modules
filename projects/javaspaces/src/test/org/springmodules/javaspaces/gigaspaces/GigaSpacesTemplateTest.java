/*
 * Title: Short descriptive title Description: A broad description of the
 * intention Copyright 2005 GigaSpaces Technologies Ltd. All rights reserved.
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT. GIGASPACES WILL NOT BE
 * LIABLE FOR ANY DAMAGE OR LOSS IN CONNECTION WITH THE SOFTWARE.
 */

package org.springmodules.javaspaces.gigaspaces;

import java.rmi.RemoteException;
import java.util.Random;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.javaspaces.gigaspaces.GigaSpacesTemplate;
import org.springmodules.javaspaces.gigaspaces.app.SimpleBean;


import com.j_spaces.core.client.ExternalEntry;
import com.j_spaces.core.client.UpdateModifiers;


public class GigaSpacesTemplateTest
		extends AbstractDependencyInjectionSpringContextTests
{

	GigaSpacesTemplate	template	= null;

	protected String[] getConfigLocations()
	{
		return new String[] { "/config/simple.xml","/config/common.xml" };
	}

	protected void onSetUp() throws Exception
	{

		template = (GigaSpacesTemplate) applicationContext.getBean("gigaSpacesTemplate");
	}

	public void testWrite() throws Exception
	{

		SimpleBean bean = new SimpleBean("ddd", 2);
		bean.setTestboolean(true);
		bean.setTestBooleanObject(new Boolean(true));
		template.write(bean, -1);
		SimpleBean template1 = new SimpleBean(null, 2);
		template1.setTestboolean(true);
		SimpleBean taken = (SimpleBean) template.take(template1, -1);
		assertEquals("written object is not equal to taken one!", bean, taken);
	}

	public void testTake() throws Exception
	{
		template.clean();
		SimpleBean bean = new SimpleBean("dddd", 1);
		template.write(bean, -1);
		SimpleBean templateBean = new SimpleBean(null, 1);
		SimpleBean taken = (SimpleBean) template.take(/*bean*/templateBean, -1);
		assertNotNull("Taken object is null!", taken);
	}

	public void testTakeIfExist() throws Exception
	{
		SimpleBean bean = new SimpleBean("lior_IfExists", 1);
		template.write(bean, -1);
		SimpleBean templateBean = new SimpleBean("lior_IfExists", 1);
		SimpleBean taken = (SimpleBean) template.takeIfExists(/*bean*/templateBean, -1);
		assertNotNull("Taken object is null!", taken);
	}

	public void testTakeWithTransaction() throws Throwable
	{
		SimpleBean bean = new SimpleBean("lior_TakeWithTransaction", 1);
		template.write(bean, -1);
		SimpleBean templateBean = new SimpleBean(	"lior_TakeWithTransaction",
													1);
		SimpleBean taken = (SimpleBean) template.take(/*bean*/	templateBean,
														Lease.ANY);
		assertNotNull("Taken object is null!", taken);
	}

	public void testTakeIfExistWithTransaction() throws Throwable
	{
		SimpleBean bean = new SimpleBean("lior_IfExistsTakeWithTransaction", 1);
		template.write(bean, -1);
		SimpleBean templateBean = new SimpleBean(	"lior_IfExistsTakeWithTransaction",
													1);
		SimpleBean taken = (SimpleBean) template.takeIfExists(	templateBean/*bean*/,
																2000);
		assertNotNull("Taken object is null!", taken);
	}

	public void testSnapshot()
	{
		SimpleBean templateBean = new SimpleBean("lior_Snapshot", 1);
		ExternalEntry taken = (ExternalEntry) template.snapshot(templateBean);
		assertNotNull("Taken object is null!", taken);
	}

	public void testCount() throws Exception
	{
		Random rn = new Random();
		String bean_name = "lior_count" + rn.nextInt();
		SimpleBean bean = new SimpleBean(bean_name, 1);
		template.write(bean, -1);
		SimpleBean templateBean = new SimpleBean(bean_name, 1);
		int count = (int) template.count(templateBean);
		assertEquals("The count is not 1!", 1, count);
	}

	public void testClear() throws Exception
	{
		SimpleBean bean = new SimpleBean("lior_clear", 1);
		template.write(bean, -1);
		SimpleBean templateBean = new SimpleBean("lior_clear", 1);
		template.clear(templateBean);
		SimpleBean taken = null;
		try
		{
			//throw exception if there is pk decleration
			taken = (SimpleBean) template.takeIfExists(templateBean, -1);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		assertNull("The bean is not been clear!", taken);
	}

	public void testClean() throws Exception
	{
		SimpleBean bean = new SimpleBean("lior_clean", 1);
		template.write(bean, -1);
		SimpleBean templateBean = new SimpleBean("lior_clean", 1);
		template.clean();
		SimpleBean taken = null;
		try
		{
			//throw exception if there is pk decleration
			taken = (SimpleBean) template.takeIfExists(templateBean, -1);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

		assertNull("The bean is not been clean!", taken);
	}

	public void testName()
	{
		String name = (String) template.getName();
		assertEquals("The name of the cache is not myCache!", "myCache", name);
	}

	public void testAdmin()
	{
		Object admin = (Object) template.getAdmin();
		assertNotNull("The admin is null!", admin);
	}

	public void testWriteMultiple() throws Throwable
	{
		SimpleBean bean1 = new SimpleBean("lior_multi1", 1);
		SimpleBean bean2 = new SimpleBean("lior_multi2", 1);
		template.writeMultiple(new Object[] { bean1, bean2 }, -1);
		SimpleBean templateBean = new SimpleBean("lior_multi1", 1);
		SimpleBean taken = (SimpleBean) template.take(/*bean1*/templateBean, -1);
		assertEquals("The bean are not equals!", bean1, taken);
	}

	public void testReadMultiple() throws Throwable
	{
		template.clean();
		SimpleBean bean1 = new SimpleBean("lior_read2", 1);
		SimpleBean bean2 = new SimpleBean("lior_read1", 1);
		template.writeMultiple(new Object[] { bean1, bean2 }, -1);
		SimpleBean templateBean = new SimpleBean(null, 1);
		Object[] taken = template.readMultiple(templateBean,  2);
		assertEquals("The bean are null!", 2, taken.length);
	}

	public void testTakeMultiple() throws Throwable
	{
		SimpleBean bean1 = new SimpleBean("lior_take1", 1);
		SimpleBean bean2 = new SimpleBean("lior_take2", 1);
		template.writeMultiple(new Object[] { bean1, bean2 }, -1);
		SimpleBean templateBean = new SimpleBean(null, 1);
		Object[] taken = template.takeMultiple(templateBean, 2);
		assertEquals("The bean are null!", 2, taken.length);
	}

	public void testPing() throws Exception
	{
		template.ping();
		assertTrue("The ping is null!", true);
	}
/*
	public void testReplace() throws Throwable
	{
		SimpleBean bean1 = new SimpleBean("lior_Replace1", 1);
		SimpleBean bean2 = new SimpleBean("lior_Replace2", 1);
		template.write(bean1, Lease.FOREVER);
		//SimpleBean templateBean = new SimpleBean("lior_Replace1", 1);
		Object[] taken = template.replace(bean1, bean2, Lease.FOREVER);
		assertEquals("The bean are null!", 2, taken.length);
	}
*/
	public void testUpdate() throws Throwable
	{
		template.clean();
		SimpleBean bean = new SimpleBean("lior_Update1", 1);
		template.write(bean,  Lease.FOREVER);
		SimpleBean templateBean = new SimpleBean("lior_Update1", 2);
		SimpleBean taken = (SimpleBean) template.update(templateBean/*bean*/,

														Lease.FOREVER,
														Lease.FOREVER);
		assertNotSame("The bean are equal!", templateBean, taken);
	}

	public void testUpdateModifier() throws Throwable
	{
		template.clean();
		SimpleBean bean = new SimpleBean("lior_UpdateModifier2", 1);
		template.write(bean,  Lease.FOREVER);
		SimpleBean templateBean = new SimpleBean("lior_UpdateModifier2", 2);
		SimpleBean taken = (SimpleBean) template.update(templateBean,
														Lease.FOREVER,
														Lease.FOREVER,
														UpdateModifiers.UPDATE_OR_WRITE);
		assertNotSame("The bean are equal!", templateBean, taken);
	}

	public void testUpdateMultiple() throws Throwable
	{
		template.clean();
		SimpleBean bean1 = new SimpleBean("lior_Update1", 1);
		SimpleBean bean2 = new SimpleBean("lior_Update2", 1);
		template.write(bean1,  Lease.FOREVER);
		template.write(bean2,  Lease.FOREVER);
		bean1.setAge(3);
		bean2.setAge(3);
		SimpleBean templateBean1 = new SimpleBean("lior_Update1", 2);
		SimpleBean templateBean2 = new SimpleBean("lior_Update2", 3);
		Object[] list = template.updateMultiple(new SimpleBean[] { templateBean1,
				templateBean2/*bean1,bean2 */}, new long[] { Lease.FOREVER,
				Lease.FOREVER });
		assertEquals("The bean are equal!", 2, list.length);
	}

	public void testUpdateMultipleModifier() throws Throwable
	{
		template.clean();
		SimpleBean bean1 = new SimpleBean("lior_Update1", 1);
		SimpleBean bean2 = new SimpleBean("lior_Update2", 1);
		template.write(bean1,  Lease.FOREVER);
		template.write(bean2,  Lease.FOREVER);
		SimpleBean templateBean1 = new SimpleBean("lior_Update1", 2);
		SimpleBean templateBean2 = new SimpleBean("lior_Update2", 3);
		Object[] taken = template.updateMultiple(new SimpleBean[] {templateBean1,
				templateBean2/*bean1,bean2*/ },  new long[] { Lease.FOREVER,
				Lease.FOREVER }, UpdateModifiers.UPDATE_ONLY);
		assertEquals("The bean are equal!", 2, taken.length);
	}

	public void testIsSecured()
	{
		boolean isSec = template.isSecured();
		assertNotNull("The isSecured is null!", new Boolean(isSec));
	}

	public void testDropClass() throws Throwable
	{
		SimpleBean bean1 = new SimpleBean("lior_dropClass", 1);
		template.write(bean1, Lease.FOREVER);
		template.dropClass(SimpleBean.class.getName());
		SimpleBean taken = (SimpleBean) template.takeIfExists(bean1, 1000);
		assertNull("The dropClass is not null!", taken);
	}

	public void testIsEmbedded() throws Throwable
	{
		boolean isEmbedded = template.isEmbedded();
		assertTrue("The isEmbedded is true!", isEmbedded);
	}

	public void testSetOptimisticLocking()
	{
		template.setOptimisticLocking(true);
		assertTrue("The testSetOptimisticLocking is true!", true);
	}

	public void testIsOptimisticLocking()
	{
		template.setOptimisticLocking(true);
		boolean ise = template.isOptimisticLockingEnabled();
		assertTrue("The testIsOptimisticLocking is false!", ise);

	}

	public void testSetFifo()
	{
		template.setFifo(true);
		assertTrue("The testSetFifo is true!", true);
	}

	public void testIsFifo()
	{
		template.setFifo(false);
		boolean ise = template.isFifo();
		assertFalse("The testIsFifo is true!", ise);

	}

	public void testSetNOWriteLeaseMode()
	{
		template.setNOWriteLeaseMode(true);
		assertTrue("The testSetNOWriteLeaseMode is true!", true);
	}

	public void testIsNOWriteLeaseMode()
	{
		template.setNOWriteLeaseMode(true);
		boolean ise = template.isNOWriteLeaseMode();
		assertTrue("The testisNOWriteLeaseMode is false!", ise);

	}



	public void testRead() throws Throwable
	{
		SimpleBean bean = new SimpleBean("lior_Read", 1);
		template.write(bean, -1);
		SimpleBean templateBean = new SimpleBean("lior_Read", 1);
		SimpleBean taken = (SimpleBean) template.read(	templateBean/*bean*/,

														Lease.ANY);
		assertNotNull("Read object is null!", taken);
	}

	public void testReadIfExist() throws Throwable
	{
		SimpleBean bean = new SimpleBean("lior_ReadIfExists", 1);
		template.write(bean, -1);
		SimpleBean templateBean = new SimpleBean("lior_ReadIfExists", 1);
		SimpleBean taken = (SimpleBean) template.readIfExists(	templateBean/*bean*/,

																2000);
		assertNotNull("Read object is null!", taken);
	}
	public void testNotify() throws Throwable
	{
		SimpleBean bean = new SimpleBean("lior_AddListener", 1);
		final SimpleBean templateBean = new SimpleBean("lior_AddListener", 1);
		RemoteEventListener listener = new RemoteEventListener(){
			public  void notify(RemoteEvent remoteevent)
	        throws UnknownEventException, RemoteException{
				assertNotNull("is null",remoteevent);
			}
		};
		template.notify(templateBean, listener, Lease.FOREVER, null, null);
		template.write(bean, -1);
	}

	public void testSetUpdateModifiers()
	{
		template.setUpdateModifiers(UpdateModifiers.UPDATE_ONLY);
		assertTrue("The testSetUpdateModifiers is true!", true);
	}

	public void testGetUpdateModifiers()
	{
		template.setUpdateModifiers(UpdateModifiers.UPDATE_ONLY);
		int ise = template.getUpdateModifiers();
		assertEquals(	"The testgetUpdateModifiers is false!",
						UpdateModifiers.UPDATE_ONLY,
						ise);

	}

	public void testSetReadTakeModifiers()
	{
		template.setReadTakeModifiers(UpdateModifiers.UPDATE_ONLY);
		assertTrue("The testSetReadTakeModifiersis true!", true);
	}

	public void testGetReadTakeModifiers()
	{
		template.setReadTakeModifiers(UpdateModifiers.UPDATE_ONLY);
		int ise = template.getReadTakeModifiers();
		assertEquals(	"The testgetReadTakeModifiers is false!",
						UpdateModifiers.UPDATE_ONLY,
						ise);
	}

}
