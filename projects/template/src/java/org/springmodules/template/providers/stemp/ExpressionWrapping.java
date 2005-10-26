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

package org.springmodules.template.providers.stemp;

/**
 * Represents an expression wrapping. In Stemp, expressions are wrapped with special characters to identify them
 * in the template text. For example, one can use Ant-like wrapping using <code>${</code> as a prefix and
 * <code>}</code> as a suffix.
 *
 * @author Uri Boness
 */
public class ExpressionWrapping {

    private String prefix;
    private String suffix;

    /**
     * Constructs a new ExpressionWrapping.
     */
    public ExpressionWrapping() {
    }

    /**
     * Constructs a new ExpressionWrapping with given prefix and suffix.
     *
     * @param prefix The prefix of the wrapping.
     * @param suffix The suffix of the wrapping.
     */
    public ExpressionWrapping(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * Constructs a symmetric expression wrappoing where the prefix and suffix are identical.
     *
     * @param symmetricWrapper The symmetric wrapper.
     */
    public ExpressionWrapping(String symmetricWrapper) {
        this.prefix = symmetricWrapper;
        this.suffix = symmetricWrapper;
    }

    /**
     * Returns the prefix of this wrapping.
     *
     * @return The prefix of this wrapping.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the prefix of this wrapping.
     *
     * @param prefix The prefix of this wrapping.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the suffix of this wrapping.
     *
     * @return The suffix of this wrapping.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets the suffix of this wrapping.
     *
     * @param suffix The suffix of this wrapping.
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Returns whether this wrapping is symmetric (that is, the prefix and suffix are identical).
     *
     * @return True if this wrapping is symmetric, false otherwise.
     */
    public boolean isSymmetric() {
        return prefix.equals(suffix);
    }

    /**
     * Wraps the given text with this wrapping.
     *
     * @param text The text to wrap.
     * @return The wrapped text.
     */
    public String wrap(String text) {
        return prefix + text + suffix;
    }

    /**
     * Returns whether the given text is wrapped with this wrapping.
     *
     * @param text The given text.
     * @return True if the given text is wrapped with this wrapping, false otherwise.
     */
    public boolean isWrapped(String text) {
        return text.startsWith(prefix) && text.endsWith(suffix);
    }

    /**
     * Unwraps the given text.
     *
     * @param text A text that is wrapped with this wrappoing.
     * @return The unwrapped version of the given text.
     */
    public String unwrap(String text) {
        if (!isWrapped(text)) {
            throw new IllegalArgumentException("Expected a text that is wrapped with '" + toString() + "'");
        }
        int prefixLength = prefix.length();
        int suffixLength = suffix.length();
        return text.substring(prefixLength, text.length() - suffixLength);
    }

    public String toString() {
        return prefix + "<param_name>" + suffix;
    }

}
