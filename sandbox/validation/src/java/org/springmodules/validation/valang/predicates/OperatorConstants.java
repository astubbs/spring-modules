package org.springmodules.validation.valang.predicates;


/**
 * <p>OperatorConstants defines a number of operator constants.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 */
public interface OperatorConstants {

	public static final Operator EQUALS_OPERATOR = new Operator.EqualsOperator() {};
	public static final Operator NOT_EQUAL_OPERATOR = new Operator.NotEqualsOperator() {};
	public static final Operator LESS_THAN_OPERATOR = new Operator.LessThanOperator() {};
	public static final Operator LESS_THAN_OR_EQUAL_OPERATOR = new Operator.LessThanOrEqualOperator() {};
	public static final Operator MORE_THAN_OPERATOR = new Operator.MoreThanOperator() {};
	public static final Operator MORE_THAN_OR_EQUAL_OPERATOR = new Operator.MoreThanOrEqualOperator() {};
	public static final Operator IN_OPERATOR = new Operator.InOperator() {};
	public static final Operator NOT_IN_OPERATOR = new Operator.NotInOperator() {};
	public static final Operator BETWEEN_OPERATOR = new Operator.BetweenOperator() {};
	public static final Operator NOT_BETWEEN_OPERATOR = new Operator.NotBetweenOperator() {};
	public static final Operator NULL_OPERATOR = new Operator.NullOperator() {};
	public static final Operator NOT_NULL_OPERATOR = new Operator.NotNullOperator() {};
	public static final Operator HAS_TEXT_OPERATOR = new Operator.HasTextOperator() {};
	public static final Operator HAS_NO_TEXT_OPERATOR = new Operator.HasNoTextOperator() {};
	public static final Operator HAS_LENGTH_OPERATOR = new Operator.HasLengthOperator() {};
	public static final Operator HAS_NO_LENGTH_OPERATOR = new Operator.HasNoLengthOperator() {};
	public static final Operator IS_BLANK_OPERATOR = new Operator.IsBlankOperator() {};
	
	public static final Operator IS_NOT_BLANK_OPERATOR = new Operator.IsNotBlankOperator() {};
	
	public static final Operator IS_WORD_OPERATOR = new Operator.IsWordOperator() {};
	
	public static final Operator IS_NOT_WORD_OPERATOR = new Operator.IsNotWordOperator() {};
	
	public static final Operator IS_LOWER_CASE_OPERATOR = new Operator.IsLowerCaseOperator() {};
	
	public static final Operator IS_NOT_LOWER_CASE_OPERATOR = new Operator.IsNotLowerCaseOperator() {};
	
	public static final Operator IS_UPPER_CASE_OPERATOR = new Operator.IsUpperCaseOperator() {};
	
	public static final Operator IS_NOT_UPPER_CASE_OPERATOR = new Operator.IsNotUpperCaseOperator() {};
}
