package org.springmodules.workflow.jbpm30;

import org.jbpm.graph.def.ProcessDefinition;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author robh
 */
public class ProcessDefinitionTests extends TestCase {

    public void testLoadProcessDefinition() throws IOException {
        ClassPathResource resource = new ClassPathResource("simpleWorkflow.xml", getClass());
        ProcessDefinition pd = ProcessDefinition.parseXmlInputStream(resource.getInputStream());
        assertNotNull(pd);
    }
}
