/*
 * Copyright 2002-2005 the original author or authors.
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
package org.springmodules.validation.valang.javascript;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.NotPredicate;
import org.apache.commons.collections.functors.OrPredicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.ReflectiveVisitorHelper;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.JavaScriptUtils;
import org.springmodules.validation.valang.functions.AbstractFunction;
import org.springmodules.validation.valang.functions.AbstractMathFunction;
import org.springmodules.validation.valang.functions.AddFunction;
import org.springmodules.validation.valang.functions.BeanPropertyFunction;
import org.springmodules.validation.valang.functions.DateLiteralFunction;
import org.springmodules.validation.valang.functions.DivideFunction;
import org.springmodules.validation.valang.functions.Function;
import org.springmodules.validation.valang.functions.LengthOfFunction;
import org.springmodules.validation.valang.functions.LiteralFunction;
import org.springmodules.validation.valang.functions.LowerCaseFunction;
import org.springmodules.validation.valang.functions.MapEntryFunction;
import org.springmodules.validation.valang.functions.ModuloFunction;
import org.springmodules.validation.valang.functions.MultiplyFunction;
import org.springmodules.validation.valang.functions.NotFunction;
import org.springmodules.validation.valang.functions.SubtractFunction;
import org.springmodules.validation.valang.functions.TargetBeanFunction;
import org.springmodules.validation.valang.functions.UpperCaseFunction;
import org.springmodules.validation.valang.predicates.BasicValidationRule;
import org.springmodules.validation.valang.predicates.GenericTestPredicate;
import org.springmodules.validation.valang.predicates.Operator;
import org.springmodules.validation.valang.predicates.Operator.BetweenOperator;
import org.springmodules.validation.valang.predicates.Operator.EqualsOperator;
import org.springmodules.validation.valang.predicates.Operator.HasLengthOperator;
import org.springmodules.validation.valang.predicates.Operator.HasNoLengthOperator;
import org.springmodules.validation.valang.predicates.Operator.HasNoTextOperator;
import org.springmodules.validation.valang.predicates.Operator.HasTextOperator;
import org.springmodules.validation.valang.predicates.Operator.InOperator;
import org.springmodules.validation.valang.predicates.Operator.IsBlankOperator;
import org.springmodules.validation.valang.predicates.Operator.IsLowerCaseOperator;
import org.springmodules.validation.valang.predicates.Operator.IsNotBlankOperator;
import org.springmodules.validation.valang.predicates.Operator.IsNotLowerCaseOperator;
import org.springmodules.validation.valang.predicates.Operator.IsNotUpperCaseOperator;
import org.springmodules.validation.valang.predicates.Operator.IsNotWordOperator;
import org.springmodules.validation.valang.predicates.Operator.IsUpperCaseOperator;
import org.springmodules.validation.valang.predicates.Operator.IsWordOperator;
import org.springmodules.validation.valang.predicates.Operator.LessThanOperator;
import org.springmodules.validation.valang.predicates.Operator.LessThanOrEqualOperator;
import org.springmodules.validation.valang.predicates.Operator.MoreThanOperator;
import org.springmodules.validation.valang.predicates.Operator.MoreThanOrEqualOperator;
import org.springmodules.validation.valang.predicates.Operator.NotBetweenOperator;
import org.springmodules.validation.valang.predicates.Operator.NotEqualsOperator;
import org.springmodules.validation.valang.predicates.Operator.NotInOperator;
import org.springmodules.validation.valang.predicates.Operator.NotNullOperator;
import org.springmodules.validation.valang.predicates.Operator.NullOperator;

/**
 * Translates a collection of valang validation rules into a JavaScript statement  
 * that is capable of validating a HTML form and writes this to a provided 
 * <code>Writer</code>. This class is <b>not</b> thread safe so it is recommended 
 * that a new instance be created each time a translation is required.
 * 
 * <p>The generated JavaScript code is dependent on the code base found in the 
 * file "valang_codebase.js" having already been loaded into the page where
 * the validation will occur.
 * 
 * @author Oliver Hutchison
 */
