package org.springmodules.template.providers.stemp;

import java.io.*;
import java.util.*;

import org.springmodules.template.providers.stemp.resolvers.*;
import org.springmodules.template.providers.stemp.parsers.*;

/**
 * Stemp stands for <b>S</b>imple <b>Temp</b>late. As the name suggests, this is a simple template processor that comes
 * out-of-the-box with Spring Template. In its basics, Stemp only support template texts with expression place holders.
 * I has no support for flow control (conditinal or looping). It can be used for those simple cases, where
 * all you have is a parameterized text (i.e. text with expression placeholders - parameters) that doesn't need complex processing.
 * If complex processing is needed, conceder using other template processors.
 * <p/>
 * Stemp is a "self-factory" class. The user should not (cannot) instanciate a new Stemp explicitly by calling its
 * constructor. Instead one of the static factory methods should be used for that. This works quite similarly to the
 * way {@link java.util.regex.Pattern} is used. All factory methods are overloaded versions of the <code>compile(...)</code>
 * mehtod.
 * <p/>
 * Stemp has two unique features that other template engines (of its kind) lack: <br>
 * <ol>
 *  <li>
 *       Expressions in the placeholder can take any form. Stemp uses <code>ExpressionResolver</code> to abstract the
 *       process of evaluating an expression against a model. It comes with 5 different resolvers that can be set by
 *       calling <code>Stemp.setDefaultExpressionResolver(...)</code> or <code>Stemp.compile(...)</code>. The availabe
 *       expression resolvers are:<br/>
 *       <ul>
 *          <li>{@link org.springmodules.template.providers.stemp.resolvers.SimpleExpressionResolver}</li>
 *          <li>{@link org.springmodules.template.providers.stemp.resolvers.BeanWrapperExpressionResolver}</li>
 *          <li>{@link org.springmodules.template.providers.stemp.resolvers.OgnlExpressionResolver}</li>
 *          <li>{@link org.springmodules.template.providers.stemp.resolvers.XPathExpressionResolver}</li>
 *          <li>{@link org.springmodules.template.providers.stemp.resolvers.StempelExpressionResolver} (<b><i>the default</i></b>)</li>
 *       </ul>
 *       To know more about these expression resolvers please read their javadoc documentation.
 *  </li><br/>
 *  <li>
 *      Stemp is extendable. The extendability is achieved by using a configurable StempParser. It is possible to write
 *      a complex parser that also supports control flow elements such as conditinal and looping construts
 *      (<i>if</i>, <i>else</i>, <i>for</i>, <i>foreach</i>, etc..). As mentioned, the default parser is quite a simple
 *      one that only support expression placeholders. The reason it doesn't support more complex constructs is because we believe
 *      that other template engines can be used for that and there's no reason to reinvent the wheel.
 *  </li>
 * <ol>
 *
 *
 * @author Uri Boness
 */
public class Stemp {

    // the default expression resolver to use.
    private static ExpressionResolver defaultExpressionResolver = new StempelExpressionResolver();

    // the default expression wrapping to use (ant style).
    private static ExpressionWrapping defaultExpressionWrapping = new ExpressionWrapping("${", "}");

    // the parser to use.
    private static StempParser parser = new SimpleExpressionsPlaceholdersParser();

    private ExpressionResolver expressionResolver;
    private ExpressionWrapping expressionWrapping;

    // a compiled template (i.e. a Stemp instance) is made out of a list of stemplets where each one is
    // responsible for its own output.
    private List stemplets;

    // cached expressions. holds all expressions in the template. It is initialized lazily after the first call
    // to getExpressions() method.
    private String[] expressions;

    // a private construtor. Stemp is not constructed by the clients, instead the client should use
    // one of the static compilation methods.
    private Stemp(ExpressionResolver expressionResolver, ExpressionWrapping expressionWrapping) {
        this.expressionResolver = expressionResolver;
        this.expressionWrapping = expressionWrapping;
        stemplets = new ArrayList();
    }


    //=========================================== Configuration Methods ================================================

