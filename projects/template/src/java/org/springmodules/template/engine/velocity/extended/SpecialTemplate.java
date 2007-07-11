/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.template.engine.velocity.extended;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/**
 * A special template that initializes and holds a compiled runtime representation of a velocity template (in the
 * form of a {@link SimpleNode}. This template can be retrieved from the {@link ExtendedVelocityEngine} by calling
 * {@link ExtendedVelocityEngine#getSpecialTemplate(org.springframework.core.io.Resource, String)} or
 * {@link ExtendedVelocityEngine#getSpecialTemplate(org.springframework.core.io.Resource, String)}. This template
 * was specially introduced as currently with the original velocity template engine, there is no way to create
 * a compiled runtime representation of a template directly out of an input stream or a reader.
 *
 * @author Uri Boness
 * @see ExtendedVelocityEngine#getSpecialTemplate(org.springframework.core.io.Resource, String)
 * @see ExtendedVelocityEngine#getSpecialTemplate(java.io.Reader, String, String)
 * @see ExtendedRuntimeInstance#getSpecialTemplate(java.io.Reader, String, String)
 */
public class SpecialTemplate {

    private final static Log logger = LogFactory.getLog(SpecialTemplate.class);

    private final SimpleNode node;

    private final String name;

    /**
     * Constructs a new SpecialTemplate with given node (represents the compiled template), runtime instance, and a name
     * that will be used for log messages.
     *
     * @param node The compile runtime representation of the template.
     * @param ri The velocity runtime instance to be used.
     * @param name A template name that will be used in log messages.
     */
    public SpecialTemplate(SimpleNode node, ExtendedRuntimeInstance ri, String name) {
        this.node = node;
        this.name = name;
        init(node, ri, name);
    }

    /**
     * Merges this template with the given velocity context and writes the merge result to the given writer.
     *
     * @param context The context to be merged with this template.
     * @param writer The writer to which the merged template will be written.
     * @throws IOException thrown when the merging fails.
     */
    public void merge(Context context, Writer writer) throws IOException {

        InternalContextAdapterImpl ica = new InternalContextAdapterImpl(context);

        ica.pushCurrentTemplateName(name);

        try {

            node.render(ica, writer);

        } catch (IOException ioe) {
            logger.error("Could not merge veloctiy template '" + name + "'", ioe);
            throw ioe;
        } finally {
            ica.popCurrentTemplateName();
        }
    }


    //============================================== Helper Methods ====================================================

    /**
     * Initializes this the given node using the given velocity runtime.
     *
     * @param node The node to initialize.
     * @param ri The velcotiy runtime instance that will be used while initilizing.
     * @param name A name to be associated with the template to be used in log messages.
     */
    protected static void init(SimpleNode node, ExtendedRuntimeInstance ri, String name) {
        InternalContextAdapterImpl ica = new InternalContextAdapterImpl(  new VelocityContext() );
        try {

            ica.pushCurrentTemplateName( name );
            node.init(ica, ri);

        } finally {
            ica.popCurrentTemplateName();
        }
    }

}