public class ValangJavaScriptTranslator {

    /**
     * Returns a <code>Reader</code> for accessing the JavaScript codebase used by the 
     * translated validation rules.
     */
    public static Reader getCodebaseReader() {
        return new InputStreamReader(ValangJavaScriptTranslator.class.getResourceAsStream("valang_codebase.js"));
    }

    private static final Log logger = LogFactory.getLog(ValangJavaScriptTranslator.class);

    private static final ReflectiveVisitorHelper reflectiveVisitorHelper = new ReflectiveVisitorHelper();

    private Writer writer;

    public ValangJavaScriptTranslator() {
    }

    /**
     * Translates the provided set of Valang <code>BasicValidationRule</code>s into JavaScript
     * code capable of validating a HTML form and outputs the translated code into the provided
     * writer.
     * @param writer the writer to output the JavaScript code into
     * @param name the name of the command that is being validated
     * @param installSelfWithForm should the generated JavaScript attempt to install
     * its self with the form on creation
     * @param rules the collection of <code>BasicValidationRule</code>s to translate
     * @param messageSource the message source accessor that will be used to resolve validation
     * messages.
     */
    public void writeJavaScriptValangValidator(Writer writer, String name, boolean installSelfWithForm,
                                               Collection rules, MessageSourceAccessor messageSource) throws IOException {
        try {
            setWriter(writer);
            append("new ValangValidator(");
            appendJsString(name);
            append(',');
            append(Boolean.toString(installSelfWithForm));
            append(',');
            appendArrayValidators(rules, messageSource);
            append(')');
        }
        finally {
            clearWriter();
        }
    }

    protected void setWriter(Writer writer) {
        Assert.state(this.writer == null,
                "Attempted to set writer when one already set - is this class being used is multiple threads?");
        this.writer = writer;
    }

    protected void clearWriter() {
        writer = null;
    }

    protected void append(String string) throws IOException {
        writer.write(string);
    }

    protected void appendJsString(String string) throws IOException {
        writer.write('\'');
        writer.write(JavaScriptUtils.javaScriptEscape(string));
        writer.write('\'');
    }

    protected void append(char c) throws IOException {
        writer.write(c);
    }

    private void append(int i) throws IOException {
        writer.write(Integer.toString(i));
    }

    protected void appendArrayValidators(Collection rules, MessageSourceAccessor messageSource) throws IOException {
        append("new Array(");
        for (Iterator i = rules.iterator(); i.hasNext();) {
            appendValidatorRule((BasicValidationRule)i.next(), messageSource);
            if (i.hasNext()) {
                append(',');
            }
        }
        append(')');
    }

    protected void appendValidatorRule(BasicValidationRule rule, MessageSourceAccessor messageSource)
            throws IOException {
        append("new ValangValidator.Rule('");
        append(rule.getField());
        append("','not implemented',");
        appendJsString(getErrorMessage(rule, messageSource));
        append(',');
        appendValidationFunction(rule.getPredicate());
        append(')');
    }

    protected String getErrorMessage(BasicValidationRule rule, MessageSourceAccessor messageSource) {
        if (StringUtils.hasLength(rule.getErrorKey())) {
            if (rule.getErrorArgs() != null && !rule.getErrorArgs().isEmpty()) {
                // TODO: implement message arguments in JavaScript
                logger.warn("Translating error message with arguments is not implemented; using default message");
                return rule.getErrorMessage();
            }
            else {
                return messageSource.getMessage(rule.getErrorKey(), rule.getErrorMessage());
            }
        }
        else {
            return rule.getErrorMessage();
        }
    }

    protected void appendValidationFunction(Predicate p) throws IOException {
        append("function() {return ");
        doVisit(p);
        append('}');
    }

