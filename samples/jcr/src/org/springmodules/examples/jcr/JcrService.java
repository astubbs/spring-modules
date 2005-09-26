package org.springmodules.examples.jcr;

import java.io.IOException;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springmodules.jcr.JcrTemplate;
import org.springmodules.jcr.SessionFactory;

/**
 * Sample class for working with the JCR repository.
 * @author Costin Leau
 *
 */
public class JcrService {

    /**
     * Main method.
     * 
     */
    public static void main(String args[]) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-repository.xml");
        
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("test.file");
        
        SessionFactory factory = (SessionFactory)context.getBean("jcrSessionFactory");
        factory.getSession();
        
        JcrTemplate template = (JcrTemplate)context.getBean("jcrTemplate");
        try {
            template.importFile(null, resource.getFile());
        } catch (IOException e) {
            System.out.println("file doesn't exist");
        }
        context.close();
    }
}
