package org.springmodules.validation.valang;

import java.io.StringReader;import java.util.Collection;import java.util.Date;import java.util.HashMap;import java.util.Iterator;import java.util.Map;import junit.framework.TestCase;import org.springframework.beans.BeanWrapperImpl;import org.springframework.validation.BindException;import org.springframework.validation.Errors;import org.springmodules.util.dateparser.DateParseException;import org.springmodules.util.dateparser.DefaultDateParser;import org.springmodules.validation.predicates.ValidationRule;

/**
 * @author Steven Devijver
 *
 */
public class ValangParserTests extends TestCase {

	public ValangParserTests() {
		super();
	}

	public ValangParserTests(String arg0) {
		super(arg0);
	}
	
	public class Person {
		private int age = 0;
		private String firstName = null;
		private String size = null;
		private Date dateOfBirth = null;		private int minAge = 18;		
		public Person(int age, String firstName) { super(); setAge(age); setFirstName(firstName); }
		public Person(String size) { super(); setSize(size); }
		public Person(Date dateOfBirth) { super(); setDateOfBirth(dateOfBirth); }
		public int getAge() { return this.age; }
		public void setAge(int age) { this.age = age; }
		public String getFirstName() { return this.firstName; }
		public void setFirstName(String firstName) { this.firstName = firstName; }
		public String getSize() { return this.size; }
		public void setSize(String size) { this.size = size; }				public Date getDateOfBirth() { return this.dateOfBirth; }		public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }				public int getMinAge() { return this.minAge; }				public Object[] getSizes() { return new Object[] { "S", "M", "L", "XL" }; }				public Map getMap() { Map map = new HashMap(); map.put("firstName", "Steven"); map.put("sizes", getSizes()); return map; }
	}
	
	private ValangParser getParser(String text) {
		ValangParser parser = new ValangParser(new StringReader(text));
		return parser;
	}

	private Collection parseRules(String text) {
		try {
			return getParser(text).parseValidation();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private boolean validate(Object target, String text) {
		Collection rules = parseRules(text);
		Errors errors = new BindException(target, "person");		Object tmpTarget = null;		if (!(target instanceof Map)) {
			tmpTarget = new BeanWrapperImpl(target);		} else {			tmpTarget = target;		}
		for (Iterator iter = rules.iterator(); iter.hasNext();) {
			ValidationRule rule = (ValidationRule)iter.next();
			rule.validate(tmpTarget, errors);
		}
		return !errors.hasErrors();
	}
	
	public void testParser1SimpleRule() {
		String text =
			"{age : age <= 120 : 'We do not do business with the undead.'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
	}
	
	public void testParser2TwoSimpleRules() {
		String text = 
			"{age : age >= 18 : 'Our customers must be 18 years or older.'}\n" +
			"{age : age <= 120 : 'We do not do business with the undead.'}";
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(150, "Steven"), text));
	}
	
	public void testParser3LengthRule() {
		String text = 
			"{firstName : length (firstName) < 7 and length (firstName) > -1 : 'First name must be no longer than 30 characters.'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(7, "Benjamin"), text));
	}
	
	public void testParser4NullRule() {
		String text =
			"{firstName : firstName is null : 'First name must be null.'}"
		;
		assertTrue(validate(new Person(20, null), text));
		assertFalse(validate(new Person(30, "Steven"), text));
	}
	
	public void testParser5NotNullRule() {
		String text =
			"{firstName : firstName is not null : 'First name must not be null.'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(20, null), text));
	}
	
	public void testParser6HasLengthRule() {
		String text =
			"{firstName : firstName has length : 'First name is required.'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(20, ""), text));
	}
	
	public void testParser7HasNoLengthRule() {
		String text =
			"{firstName : firstName has no length : 'First name is not allowed.'}"
		;
		assertTrue(validate(new Person(20, null), text));
		assertFalse(validate(new Person(30, "Steven"), text));
	}
	
	public void testParser8HasTextRule() {
		String text =
			"{firstName : firstName has text : 'First name is required.'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(20, "    "), text));
	}
	
	public void testParser9HasNoTextRule() {
		String text =
			"{firstName : firstName has no text and !(false) = true : 'First name is not allowed.'}"
		;
		assertTrue(validate(new Person(20, "   "), text));
		assertFalse(validate(new Person(30, "Steven"), text));
	}
	
	public void testParser10NotRule() {
		String text =
			"{firstName : not length (firstName) > 7 : 'First name must be not longer than 7 characters.'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(7, "Benjamin"), text));
	}
	
	public void testParser11ComplexNotRule() {
		String text =
			"{firstName : not length (firstName) > 7 or not(len (firstName) > 5 and len (firstName) > 7) : 'First name is not valid'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(30, "Coraline"), text));
	}
	
	public void testParser12ComplexRule1() {
		String text =
			"{firstName : (length (firstName) > 5 and age <= 30) or (firstName has length and age > 20) : 'Arrggh!!'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertTrue(validate(new Person(30, "Marie-Claire"), text));
		assertFalse(validate(new Person(7, "test"), text));
	}
	
	public void testParser13InRule() {
		String text =
			"{size : upper(?) in upper(lower('S')), upper(upper(lower(lower('M')))), upper('L'), 'XL' : 'Not a valid size.'}"
		;
		assertTrue(validate(new Person("M"), text));
		assertFalse(validate(new Person("XXL"), text));
	}
	
	public void testParser14NotInRule() {
		String text =
			"{firstName : firstName not in 'Bill', 'George' : 'We do not do business with Bill and George.'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(60, "Bill"), text));
	}
	
	public void testParser15LengthBetweenRule() {
		String text =
			"{firstName : length (firstName) between 0 and 6 : 'First name is required and must be not longer than 6 characters.'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(30, "Marie-Claire"), text));
	}
	
	public void testParser16LengthNotBetweenRule() {
		String text = 
			"{firstName : firstName is null or length (firstName) not between 0 and 6 : 'First name must not have a length between 0 and 6.'}"
		;
		assertTrue(validate(new Person(30, "Marie-Claire"), text));
		assertTrue(validate(new Person(20, null), text));
		assertFalse(validate(new Person(30, "Steven"), text));
	}
	
	public void testParser17BetweenRule() {
		String text =
			"{age : age = 0 or age between 18 and 120 : 'Age must be between 18 and 120.'}"
		;
		assertTrue(validate(new Person(30, "Steven"), text));
		assertFalse(validate(new Person(7, "Benjamin"), text));
	}
	
	public void testParser18NotBetweenRule() {
		String text =
			"{age : ? = 0 or age not between 20 and 30 : 'Age must not be between 20 and 30.'}"
		;
		assertTrue(validate(new Person(7, "Benjamin"), text));
		assertFalse(validate(new Person(30, "Steven"), text));
	}		public void testParser19Dates1() throws DateParseException {		String text = 			"{ dateOfBirth : dateOfBirth >= [1970-01-01] : 'You must be born after 1 january 1970.' }"		;		assertTrue(validate(new Person(new DefaultDateParser().parse("1974-11-24")), text));		assertFalse(validate(new Person(new DefaultDateParser().parse("1950-07-14")), text));	}		public void testParser20Dates2() throws DateParseException {		String text =			"{ dateOfBirth : ? is null or (dateOfBirth >= [T<d] and [T>d] > dateOfBirth) : 'You must be born today.' }"		;		assertTrue(validate(new Person(new DefaultDateParser().parse("T")), text));		assertFalse(validate(new Person(new DefaultDateParser().parse("T-1d")), text));	}		public void testParser21ErrorKey() {		String text =			"{ age : age >= 18 : 'Customers must be 18 years or older.' : '18_years_or_older' }"		;		assertTrue(validate(new Person(30, "Steven"), text));		assertFalse(validate(new Person(7, "Benjamin"), text));	}		public void testParser22ErrorArgs() {		String text =			"{ age : ? >= minAge : 'Customers must be older than {0}.' : 'not_old_enough' : minAge }"		;		assertTrue(validate(new Person(30, "Steven"), text));		assertFalse(validate(new Person(7, "Benjamin"), text));	}		public void testParser23InCollection() {		String text =			"{ size : ? in @sizes and ? in @$map('sizes') : 'Size is not correct.' }";		;		assertTrue(validate(new Person("M"), text));		assertFalse(validate(new Person("XXL"), text));	}		public void testParser23MapKey() {		String text = 			"{ firstName : ? = $map('firstName') : 'First name is not correct.' }"		;		assertTrue(validate(new Person(30, "Steven"), text));		assertFalse(validate(new Person(30, "Marie-Claire"), text));	}		public void testParser25MapTarget() {		Map customer = new HashMap();		customer.put("name", "Steven");		Map order = new HashMap();		order.put("customer", customer);				String text =			"{ customer.name : ? = 'Steven' : 'Customer name is incorrect.' }"		;		assertTrue(validate(order, text));	}	}