    /**
     * Sets the default expression resolver that will be used by Stemp.
     *
     * @param defaultExpressionResolver The default expression resolver that will be used by Stemp.
     */
    public static void setDefaultExpressionResolver(ExpressionResolver defaultExpressionResolver) {
        Stemp.defaultExpressionResolver = defaultExpressionResolver;
    }

    /**
     * Sets the default expression wrapping that will be used by stemp.
     *
     * @param defaultExpressionWrapping The default expression wrapping that will be used by stemp.
     */
    public static void setDefaultExpressionWrapping(ExpressionWrapping defaultExpressionWrapping) {
        Stemp.defaultExpressionWrapping = defaultExpressionWrapping;
    }

    /**
     * Sets the parser that will be used by Stemp.
     *
     * @param parser The parser that will be used by Stemp.
     */
    public static void setParser(StempParser parser) {
        Stemp.parser = parser;
    }


    //============================================== Factory Methods ===================================================

    /**
     * Compiles the given template source and constructs a Stemp instance from it. This uses the stempel expressoin
     * resolver {@link StempelExpressionResolver} and an Ant style expression wrapping (that is, an expression should
     * be in the form: <code>${exp}</code>.
     *
     * @param reader The reader from which the template source will be read.
     * @return The compiled template, that is, the newly constructed Stemp instance.
     */
    public static Stemp compile(Reader reader) {
        return compile(reader, defaultExpressionResolver);
    }

    /**
     * Compiles the given template source and constructs a Stemp instance from it. This uses an Ant style expression
     * wrapping (that is, an expression should be in the form: <code>${exp}</code>. The given expression resolver will
     * be used to resolve the expression.
     *
     * @param reader The reader from which the template source will be read.
     * @param expressionResolver The expression resolver that should be use.
     * @return The compiled template, that is, the newly constructed Stemp instance.
     */
    public static Stemp compile(Reader reader, ExpressionResolver expressionResolver) {
        return compile(reader, expressionResolver, defaultExpressionWrapping);
    }

    /**
     * Compiles the given template source and constructs a Stemp instance from it. This uses the stempel expressoin
     * resolver {@link StempelExpressionResolver}.
     *
     * @param reader The reader from which the template source will be read.
     * @param expressionWrapping The expression wrapping that should be used.
     * @return The compiled template, that is, the newly constructed Stemp instance.
     */
    public static Stemp compile(Reader reader, ExpressionWrapping expressionWrapping) {
        return compile(reader, defaultExpressionResolver, expressionWrapping);
    }

    /**
     * Compiles the given template source and constructs a Stemp instance from it.
     *
     * @param reader The reader from which the template source will be read.
     * @param expressionResolver The expression resolver that should be used.
     * @param expressionWrapping The expression wrapping that should be used.
     * @return The compiled template, that is, the newly constructed Stemp instance.
     */
    public static Stemp compile(Reader reader, ExpressionResolver expressionResolver, ExpressionWrapping expressionWrapping) {
        Stemp stemp = new Stemp(expressionResolver, expressionWrapping);
        stemp.stemplets = parser.parse(reader, expressionResolver, expressionWrapping);
        return stemp;
    }

    /**
     * Compiles the given template source and constructs a Stemp instance from it. This uses the stempel expressoin
     * resolver {@link StempelExpressionResolver} and an Ant style expression wrapping (that is, an expression should
     * be in the form: <code>${exp}</code>.
     *
     * @param text The text that serves as the template source.
     * @return The compiled template, that is, the newly constructed Stemp instance.
     */
    public static Stemp compile(String text) {
        return compile(new StringReader(text));
    }

    /**
     * Compiles the given template source and constructs a Stemp instance from it. This uses an Ant style expression
     * wrapping (that is, an expression should be in the form: <code>${exp}</code>. The given expression resolver will
     * be used to resolve the expression.
     *
     * @param text The text that serves as the template source.
     * @param expressionResolver The expression resolver that should be use.
     * @return The compiled template, that is, the newly constructed Stemp instance.
     */
    public static Stemp compile(String text, ExpressionResolver expressionResolver) {
        return compile(new StringReader(text), expressionResolver);
    }

