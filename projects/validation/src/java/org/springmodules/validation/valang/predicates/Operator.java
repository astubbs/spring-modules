/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.valang.predicates;

/**
 * <p>The Operator interface represents an operator in a validation rule.
 *
 * @author Steven Devijver
 * @since 23-04-2005
 */
public interface Operator {
	public interface EqualsOperator extends Operator {}

	public interface NotEqualsOperator extends Operator {}

	public interface LessThanOperator extends Operator {}

	public interface LessThanOrEqualOperator extends Operator {}

	public interface MoreThanOperator extends Operator {}

	public interface MoreThanOrEqualOperator extends Operator {}

	public interface InOperator extends Operator {}

	public interface NotInOperator extends Operator {}

	public interface BetweenOperator extends Operator {}

	public interface NotBetweenOperator extends Operator {}

	public interface NullOperator extends Operator {}

	public interface NotNullOperator extends Operator {}

	public interface HasTextOperator extends Operator {}

	public interface HasNoTextOperator extends Operator {}

	public interface HasLengthOperator extends Operator {}

	public interface HasNoLengthOperator extends Operator {}


	public interface IsBlankOperator extends Operator {}


	public interface IsNotBlankOperator extends Operator {}


	public interface IsWordOperator extends Operator {}


	public interface IsNotWordOperator extends Operator {}


	public interface IsUpperCaseOperator extends Operator {}


	public interface IsNotUpperCaseOperator extends Operator {}


	public interface IsLowerCaseOperator extends Operator {}


	public interface IsNotLowerCaseOperator extends Operator {}
}
