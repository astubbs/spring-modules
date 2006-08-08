package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import junit.framework.TestCase;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class HighlightActionTest extends XMLEnhancedTestCase {
    
    public HighlightActionTest(String testName) {
        super(testName);
    }

    public void testRenderPart1() throws Exception {
        HighlightAction action = new HighlightAction("test");
        String rendering = action.execute();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("new Effect.Highlight(\"test\");", "//script", rendering);
    }
    
    public void testRenderPart2() throws Exception {
        HighlightAction action = new HighlightAction("test", new Float(0.5), "#ffff33", "#ffff44", "#ffff55");
        String rendering = action.execute();
        
        System.out.println(rendering);
        
        // Cannot test because there's no fixed order between json elements:
        //assertXpathEvaluatesTo("new Effect.Highlight(\"test\",{\"duration\":0.5,\"startcolor\"=\"#ffff33\",\"endcolor\"=\"#ffff44\",\"restorecolor\"=\"#ffff55\"});", "//script", rendering);
    }
}