    protected void doVisit(Object value) throws IOException {
        reflectiveVisitorHelper.invokeVisit(this, value);
    }

    void visitNull() throws IOException {
        append("null");
    }

    void visit(Function f) throws IOException {
        if (logger.isWarnEnabled()) {
            logger.warn("Encountered unsupported custom function '" + f.getClass().getName() + "'");
        }
        append("this._throwError('don\\'t know how to handle custom function \\'");
        append(getNameForCustomFunction(f));
        append("\\'')");
    }

    void visit(AbstractFunction f) throws IOException {
        Function[] arguments = f.getArguments();
        append(getNameForCustomFunction(f));
        append('(');
        for (int i = 0; i < arguments.length; i++) {
            doVisit(arguments[i]);
            if (i < arguments.length - 1) {
                append(',');
            }
        }
        append(')');
    }

    protected String getNameForCustomFunction(Function f) {
        return "this." + ClassUtils.getShortName(f.getClass());
    }

    void visit(NotPredicate p) throws IOException {
        Assert.isTrue(p.getPredicates().length == 1);
        append("! (");
        doVisit(p.getPredicates()[0]);
        append(')');
    }

    void visit(AndPredicate p) throws IOException {
        String op = ") && ";
        for (int i = 0; i < p.getPredicates().length; i++) {
            Predicate innerP = p.getPredicates()[i];
            append('(');
            doVisit(innerP);
            if (i < p.getPredicates().length - 1) {
                append(op);
            }
            else {
                append(')');
            }
        }
    }

    void visit(OrPredicate p) throws IOException {
        String op = ") || ";
        for (int i = 0; i < p.getPredicates().length; i++) {
            Predicate innerP = p.getPredicates()[i];
            append('(');
            doVisit(innerP);
            if (i < p.getPredicates().length - 1) {
                append(op);
            }
            else {
                append(')');
            }
        }
    }

    void visit(GenericTestPredicate p) throws IOException {
        append(operatorToFunctionName(p.getOperator()));
        append("((");
        doVisit(p.getLeftFunction());
        append("), (");
        doVisit(p.getRightFunction());
        append("))");
    }

    protected String operatorToFunctionName(Operator operator) {
        if (operator instanceof EqualsOperator) {
            return "this.equals";
        }
        else if (operator instanceof NotEqualsOperator) {
            return "! this.equals";
        }
        else if (operator instanceof LessThanOperator) {
            return "this.lessThan";
        }
        else if (operator instanceof LessThanOrEqualOperator) {
            return "this.lessThanOrEquals";
        }
        else if (operator instanceof MoreThanOperator) {
            return "this.moreThan";
        }
        else if (operator instanceof MoreThanOrEqualOperator) {
            return "this.moreThanOrEquals";
        }
        else if (operator instanceof InOperator) {
            return "this.inFunc";
        }
        else if (operator instanceof NotInOperator) {
            return "! this.inFunc";
        }
        else if (operator instanceof BetweenOperator) {
            return "this.between";
        }
        else if (operator instanceof NotBetweenOperator) {
            return "! this.between";
        }
        else if (operator instanceof NullOperator) {
            return "this.nullFunc";
        }
        else if (operator instanceof NotNullOperator) {
            return "! this.nullFunc";
        }
        else if (operator instanceof HasTextOperator) {
            return "this.hasText";
        }
        else if (operator instanceof HasNoTextOperator) {
            return "! this.hasText";
        }
        else if (operator instanceof HasLengthOperator) {
            return "this.hasLength";
        }
        else if (operator instanceof HasNoLengthOperator) {
            return "! this.hasLength";
        }
        else if (operator instanceof IsBlankOperator) {
            return "this.isBlank";
        }
        else if (operator instanceof IsNotBlankOperator) {
            return "! this.isBlank";
        }
        else if (operator instanceof IsWordOperator) {
            return "this.isWord";
        }
        else if (operator instanceof IsNotWordOperator) {
            return "! this.isWord";
        }
        else if (operator instanceof IsUpperCaseOperator) {
            return "this.isUpper";
        }
        else if (operator instanceof IsNotUpperCaseOperator) {
            return "! this.isUpper";
        }
        else if (operator instanceof IsLowerCaseOperator) {
            return "this.isLower";
        }
        else if (operator instanceof IsNotLowerCaseOperator) {
            return "! this.isLower";
        }
        else {
            throw new UnsupportedOperationException("Unexpected operator type '" + operator.getClass().getName() + "'");
        }
    }

