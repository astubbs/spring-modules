package org.springmodules.validation.util.condition.parser.valang;

import java.io.StringReader;
import java.util.Map;

import org.springmodules.validation.util.BasicContextAware;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.adapter.CommonsPredicateCondition;
import org.springmodules.validation.util.condition.parser.ConditionParseException;
import org.springmodules.validation.util.condition.parser.ConditionParser;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.ValangParser;

/**
 * @author Uri Boness
 */
public class ValangConditionParser extends BasicContextAware implements ConditionParser {

    private Map dateParserRegistrations = null;

    private Map customFunctions = null;

    public ValangConditionParser() {
    }

    public ValangConditionParser(Map customFunctions, Map dateParserRegistrations) {
        this.customFunctions = customFunctions;
        this.dateParserRegistrations = dateParserRegistrations;
    }

    public Condition parse(String text) throws ConditionParseException {
        try {
            return new CommonsPredicateCondition(createValangParser(text).parseExpression());
        } catch (ParseException pe) {
            throw new ConditionParseException("Could not parse Valang expression '" + text + "'", pe);
        }
    }

    private ValangParser createValangParser(String text) {
        ValangParser parser = new ValangParser(new StringReader(text));
        initLifecycle(parser.getVisitor());
        parser.setDateParsersByRegexp(dateParserRegistrations);
        parser.setFunctionsByName(customFunctions);
        return parser;
    }

    public void setDateParserRegistrations(Map dateParserRegistrations) {
        this.dateParserRegistrations = dateParserRegistrations;
    }

    public void setCustomFunctions(Map customFunctions) {
        this.customFunctions = customFunctions;
    }

}
