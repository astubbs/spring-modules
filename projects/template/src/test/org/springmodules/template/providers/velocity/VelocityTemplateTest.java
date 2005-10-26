package org.springmodules.template.providers.velocity;
/**
 * @author Uri Boness
 */

import org.apache.velocity.app.*;
import org.springmodules.template.*;
import org.springmodules.template.sources.*;

public class VelocityTemplateTest extends AbstractTemplateTest {

    protected Template createTemplateForCommonModel() throws Exception {
        VelocityEngine engine = new VelocityEngine();
        engine.init();
        TemplateSource source = new StringTemplateSource("Name is ${person.name} and age is ${person.age}");
        return new VelocityTemplate(engine, source);
    }

    protected String getOutputForCommonModel() {
        return "Name is John and age is 40";
    }

}