/**
 * Search Engine support 
 */
package org.springmodules.web.servlet.mvc;

import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.LastModified;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author janm
 */
public class SearchEngineFriendlyHandlerAdapter implements HandlerAdapter {

	/**
	 * List of <code>String</code>s
	 */
	private List suffixes;
	private String queryStringSeparator = "~";
	private String parameterSeparator = "_";
	private String nameValueSeparator = "-";

	/**
	 * Request parameter entry 
	 * @author janm
	 */
	static class RequestParameterEntry {
		private String name;
		private String value;

		/**
		 * Creates new instance of RequestParameterEntry
		 * @param name The parameter name
		 * @param value The parameter value
		 */
		public RequestParameterEntry(String name, String value) {
			this.name = name;
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if (obj instanceof RequestParameterEntry) {
				RequestParameterEntry rhs = (RequestParameterEntry)obj;
				
				return rhs.name.equals(this.name) && rhs.value.equals(this.value);
			} else {
				return false;
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			int result = 17;
			result += name.hashCode() * 37;
			result += value.hashCode() * 37;
			return super.hashCode();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			StringBuffer result = new StringBuffer();
			result.append(getName());
			result.append(" [ name=");
			result.append(name);
			result.append(", value=");
			result.append(value);
			result.append("]");
			
			return result.toString();
		}

		/**
		 * Gets the value of name 
		 * @return Value of name.
		 */
		public final String getName() {
			return name;
		}

		/**
		 * Gets the value of value 
		 * @return Value of value.
		 */
		public final String getValue() {
			return value;
		}

	}

	/**
	 * The SE wrapper class
	 */
	static class SearchEngineRequestWrapper extends HttpServletRequestWrapper {

		private String queryStringSeparator;
		private String parameterSeparator;
		private Pattern queryStringElementPattern;

		private HttpServletRequest request;
		private String servletPath;
		private StringBuffer url;
		private String queryString;
		private Map queryParameterMap; // map of String => String[]
		private List suffixes; // list of String

		/**
		 * .ctor
		 * @param request The request
		 */
		public SearchEngineRequestWrapper(HttpServletRequest request, List suffixes, String queryStringSeparator, String parameterSeparator, String nameValueSeparator) {
			super(request);
			this.parameterSeparator = parameterSeparator;
			this.queryStringSeparator = queryStringSeparator;
			this.request = request;
			this.suffixes = suffixes;
			this.queryStringElementPattern = Pattern.compile("^(.+)" + nameValueSeparator + "(.*)$");

			url = new StringBuffer(request.getRequestURL().length());
			queryParameterMap = new HashMap();
			for (Enumeration names = request.getParameterNames(); names.hasMoreElements();) {
				String name = (String) names.nextElement();
				String[] values = request.getParameterValues(name);
				queryParameterMap.put(name, values);
			}

			parseRequest(request);
		}

		/**
		 * Parses the query string and builds the queryParameters
		 */
		private void parseQueryString() {
			// don't bother with empty query strings
			if (queryString.length() == 0) return;

			// we have the query string; parse it
			if (queryString.indexOf(parameterSeparator) == -1) {
				parseQueryStringElement(queryString);
			} else {
				String sx[] = queryString.split(parameterSeparator);
				for (int i = 0; i < sx.length; i++) {
					parseQueryStringElement(sx[i]);
				}
			}
		}

		/**
		 * Parses the query string element
		 * 
		 * @param element The element in form of {name}<code>queryStringValueSeparator</code>{value}
		 */
		private void parseQueryStringElement(String element) {
			Matcher matcher = queryStringElementPattern.matcher(element);
			if (matcher.find()) {
				String name = matcher.group(1);
				String value = matcher.group(2);
				String[] values;
				String[] existing = (String[])queryParameterMap.get(name);
				if (existing != null) {
					values = new String[existing.length + 1];
					System.arraycopy(existing, 0, values, 0, existing.length);
					values[values.length - 1] = value;
				} else {
					values = new String[] { value };
				}
				queryParameterMap.put(name, values);
			}
		}

