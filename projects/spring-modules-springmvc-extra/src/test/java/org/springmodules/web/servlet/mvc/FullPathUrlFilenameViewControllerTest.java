package org.springmodules.web.servlet.mvc;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Sergio Bossa
 */
public class FullPathUrlFilenameViewControllerTest extends TestCase {
    
    public FullPathUrlFilenameViewControllerTest(String testName) {
        super(testName);
    }

    public void testGetViewNameForUrlPath() {
        FullPathUrlFilenameViewController controller = new FullPathUrlFilenameViewController();
        String urlPath = "/foo/index.html";
        assertEquals("foo/index", controller.getViewNameForUrlPath(urlPath));
    }  
    
    public void testGetViewNameForRequest() {
        FullPathUrlFilenameViewController controller = new FullPathUrlFilenameViewController();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo/index.html");
        assertEquals("foo/index", controller.getViewNameForRequest(request));
    }  
}
