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

package org.springmodules.template.providers.stemp.parsers;

import java.io.*;
import java.util.*;

import org.springmodules.template.providers.stemp.*;
import org.springmodules.template.providers.stemp.stemplets.*;

/**
 * A simple StempParser implementation that searches for expressions wrapped with the given expression wrapping and
 * treats them as placeholders. The expressions are evaluated using the given expression resolver and the results are put
 * in the appropriate placeholder.
 *
 * @author Uri Boness
 */
public class SimpleExpressionsPlaceholdersParser implements StempParser {

    // todo performance can be improved if recursive implementation will be replaced by standard loops.
    public List parse(Reader reader, ExpressionResolver expressionResolver, ExpressionWrapping expressionWrapping)
        throws StempParseException {

        String text = readToString(reader);

        List stemplets = new ArrayList();
        if (!expressionWrapping.isSymmetric()) {
            parseHelper(text, expressionWrapping, expressionResolver, stemplets);
        } else {
            parseHelperWithSimetricWrapping(text, expressionWrapping, expressionResolver, stemplets);
        }
        return stemplets;
    }

    private void parseHelper(String text, ExpressionWrapping wrapping, ExpressionResolver resolver, List stemplets) {
        int expressionBeginIndex = -1;
        int expressionEndIndex = -1;
        int searchForExpressionBeginFrom = 0;

        while (true) {
            expressionBeginIndex = text.indexOf(wrapping.getPrefix(), searchForExpressionBeginFrom);
            if (expressionBeginIndex == -1 || expressionBeginIndex == text.length()-wrapping.getPrefix().length()) {
                stemplets.add(new StaticStemplet(text));
                return;
            }
            expressionEndIndex = text.indexOf(wrapping.getSuffix(), expressionBeginIndex +1);
            int nextParamBeginIndex = text.indexOf(wrapping.getPrefix(), expressionBeginIndex +1);
            if (nextParamBeginIndex == -1 || expressionEndIndex < nextParamBeginIndex) {
                break;
            }
            searchForExpressionBeginFrom = expressionBeginIndex + 1;
        }

        String wrappedExpression = text.substring(expressionBeginIndex, expressionEndIndex + 1);
        String expression = wrapping.unwrap(wrappedExpression);
        stemplets.add(new StaticStemplet(text.substring(0, expressionBeginIndex)));
        stemplets.add(new ExpressionStemplet(expression, resolver, wrapping));
        parseHelper(text.substring(expressionEndIndex + 1), wrapping, resolver, stemplets);
    }

    private void parseHelperWithSimetricWrapping(
        String text,
        ExpressionWrapping wrapping,
        ExpressionResolver resolver,
        List stemplets) {

        String delim = wrapping.getPrefix();

        while (text.length() > 0) {

            int firstDelimIndex = text.indexOf(delim);

            if (firstDelimIndex == -1) {
                stemplets.add(new StaticStemplet(text));
                return;
            }

            int secondDelimIndex = text.indexOf(delim, firstDelimIndex + 1);

            if (secondDelimIndex == -1) {
                stemplets.add(new StaticStemplet(text));
                return;
            }

            String textBeforeExpression = text.substring(0, firstDelimIndex);
            String wrappedExpression = text.substring(firstDelimIndex, secondDelimIndex+1);
            String expression = wrapping.unwrap(wrappedExpression);
            if (text.length() == secondDelimIndex + 1) {
                text = "";
            } else {
                text = text.substring(secondDelimIndex+1);
            }

            stemplets.add(new StaticStemplet(textBeforeExpression));
            stemplets.add(new ExpressionStemplet(expression, resolver, wrapping));
        }
    }

    protected String readToString(Reader reader) throws StempParseException {
        try {
            StringWriter writer = new StringWriter();
            BufferedReader bufferedReader = new BufferedReader(reader);
            int numOfChars = -1;
            char[] buffer = new char[1025];
            while ((numOfChars = bufferedReader.read(buffer)) != -1) {
                writer.write(buffer, 0, numOfChars);
            }
            reader.close();
            return writer.toString();
        } catch (IOException ioe) {
            throw new StempParseException("Could not parse stemp source", ioe);
        }
    }

}
