/**
 * Test for the SE friendly handler adapter
 */
package org.springmodules.web.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import junit.framework.TestCase;

/**
 * @author janm
 *
 */
public class TestSearchEngineFriendlyHandlerAdapter extends TestCase {

	private static final Log logger = LogFactory.getLog(TestSearchEngineFriendlyHandlerAdapter.class);
	private static final String GET = "get";

	private List suffixes;
	private String queryStringSeparator = "~";
	private String parameterSeparator = "_";
	private String nameValueSeparator = "-";

	private SearchEngineFriendlyHandlerAdapter adapter = new SearchEngineFriendlyHandlerAdapter();

	/**
	 * .ctor
	 */
	public TestSearchEngineFriendlyHandlerAdapter() {
		super();
		suffixes = new ArrayList();
		suffixes.add(".html");
	}
	
	/**
	 * Sets up adapter properties
	 */
	private void setAdapterProperties() {
		adapter.setQueryStringSeparator(queryStringSeparator);
		adapter.setNameValueSeparator(nameValueSeparator);
		adapter.setParameterSeparator(parameterSeparator);
		adapter.setSuffixes(suffixes);
	}

	/**
	 * Tests simple request; the request should be equivalent to /foo.html?a=1&b=2
	 * @throws Exception
	 */
	public void testSimpleRequest() throws Exception {
		queryStringSeparator = "~";
		parameterSeparator = "_";
		nameValueSeparator = "-";
		setAdapterProperties();
		
		HttpServletRequest request = new MockHttpServletRequest(GET, "/foo~a-1_b-2.html");
		adapter.handle(request, new MockHttpServletResponse(), new Controller() {

			public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
				assertEquals("a is not 1", "1", request.getParameter("a"));
				assertEquals("b is not 2", "2", request.getParameter("b"));
				assertEquals("not two parameters", 2, request.getParameterMap().size());
				assertEquals("The uri should be /foo.html", "/foo.html", request.getRequestURI());
				return null;
			}
		});
	}
	
	/**
	 * Tests simple request; the request should be equivalent to /foo.html?a=1&b=2
	 * @throws Exception
	 */
	public void testSimpleRequestMulticharSeparators() throws Exception {
		queryStringSeparator = ".qm.";
		parameterSeparator = ".am.";
		nameValueSeparator = ".eq.";
		setAdapterProperties();
		
		HttpServletRequest request = new MockHttpServletRequest(GET, "/foo.qm.a.eq.1.am.b.eq.2.html");
		adapter.handle(request, new MockHttpServletResponse(), new Controller() {

			public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
				assertEquals("a is not 1", "1", request.getParameter("a"));
				assertEquals("b is not 2", "2", request.getParameter("b"));
				assertEquals("not two parameters", 2, request.getParameterMap().size());
				assertEquals("The uri should be /foo.html", "/foo.html", request.getRequestURI());
				return null;
			}
		});

		queryStringSeparator = "~";
		parameterSeparator = "_";
		nameValueSeparator = "-";
	}
	
	/**
	 * Tests that request with suffix not listed in the suffixes list will not get
	 * processed
	 * @throws Exception
	 */
	public void testOtherSuffix() throws Exception {
		setAdapterProperties();
		
		HttpServletRequest request = new MockHttpServletRequest(GET, "/foo~a-1_b-2.gif");
		adapter.handle(request, new MockHttpServletResponse(), new Controller() {

			public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
				assertEquals("The uri should be /foo~a-1_b-2.gif", "/foo~a-1_b-2.gif", request.getRequestURI());
				assertEquals("there are parameters", 0, request.getParameterMap().size());
				return null;
			}
		});
	}

	/**
	 * Tests multiple attributes with the same name in the request. This is equivalent
	 * to foo.html?a=1&a=2...&a=n
	 * @throws Exception
	 */
	public void testMultipleValues() throws Exception {
		setAdapterProperties();
		
		HttpServletRequest request = new MockHttpServletRequest(GET, "/foo~a-1_a-2_b-3.html");
		adapter.handle(request, new MockHttpServletResponse(), new Controller() {

			public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
				String[] values = request.getParameterValues("a");
				assertEquals("Should have two values", 2, values.length);
				// there is no guarantee that the values will be in order; we must sort the names
				boolean foundOne = false, foundTwo = false;
				for (int i = 0; i < values.length; i++) {
					foundOne = foundOne || values[i].equals("1");
					foundTwo = foundTwo || values[i].equals("2");
				}
				// check that we have found the values
				assertTrue("Value 1 not found in a", foundOne);
				assertTrue("Value 2 not found in a", foundTwo);
				
				// check that b has the value set to 3
				assertEquals("b was not set to 3", "3", request.getParameter("b"));
				
				return null;
			}
			
		});
	}
}
