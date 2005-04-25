package org.springmodules.util.dateparser;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

/**
 * @author Steven Devijver
 * @since 25-04-2005
 */
public class TestDefaultDateParser extends TestCase {

	public TestDefaultDateParser() {
		super();
	}

	public TestDefaultDateParser(String arg0) {
		super(arg0);
	}

	public void testFormat_junk1() {
		try {
			new DefaultDateParser().parse("2005-04-09 2330:00");
			fail();
		} catch (DateParseException e) {}
	}
	
	public void testFormat_yyyyMMdd() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409");
		assertNotNull(t);
		assertEquals("2005-04-09", formatDate(t, "yyyy-MM-dd"));
	}
	
	public void testFormat_yyyy_MM_dd() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409");
		assertNotNull(t);
		assertEquals("2005-04-09", formatDate(t, "yyyy-MM-dd"));
	}
	
	public void testFormat_yyyy_MM_dd_HH_mm_ss() throws DateParseException {
		Date t = new DefaultDateParser().parse("2005-04-09 23:30:00");
		assertNotNull(t);
		assertEquals("2005-04-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testFormat_yyyyMMdd_HHmmss() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000");
		assertNotNull(t);
		assertEquals("2005-04-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testFormat_yyyyMMdd_HH_mm_ss() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 23:30:00");
		assertNotNull(t);
		assertEquals("2005-04-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testFormat_yyyy_MM_dd_HHmmss() throws DateParseException {
		Date t = new DefaultDateParser().parse("2005-04-09 233000");
		assertNotNull(t);
		assertEquals("2005-04-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_addMilliseconds() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000+10S");
		assertNotNull(t);
		assertEquals("2005-04-09 23:30:00.010", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_subtractMilliseconds() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000-10S");
		assertNotNull(t);
		assertEquals("2005-04-09 23:29:59.990", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}	
	
	public void testModifier_shiftUpSecond() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000>s");
		assertNotNull(t);
		assertEquals("2005-04-09 23:30:01.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_shiftDownSecond() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000<s");
		assertNotNull(t);
		assertEquals("2005-04-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_addSeconds() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000+10s");
		assertNotNull(t);
		assertEquals("2005-04-09 23:30:10.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_subtractSeconds() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000-10s");
		assertNotNull(t);
		assertEquals("2005-04-09 23:29:50.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_shiftUpMinute() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000>m");
		assertNotNull(t);
		assertEquals("2005-04-09 23:31:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_shiftDownMinute() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233025<m");
		assertNotNull(t);
		assertEquals("2005-04-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_addMinutes() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000+10m");
		assertNotNull(t);
		assertEquals("2005-04-09 23:40:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_subtractMinutes() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000-10m");
		assertNotNull(t);
		assertEquals("2005-04-09 23:20:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_shiftUpHour() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000>H");
		assertNotNull(t);
		assertEquals("2005-04-10 00:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_shiftDownHour() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000<H");
		assertNotNull(t);
		assertEquals("2005-04-09 23:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_addHours() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000+10H");
		assertNotNull(t);
		assertEquals("2005-04-10 09:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_subtractHours() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000-10H");
		assertNotNull(t);
		assertEquals("2005-04-09 13:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_shiftUpDay() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000>d");
		assertNotNull(t);
		assertEquals("2005-04-10 00:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_shiftDownDay() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000<d");
		assertNotNull(t);
		assertEquals("2005-04-09 00:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_addDays() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000+10d");
		assertNotNull(t);
		assertEquals("2005-04-19 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_subtractDays() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000-10d");
		assertNotNull(t);
		assertEquals("2005-03-30 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_shiftUpWeek() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000>w");
		assertNotNull(t);
		assertEquals("2005-04-11 00:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_shiftDownWeek() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000<w");
		assertNotNull(t);
		assertEquals("2005-04-04 00:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_addWeeks() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000+10w");
		assertNotNull(t);
		assertEquals("2005-06-18 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_subtractWeeks() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000-10w");
		assertNotNull(t);
		assertEquals("2005-01-29 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_shiftUpMonth() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000>M");
		assertNotNull(t);
		assertEquals("2005-05-01 00:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_shiftDownMonth() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000<M");
		assertNotNull(t);
		assertEquals("2005-04-01 00:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_addMonths() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000+10M");
		assertNotNull(t);
		assertEquals("2006-02-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_subtractMonths() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000-10M");
		assertNotNull(t);
		assertEquals("2004-06-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_shiftUpYear() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000>y");
		assertNotNull(t);
		assertEquals("2006-01-01 00:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_shiftDownYear() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000<y");
		assertNotNull(t);
		assertEquals("2005-01-01 00:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testModifier_addYears() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000+10y");
		assertNotNull(t);
		assertEquals("2015-04-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testModifier_subtractYears() throws DateParseException {
		Date t = new DefaultDateParser().parse("20050409 233000-10y");
		assertNotNull(t);
		assertEquals("1995-04-09 23:30:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	public void testComplexModifier1() throws DateParseException {
		Date t = new DefaultDateParser().parse("2005-04-09 23:30:00>M+10d+8H");
		assertNotNull(t);
		assertEquals("2005-05-11 08:00:00.000", formatDate(t, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	public void testT() throws DateParseException {
		Date t1 = new Date();
		Date t2 = new DefaultDateParser().parse("T");
		Date t3 = new Date();
		
		assertNotNull(t2);
		assertTrue(t1.getTime() <= t2.getTime() && t2.getTime() <= t3.getTime());
	}
	
	public String formatDate(Date t, String format) {
		return new SimpleDateFormat(format).format(t);
	}
}
