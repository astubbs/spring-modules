package org.springmodules.validation.util.fel;

/**
 * @author Uri Boness
 */
public interface Function {

    Object evaluate(Object argument) throws FelEvaluationException;

}