		/**
		 * Parses the request and separates the uri and the query string
		 * 
		 * @param request The request
		 */
		private void parseRequest(HttpServletRequest request) {
			String requestUri = request.getRequestURI();
			String suffix = null;
			for (Iterator i = suffixes.iterator(); i.hasNext();) {
				String s = (String) i.next();
				if (requestUri.endsWith(s)) {
					suffix = s;
					requestUri = requestUri.substring(0, requestUri.length() - s.length());
					break;
				}
			}

			if (suffix != null) {
				// we have not identified the suffix; keep the request as is.
	
				if (requestUri.indexOf(queryStringSeparator) == -1) {
					// the request uri does not contain the uriSeparator; i.e. it
					// represents just the uri
					queryString = "";
					url.append(requestUri);
				} else {
					String requestParts[] = requestUri.split(queryStringSeparator);
					url.append(requestParts[0]);
					queryString = requestParts[1];
				}
				url.append(suffix);

				parseQueryString();
			} else {
				url.append(requestUri);
			}
			servletPath = request.getServletPath().replace(queryStringSeparator + queryString, "");
		}

		public String getQueryString() {
			return queryString;
		}

		public String getRequestURI() {
			return url.toString();
		}

		public StringBuffer getRequestURL() {
			return url;
		}

		public String getParameter(String name) {
			String[] values = (String[])queryParameterMap.get(name);
			return (values != null ? values[0] : null);
			// return (value != null ? value.toString() : null);
		}

		public Map getParameterMap() {
			return queryParameterMap;
		}

		public Enumeration getParameterNames() {
			return Collections.enumeration(queryParameterMap.keySet());
		}

		public Enumeration getAttributeNames() {
			return request.getAttributeNames();
		}

		public Object getAttribute(String name) {
			return request.getAttribute(name);
		}

		public String[] getParameterValues(String name) {
			return (String[])queryParameterMap.get(name);
		}

		public String getServletPath() {
			return servletPath;
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerAdapter#supports(java.lang.Object)
	 */
	public boolean supports(Object handler) {
		return (handler instanceof Controller);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerAdapter#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpServletRequest wrapper;
		if (request.getQueryString() != null || request.getRequestURI().indexOf(queryStringSeparator) == -1) {
			// we have the traditional one
			wrapper = request;
		} else {
			// there is no traditional query separator
			wrapper = new SearchEngineRequestWrapper(request, suffixes, queryStringSeparator, parameterSeparator, nameValueSeparator);
		}

		return ((Controller) handler).handleRequest(wrapper, response);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerAdapter#getLastModified(javax.servlet.http.HttpServletRequest, java.lang.Object)
	 */
	public long getLastModified(HttpServletRequest request, Object handler) {
		if (handler instanceof LastModified) {
			return ((LastModified) handler).getLastModified(request);
		}
		return -1L;
	}

	/**
	 * @param suffixes The suffixes to set.
	 */
	public final void setSuffixes(List suffixes) {
		this.suffixes = suffixes;
	}

	/**
	 * Sets new value for field uriSeparator
	 * @param uriSeparator The uriSeparator to set.
	 */
	public final void setQueryStringSeparator(String uriSeparator) {
		this.queryStringSeparator = uriSeparator;
	}

	/**
	 * Sets new value for field nameValueSeparator
	 * @param nameValueSeparator The nameValueSeparator to set.
	 */
	public final void setNameValueSeparator(String nameValueSeparator) {
		this.nameValueSeparator = nameValueSeparator;
	}

	/**
	 * Sets new value for field parameterSeparator
	 * @param parameterSeparator The parameterSeparator to set.
	 */
	public final void setParameterSeparator(String parameterSeparator) {
		this.parameterSeparator = parameterSeparator;
	}

}