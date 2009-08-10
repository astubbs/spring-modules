package org.springmodules.xt.ajax.component;

import java.util.Arrays;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class FieldSetTest extends XMLEnhancedTestCase {
    
    public FieldSetTest(String testName) {
        super(testName);
    }

    public void testConstructor1() throws Exception {
        FieldSet fs = new FieldSet(new TaggedText("legend", TaggedText.Tag.SPAN));
        
        String rendering = fs.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("legend", "/fieldset/legend", rendering);
    }
    
    public void testConstructor2() throws Exception {
        FieldSet fs = new FieldSet(Arrays.asList(
                (Component) new InputField("test1", "test1", InputField.InputType.TEXT),
                (Component) new InputField("test2", "test2", InputField.InputType.TEXT)));
        
        String rendering = fs.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("test1", "/fieldset/input[1]/@name", rendering);
        assertXpathEvaluatesTo("test2", "/fieldset/input[2]/@name", rendering);
    }
    
    public void testConstructor3() throws Exception {
        FieldSet fs = new FieldSet(new TaggedText("legend", TaggedText.Tag.SPAN), 
                Arrays.asList(
                (Component) new InputField("test1", "test1", InputField.InputType.TEXT),
                (Component) new InputField("test2", "test2", InputField.InputType.TEXT)));
        
        String rendering = fs.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("legend", "/fieldset/legend", rendering);
        assertXpathEvaluatesTo("test1", "/fieldset/input[1]/@name", rendering);
        assertXpathEvaluatesTo("test2", "/fieldset/input[2]/@name", rendering);
    }
    
    public void testAddElement() throws Exception {
        FieldSet fs = new FieldSet(new TaggedText("legend", TaggedText.Tag.SPAN));
        fs.addElement(new InputField("test1", "test1", InputField.InputType.TEXT));
        fs.addElement(new InputField("test2", "test2", InputField.InputType.TEXT));
        
        String rendering = fs.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("legend", "/fieldset/legend", rendering);
        assertXpathEvaluatesTo("test1", "/fieldset/input[1]/@name", rendering);
        assertXpathEvaluatesTo("test2", "/fieldset/input[2]/@name", rendering);
    }
}
