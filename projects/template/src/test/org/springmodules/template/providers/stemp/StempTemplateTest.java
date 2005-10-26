package org.springmodules.template.providers.stemp;

import java.io.*;

import org.springmodules.template.*;
import org.springmodules.template.providers.stemp.resolvers.*;

/**
 * @author Uri Boness
 */
public class StempTemplateTest extends AbstractTemplateTest {

    protected Template createTemplateForCommonModel() throws Exception {
        String templateText = "Name is ${person.name} and age is ${person.age}";
        Stemp stemp = Stemp.compile(new StringReader(templateText),
            new StempelExpressionResolver(), new ExpressionWrapping("${", "}"));
        return new StempTemplate(stemp);
    }

    protected String getOutputForCommonModel() {
        return "Name is John and age is 40";
    }

}
