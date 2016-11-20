package de.ellersoftware.tools.ldifsort.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.Locale;

import org.junit.Test;

import de.ellersoftware.tools.ldifsort.LdifEntry;
import de.ellersoftware.tools.ldifsort.LdifValue;
import de.ellersoftware.tools.ldifsort.comparators.LdifDNComparator;
import de.ellersoftware.tools.ldifsort.comparators.LdifEntryComparator;
import de.ellersoftware.tools.ldifsort.comparators.LdifNumberComparator;
import de.ellersoftware.tools.ldifsort.comparators.LdifStringComparator;

public class TestLdifComparators {

	@Test
	public void testLdifDNComparator() {
		LdifDNComparator comparator = new LdifDNComparator();
		LdifValue value1 = new LdifValue("ou=hugo,dn=test,o=test");
		LdifValue value2 = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);

		value1 = new LdifValue("ou=hugo,dn=test,o=test    ");
		value2 = new LdifValue("   ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);
	}

	@Test
	public void testLdifDNComparatorCompareToNull() {
		LdifDNComparator comparator = new LdifDNComparator();
		LdifValue value = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(comparator.compare(null, null)).isEqualTo(0);
		assertThat(comparator.compare(value, null)).isGreaterThan(0);
		assertThat(comparator.compare(null, value)).isLessThan(0);
	}

	@Test
	public void testLdifDNComparatorCompares() {
		LdifDNComparator comparator = new LdifDNComparator();
		LdifValue value1 = new LdifValue("ou=hugo,dn=test,o=test");
		LdifValue value2 = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);

		value1 = new LdifValue("ou=hugo,dn=test,o=test");
		value2 = new LdifValue("ou=hugo,dn=text,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isLessThan(0);

		value1 = new LdifValue("ou=hugo, dn=test, o=test");
		value2 = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);

		value1 = new LdifValue("ou    = hugo, dn=test, o= test ");
		value2 = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);

		value1 = new LdifValue("ou=hugo,         dn=test, o=test");
		value2 = new LdifValue("ou=hug, ou=hugo, dn=test, o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isLessThan(0);
		assertThat(comparator.compare(value2, value1)).isGreaterThan(0);
		
		value1 = new LdifValue("ou=hugo,dn=test,otest");
		value2 = new LdifValue("ou=hugo,dn=text,o=test");
		assertThat(value1).isNotSameAs(value2);
		try {
			assertThat(comparator.compare(value1, value2)).isLessThan(0);
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException iae) {
			assertThat(iae).hasMessage("first value no key=value pair");
		}
		
		value1 = new LdifValue("ou=hugo,dn=test,o=test");
		value2 = new LdifValue("ou=hugo,dn=text,otest");
		assertThat(value1).isNotSameAs(value2);
		try {
			assertThat(comparator.compare(value1, value2)).isLessThan(0);
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException iae) {
			assertThat(iae).hasMessage("second value no key=value pair");
		}
	}



	@Test
	public void testLdifStringComparator() {
		LdifStringComparator comparator = new LdifStringComparator(true, Locale.getDefault());
		LdifValue value1 = new LdifValue("ou=hugo,dn=test,o=test");
		LdifValue value2 = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);

		value1 = new LdifValue("ou=hugo,dn=test,o=test    ");
		value2 = new LdifValue("   ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);
	}

	@Test
	public void testLdifStringComparatorCompareToNull() {
		LdifStringComparator comparator = new LdifStringComparator(true, Locale.getDefault());
		LdifValue value = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(comparator.compare(null, null)).isEqualTo(0);
		assertThat(comparator.compare(value, null)).isGreaterThan(0);
		assertThat(comparator.compare(null, value)).isLessThan(0);
	}



	@Test
	public void testLdifStringComparatorCompares() {
		LdifStringComparator comparator = new LdifStringComparator(true, Locale.GERMANY);
		LdifValue value1 = new LdifValue("ou=hugo,dn=test,o=test");
		LdifValue value2 = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value1)).isEqualTo(0);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);

		value1 = new LdifValue("ou=test,ou=hugo,dn=test,o=test");
		value2 = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isNotEqualTo(0);
		assertThat(comparator.compare(value1, value2)).isGreaterThan(0);

		value1 = new LdifValue("ou    = hugo, dn=test, o= test ");
		value2 = new LdifValue("ou=hugo,dn=test,o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isGreaterThan(0);

		value1 = new LdifValue("ou=hugo,         dn=test, o=test");
		value2 = new LdifValue("ou=hug, ou=hugo, dn=test, o=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isGreaterThan(0);

		value1 = new LdifValue("18");
		value2 = new LdifValue("2");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isLessThan(0); // alpha-sort
	}




	@Test
	public void testLdifNumberComparator() {
		LdifNumberComparator comparator = new LdifNumberComparator();
		LdifValue value1 = new LdifValue("123");
		LdifValue value2 = new LdifValue("123");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);

		value1 = new LdifValue("123");
		value2 = new LdifValue("  123  ");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);
	}

	@Test
	public void testLdifNumberComparatorCompareToNull() {
		LdifNumberComparator comparator = new LdifNumberComparator();
		LdifValue value = new LdifValue("123");
		assertThat(comparator.compare(null, null)).isEqualTo(0);
		assertThat(comparator.compare(value, null)).isGreaterThan(0);
		assertThat(comparator.compare(null, value)).isLessThan(0);
	}

	@Test
	public void testLdifNumberComparatorCompares() {
		LdifNumberComparator comparator = new LdifNumberComparator();
		LdifValue value1 = new LdifValue("18");
		LdifValue value2 = new LdifValue("2");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isGreaterThan(0); // number-sort
	}




	@Test
	public void testLdifEntryComparator() {
		LdifEntryComparator comparator = new LdifEntryComparator("dn", true, true, false, Locale.getDefault());
		LdifEntry value1 = new LdifEntry("dn: ou=test,o=test,c=test");
		LdifEntry value2 = new LdifEntry("dn: ou=test,o=test,c=test");
		assertThat(value1).isNotSameAs(value2);
		assertThat(comparator.compare(value1, value2)).isEqualTo(0);
	}

}
