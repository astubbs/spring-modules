/*
 * Copyright 2006 the original author or authors.
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
package org.springmodules.xt.ajax.action.matcher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.ajax.ElementMatcher;

/**
 * <p>
 * Match elements to modify by using CSS Selectors.<br>
 * The SelectorMatcher will make actions matching elements identified
 * by a list of CSS Selectors.
 * </p>
 * <p>
 * Here is a list of supported selectors:
 * </p>
   <table>
        <thead>
        <tr>
            <th>Pattern</th>
            <th>Meaning</th>
            <th>CSS level</th></tr>
        <tbody>
        <tr>
            <td>*</td>
            <td>any element</td>
            <td>2</td></tr>
        <tr>
            <td>E</td>
            <td>an element of type E</td>
            <td>1</td></tr>
        <tr>
            <td>E[foo]</td>
            <td>an E element with a "foo" attribute</td>
            <td>2</td></tr>

        <tr>
            <td>E[foo="bar"]</td>
            <td>an E element whose "foo" attribute value is exactly
            equal to "bar"</td>
            <td>2</td></tr>
        <tr>
            <td>E[foo~="bar"]</td>
            <td>an E element whose "foo" attribute value is a list of
            space-separated values, one of which is exactly equal to "bar"</td>
            <td>2</td></tr>
        <tr>
            <td>E[foo^="bar"]</td>
            <td>an E element whose "foo" attribute value begins exactly
            with the string "bar"</td>
            <td>3</td></tr>
        <tr>
            <td>E[foo$="bar"]</td>
            <td>an E element whose "foo" attribute value ends exactly
            with the string "bar"</td>
            <td>3</td></tr>
        <tr>
            <td>E[foo*="bar"]</td>
            <td>an E element whose "foo" attribute value contains the
            substring "bar"</td>
            <td>3</td></tr>
        <tr>
            <td>E[attr|="value"]</td>
            <td>an E element whose "attr" attribute has a hyphen-separated
            list of values beginning (from the left) with "value"</td>
            <td>2</td></tr>
        <tr>
            <td>E:first-child</td>
            <td>an E element, first child of its parent</td>
            <td>2</td></tr>
        <tr>
            <td>E:last-child</td>
            <td>an E element, last child of its parent</td>
            <td>3</td></tr>
        <tr>
            <td>E:empty</td>
            <td>an E element that has no children (including text
            nodes)</td>
            <td>3</td></tr>
        <tr>
            <td>E#myid</td>
            <td>an E element with ID equal to "myid".</td>
            <td>1</td></tr>
        <tr>
            <td>E.myClass</td>
            <td>an E element with class name equal to "myClass" (HTML only).</td>
            <td>1</td></tr>
        <tr>
            <td>E F</td>
            <td>an F element descendant of an E element</td>
            <td>1</td></tr>
        <tr>
            <td>E &gt; F</td>
            <td>an F element child of an E element</td>
            <td>2</td></tr>
        <tr>
            <td>E + F</td>
            <td>an F element immediately preceded by an E element</td>
            <td>2</td></tr>
        <tr>
            <td>E ~ F</td>
            <td>an F element preceded by an E element</td>
            <td>3</td>
        </tr>
        </tbody>
   </table>
 * <p>
 * For additional info about CSS Selectors please visit:<br>
 * http://www.w3.org/TR/REC-CSS2/selector.html<br>
 * http://www.w3.org/TR/css3-selectors/<br>
 * </p>
 *
 * @author Sergio Bossa
 */
public class SelectorMatcher implements ElementMatcher {
    
    private static final long serialVersionUID = 26L;
    
    private List<String> selectors = new LinkedList();
    
    /**
     * @param selectors The list of CSS Selectors to use for matching.
     */
    public SelectorMatcher(List<String> selectors) {
        this.selectors = selectors;
    }

    public String render() {
        StringBuilder response = new StringBuilder();
        
        response.append("<matcher ");
        response.append("matchMode=\"selector\" ");
        response.append("contextNodeSelector=").append('"');
        Iterator<String> it = this.selectors.iterator();
        while (it.hasNext()) {
            String element = it.next();
            response.append(element);
            if (it.hasNext()) {
                response.append(",");
            }
        }
        response.append('"');
        response.append("/>");
        
        return response.toString();
    }
}
