
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.gigaspaces.converter.IPojoToEntryConverter;
import org.springmodules.javaspaces.gigaspaces.GigaSpacesTemplate;
import org.springmodules.javaspaces.gigaspaces.app.SimpleBean;
import com.j_spaces.core.IJSpace;
import com.j_spaces.core.client.EntryArrivedRemoteEvent;
import com.j_spaces.core.client.ExternalEntry;
import com.j_spaces.core.client.NotifyModifiers;
import com.j_spaces.core.client.UpdateModifiers;


/**
 * Title: Description:
 * <p>
 * Copyright: Copyright 2006 GigaSpaces Technologies Ltd. All rights reserved.
 * Company: Gigaspaces Technologies
 *
 * @author Lior Ben Yizhak
 * @version 5.0
 */
public class GigaSpacesJDBCTest
extends AbstractDependencyInjectionSpringContextTests
{

	JdbcTemplate	template	= null;

	protected String[] getConfigLocations()
	{
		return new String[] { "JDBC.xml"};
	}

	protected void onSetUp() throws Exception
	{
		template = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
	}

	public void testJDBC() throws Exception
	{
		/* SQL CREATE TABLE statement
		 *
		 */
		String createSQL = "CREATE TABLE Person(FirstName varchar2 INDEX, " +
		"LastName varchar2)";
		System.out.println("Create table...");
		try {
			template.execute( createSQL );
			System.out.println("Create table... Done!");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\nTable may exist already... ");
			System.out.println("Restart or clean (space-browser) space !");

		}

		/* SQL INSERT statement
		 *
		 */
		int maxRows = 10;
		String insertSQL = "INSERT INTO Person VALUES(?,?)";
		System.out.println("Insert into table...");
		for (int i = 1; i < maxRows; i++) {
			Object[] params = new Object[] {"FirstName" + i,"LastName" + i};
			template.update(insertSQL, params);
			System.out.println("Insert into table... Done!");
		}
		/* SQL DELETE statement
		 *
		 */
		String deleteSQL="DELETE FROM Person WHERE FirstName='FirstName3'";
		System.out.println("Delete from table...");
		template.execute( deleteSQL );
		System.out.print("Delete from table...Done!");

		/* SQL SELECT statement
		 *
		 */
		String selectSQL="SELECT * FROM Person ORDER BY Person.FirstName";
		System.out.println("Select from table...");
		template.query( selectSQL, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException
			{
				System.out.println("FirstName : " + rs.getString("FirstName"));
				System.out.println("LastName : "+rs.getString("LastName"));
			}
		});
		System.out.println("Select from table... Done!");
	}



}