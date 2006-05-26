package org.springmodules.validation.valang.functions;

import java.util.regex.Pattern;

/**
 * Email function. Takes one argument. Converts the argument to a string using the <code>toString()</code> method, and
 * checks whether the returned string is a valid email address.
 *
 *
 * @author Uri Boness
 * @since May 26, 2006
 */
public class EmailFunction extends AbstractFunction {

    private static final Pattern pattern = Pattern.compile("^(([A-Za-z0-9]+_+)|([A-Za-z0-9]+\\-+)|([A-Za-z0-9]+\\.+)|([A-Za-z0-9]+\\++))*[A-Za-z0-9]+@((\\w+\\-+)|(\\w+\\.))*\\w{1,63}\\.[a-zA-Z]{2,6}$");

    public EmailFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
        definedExactNumberOfArguments(1);
    }

    protected Object doGetResult(Object target) throws Exception {
        Object value = getArguments()[0].getResult(target);
        String email = value.toString();
        return (pattern.matcher(email).matches()) ? Boolean.TRUE : Boolean.FALSE;
    }

}