    void visit(TargetBeanFunction f) throws IOException {
        append("this.getTargetBean()");
    }

    void visit(BeanPropertyFunction f) throws IOException {
        append("this.getPropertyValue(");
        appendJsString(f.getField());
        append(')');
    }

    void visit(MapEntryFunction f) throws IOException {
        append("(");
        doVisit(f.getMapFunction());
        append("[");
        doVisit(f.getKeyFunction());
        append("])");
    }

    void visit(LiteralFunction f) throws IOException {
        Object literal = f.getResult(null);
        if (literal instanceof String) {
            appendJsString((String)literal);
        }
        else if (literal instanceof Number) {
            append(literal.toString());
        }
        else if (literal instanceof Boolean) {
            append(literal.toString());
        }
        else if (literal instanceof Function[]) {
            Function[] functions = (Function[])literal;
            appeandLiteralArray(functions);
        }
        else if (literal instanceof Collection) {
            appeandLiteralArray((((Collection)literal).toArray()));
        }
        else {
            throw new UnsupportedOperationException("Unexpected literal type '" + literal.getClass() + "'");
        }
    }

    void appeandLiteralArray(Object[] functions) throws IOException {
        append("new Array(");
        for (int i = 0; i < functions.length; i++) {
            doVisit(functions[i]);
            if (i < functions.length - 1) {
                append(",");
            }
        }
        append(')');
    }

    void visit(DateLiteralFunction f) throws IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date)f.getResult(null));
        append("new Date(");
        append(cal.get(Calendar.YEAR));
        append(", ");
        append(cal.get(Calendar.MONTH));
        append(", ");
        append(cal.get(Calendar.DATE));
        append(", ");
        append(cal.get(Calendar.HOUR_OF_DAY));
        append(", ");
        append(cal.get(Calendar.MINUTE));
        append(", ");
        append(cal.get(Calendar.SECOND));
        append(", ");
        append(cal.get(Calendar.MILLISECOND));
        append(')');
    }

    void visit(LengthOfFunction f) throws IOException {
        append("this.lengthOf(");
        doVisit(f.getArguments()[0]);
        append(')');
    }

    void visit(NotFunction f) throws IOException {
        append("! ");
        doVisit(f.getArguments()[0]);
    }

    void visit(UpperCaseFunction f) throws IOException {
        append("this.upperCase(");
        doVisit(f.getArguments()[0]);
        append(')');
    }

    void visit(LowerCaseFunction f) throws IOException {
        append("this.lowerCase(");
        doVisit(f.getArguments()[0]);
        append(')');
    }

    void visit(AbstractMathFunction f) throws IOException {
        append(mathToFunctionName(f));
        append("((");
        doVisit(f.getLeftFunction());
        append("),(");
        doVisit(f.getRightFunction());
        append("))");
    }

    protected String mathToFunctionName(AbstractMathFunction f) {
        if (f instanceof AddFunction) {
            return "this.add";
        }
        else if (f instanceof DivideFunction) {
            return "this.divide";
        }
        else if (f instanceof ModuloFunction) {
            return "this.modulo";
        }
        else if (f instanceof MultiplyFunction) {
            return "this.multiply";
        }
        else if (f instanceof SubtractFunction) {
            return "this.subtract";
        }
        else {
            throw new UnsupportedOperationException("Unexpected math function type '" + f.getClass().getName() + "'");
        }
    }
}