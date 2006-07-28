package org.springmodules.validation.bean.conf;

/**
 * @author Uri Boness
 */
public interface DefaultValidationErrorCodes {

    public final static String DATE_IN_FUTURE_ERROR_CODE = "in.future";

    public final static String DATE_IN_PAST_ERROR_CODE = "in.past";

    public final static String EMAIL_ERROR_CODE = "email";

    public final static String EXPRESSION_ERROR_CODE = "expression";

    public final static String LENGTH_ERROR_CODE = "length";

    public final static String MIN_LENGTH_ERROR_CODE = "min.length";

    public final static String MAX_LENGTH_ERROR_CODE = "max.length";

    public final static String NOT_BLANK_ERROR_CODE = "not.blank";

    public final static String NOT_EMPTY_ERROR_CODE = "not.empty";

    public final static String NOT_NULL_ERROR_CODE = "not.null";

    public final static String RANGE_ERROR_CODE = "range";

    public final static String MIN_ERROR_CODE = "min";

    public final static String MAX_ERROR_CODE = "max";

    public final static String REG_EXP_ERROR_CODE = "regexp";

    public final static String SIZE_ERROR_CODE = "size";

    public final static String MIN_SIZE_ERROR_CODE = "min.size";

    public final static String MAX_SIZE_ERROR_CODE = "max.size";

}
