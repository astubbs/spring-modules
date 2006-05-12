package org.springmodules.lucene.index.support.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class is the implementation of DocumentMatching using the
 * JDK regular expressions to determine if a DocumentHandler matches
 * to a class.
 *   
 * @author Thierry Templier
 * @see java.util.regex.Pattern
 * @see java.util.regex.Matcher
 */
public class JdkRegexpDocumentMatching extends AbstractRegexpDocumentMatching {

	private Pattern compiledRegularExpression;
	
	public JdkRegexpDocumentMatching(String regularExpression) {
		super(regularExpression);
	}

	protected void initRegExpr(String regularExpression) {
		System.out.println("regularExpression = "+regularExpression);
		this.compiledRegularExpression=Pattern.compile(regularExpression);
	}

	protected boolean matchRegularExpression(String name) {
		System.out.println("name = "+name);
		Matcher matcher = this.compiledRegularExpression.matcher(name);
		return matcher.matches();
	}

}
