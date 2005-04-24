package org.springmodules.validation.predicates;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.AnyPredicate;
import org.springframework.util.StringUtils;
import org.springmodules.validation.functions.Function;
import org.springmodules.validation.functions.LiteralFunction;

/**
 * <p>GenericTestPredicate can test if a property value is null or not null.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 */
public class GenericTestPredicate extends AbstractPropertyPredicate {

	/**
	 * <p>Constructor taking two functions and an operator.
	 * 
	 * @param leftFunction the left function
	 * @param operator the operator.
	 */
	public GenericTestPredicate(Function leftFunction, Operator operator, Function rightFunction) {
		super(leftFunction, operator, rightFunction);
	}


	/**
	 * <p>The evaluate method takes the result of both functions and tests with the operator.
	 * 
	 * @param target the target bean
	 * @return the result of the test
	 */
	public boolean evaluate(Object target) {
		Object leftValue = getLeftFunction().getResult(target);
		Object rightValue = null;
		boolean dates = false;
		
		if (getRightFunction() != null) {
			rightValue = getRightFunction().getResult(target);
		}
		
		if (leftValue instanceof Number) {
			leftValue = new BigDecimal(leftValue.toString());
		}
		if (rightValue instanceof Number) {
			rightValue = new BigDecimal(rightValue.toString());
		}
		
		dates = leftValue instanceof Date && rightValue instanceof Date;
		
		if (getOperator() instanceof Operator.NullOperator) {
			return leftValue == null;
		} else if (getOperator() instanceof Operator.NotNullOperator) {
			return leftValue != null;
		} else if (getOperator() instanceof Operator.EqualsOperator) {
			if (leftValue instanceof BigDecimal) {
				return ((BigDecimal)leftValue).compareTo(rightValue) == 0;
			} else if (dates) {
				return ((Date)leftValue).getTime() == ((Date)rightValue).getTime();
			} else {
				return leftValue.equals(rightValue);
			}
		} else if (getOperator() instanceof Operator.NotEqualsOperator) {
			if (leftValue instanceof BigDecimal) {
				return ((BigDecimal)leftValue).compareTo(rightValue) != 0;
			} else if (dates) {
				return ((Date)leftValue).getTime() != ((Date)rightValue).getTime();
			} else {
				return !leftValue.equals(rightValue);
			}
		} else if (getOperator() instanceof Operator.LessThanOperator) {
			if (dates) {
				return ((Date)leftValue).getTime() < ((Date)rightValue).getTime();
			}
			return ((BigDecimal)leftValue).compareTo(rightValue) < 0;
		} else if (getOperator() instanceof Operator.LessThanOrEqualOperator) {
			if (dates) {
				return ((Date)leftValue).getTime() <= ((Date)rightValue).getTime();
			}
			return ((BigDecimal)leftValue).compareTo(rightValue) <= 0;
		} else if (getOperator() instanceof Operator.MoreThanOperator) {
			if (dates) {
				return ((Date)leftValue).getTime() > ((Date)rightValue).getTime();
			}
			return ((BigDecimal)leftValue).compareTo(rightValue) > 0;
		} else if (getOperator() instanceof Operator.MoreThanOrEqualOperator) {
			if (dates) {
				return ((Date)leftValue).getTime() >= ((Date)rightValue).getTime();
			}
			return ((BigDecimal)leftValue).compareTo(rightValue) >= 0;
		} else if (getOperator() instanceof Operator.InOperator) {
			Collection predicates = new ArrayList();
			for (Iterator iter = getIterator(rightValue); iter.hasNext();) {
				predicates.add(getPredicate(new LiteralFunction(leftValue), OperatorConstants.EQUALS_OPERATOR, (Function)iter.next()));
			}
			if (predicates.isEmpty()) {
				throw new IllegalStateException("IN expression contains no elements!");
			}
			return AnyPredicate.getInstance(predicates).evaluate(target);
		} else if (getOperator() instanceof Operator.NotInOperator) {
			Collection predicates = new ArrayList();
			for (Iterator iter = getIterator(rightValue); iter.hasNext();) {
				predicates.add(getPredicate(new LiteralFunction(leftValue), OperatorConstants.EQUALS_OPERATOR, (Function)iter.next()));
			}
			if (predicates.isEmpty()) {
				throw new IllegalStateException("NOT IN expression contains no elements!");
			}
			return !AnyPredicate.getInstance(predicates).evaluate(target);
		} else if (getOperator() instanceof Operator.BetweenOperator) {
			Object[] array = getArray(rightValue);
			Predicate predicate1 = getPredicate(new LiteralFunction(leftValue), OperatorConstants.MORE_THAN_OR_EQUAL_OPERATOR, (Function)array[0]);
			Predicate predicate2 = getPredicate(new LiteralFunction(leftValue), OperatorConstants.LESS_THAN_OR_EQUAL_OPERATOR, (Function)array[1]);
			return AndPredicate.getInstance(predicate1, predicate2).evaluate(target);
		} else if (getOperator() instanceof Operator.NotBetweenOperator) {
			Object[] array = getArray(rightValue);
			Predicate predicate1 = getPredicate(new LiteralFunction(leftValue), OperatorConstants.MORE_THAN_OR_EQUAL_OPERATOR, (Function)array[0]);
			Predicate predicate2 = getPredicate(new LiteralFunction(leftValue), OperatorConstants.LESS_THAN_OR_EQUAL_OPERATOR, (Function)array[1]);
			return !AndPredicate.getInstance(predicate1, predicate2).evaluate(target);
		} else if (getOperator() instanceof Operator.HasLengthOperator) {
			return StringUtils.hasLength(leftValue != null ? leftValue.toString() : null);
		} else if (getOperator() instanceof Operator.HasNoLengthOperator) {
			return !StringUtils.hasLength(leftValue != null ? leftValue.toString() : null);
		} else if (getOperator() instanceof Operator.HasTextOperator) {
			return StringUtils.hasText(leftValue != null ? leftValue.toString() : null);
		} else if (getOperator() instanceof Operator.HasNoTextOperator) {
			return !StringUtils.hasText(leftValue != null ? leftValue.toString() : null);
		}
		
		throw new IllegalStateException("Operator class [" + getOperator().getClass().getName() + "] not supported!");
	}

	protected Predicate getPredicate(Function leftFunction, Operator operator, Function rightFunction) {
		return new GenericTestPredicate(leftFunction, operator, rightFunction);
	}

}
