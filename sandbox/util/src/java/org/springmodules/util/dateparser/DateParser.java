package org.springmodules.util.dateparser;

import java.util.Date;

/**
 * <p>DateParser is a general contract to turn a string into a date or
 * else throw an exception.
 * 
 * @author Steven Devijver
 * @since 25-04-2005
 *
 */
public interface DateParser {

	public Date parse(String str) throws DateParseException;
}