    /**
     * Compiles the given template source and constructs a Stemp instance from it. This uses the stempel expressoin
     * resolver {@link StempelExpressionResolver}.
     *
     * @param text The text that serves as the template source.
     * @param expressionWrapping The expression wrapping that should be used.
     * @return The compiled template, that is, the newly constructed Stemp instance.
     */
    public static Stemp compile(String text, ExpressionWrapping expressionWrapping) {
        return compile(new StringReader(text), expressionWrapping);
    }

    /**
     * Compiles the given template source and constructs a Stemp instance from it.
     *
     * @param text The text that serves as the template source.
     * @param expressionResolver The expression resolver that should be used.
     * @param expressionWrapping The expression wrapping that should be used.
     * @return The compiled template, that is, the newly constructed Stemp instance.
     */
    public static Stemp compile(String text, ExpressionResolver expressionResolver, ExpressionWrapping expressionWrapping) {
        return compile(new StringReader(text), expressionResolver, expressionWrapping);
    }


    //============================================== Instance methods ==================================================

    /**
     * Generates the template output based on the given model.
     *
     * @param model The model for the template.
     * @return The generated output as a string.
     */
    public String generate(Map model) {
        StringWriter writer = new StringWriter();
        generate(model, writer);
        return writer.toString();
    }

    /**
     * Generates the template output based on the given model.
     *
     * @param model The model for the template.
     * @param out The output stream to which the generated output will be flushed.
     */
    public void generate(Map model, OutputStream out) {
        Writer writer = new OutputStreamWriter(out);
        generate(model, writer);
    }

    /**
     * Generates the template output based on the given model.
     *
     * @param model The model for the template.
     * @param writer The writer to which the generated output will bw written.
     */
    public void generate(Map model, Writer writer) {
        try {

            Map generationContext = new HashMap();

            for (Iterator i = stemplets.iterator(); i.hasNext();) {
                Stemplet stemplet = (Stemplet)i.next();
                stemplet.generate(writer, model, generationContext);
            }

        } catch (IOException ioe) {
            throw new StempException("Could not generate template", ioe);
        }
    }

    /**
     * Returns the raw template source as a string.
     *
     * @return The raw template source as a string.
     */
    public String dump() {
        StringWriter writer = new StringWriter();
        dump(writer);
        return writer.toString();
    }

    /**
     * Dumps the raw template source into the given output stream.
     *
     * @param output The output stream to which the raw template source will be dumped.
     */
    public void dump(OutputStream output) {
        Writer writer = new OutputStreamWriter(output);
        dump(writer);
    }

    /**
     * Dumps the raw template source into the given writer.
     *
     * @param writer The writer to which the raw template source will be dumped.
     */
    public void dump(Writer writer) {
        try {

            for (Iterator i = stemplets.iterator(); i.hasNext();) {
                Stemplet stemplet = (Stemplet)i.next();
                stemplet.dump(writer);
            }

        } catch(IOException ioe) {
            throw new StempException("Could not dump template", ioe);
        }
    }

    /**
     * Returns a list of all the exprssions used in the template source.
     *
     * @return A list of all the exprssions used in the template source.
     */
    public String[] getExpressions() {
        if (expressions == null) {
            List list = new ArrayList();
            for (Iterator i = stemplets.iterator(); i.hasNext();) {
                Stemplet stemplet = (Stemplet)i.next();
                addAll(list, stemplet.getExpressions());
            }
            expressions = (String[])list.toArray(new String[list.size()]);
        }
        return expressions;
    }

    /**
     * Returns the expression resolver that is used by this Stemp instance.
     *
     * @return The expression resolver that is used by this Stemp instance.
     */
    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    /**
     * Returns the expression wrapping that is used by this Stemp instance.
     *
     * @return The expression wrapping that is used by this Stemp instance.
     */
    public ExpressionWrapping getExpressionWrapping() {
        return expressionWrapping;
    }

    //================================================ Helper Methods ==================================================

    // adds all elements in the given array to the given list.
    private void addAll(List list, String[] array) {
        for (int i=0; i<array.length; i++) {
            list.add(array[i]);
        }
    }
}
