package org.springmodules.validation.bean.rule.resolver;

import org.springmodules.validation.bean.rule.ErrorArgumentsResolver;

/**
 * @author Uri Boness
 */
public class StaticErrorArgumentsResolver implements ErrorArgumentsResolver {

    private Object[] arguments;

    /**
     * Constructs a new StaticErrorArgumentsResolver with no arguments.
     */
    public StaticErrorArgumentsResolver() {
        this(new Object[0]);
    }

    /**
     * Constructs a new StaticErrorArgumentsResolver with given static arguments.
     *
     * @param arguments The given static arguments.
     */
    public StaticErrorArgumentsResolver(Object[] arguments) {
        this.arguments = arguments;
    }

    /**
     * Returns the arguments that are configured in this resolver.
     *
     * @see ErrorArgumentsResolver#resolveArguments(Object)
     */
    public Object[] resolveArguments(Object obj) {
        return arguments;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Sets the static arguments to be returned by this resolver.
     *
     * @param arguments The static arguments to be returned by this resolver.
     */
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
