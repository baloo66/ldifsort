package de.ellersoftware.tools.ldifsort.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import org.junit.Test;

import de.ellersoftware.tools.ldifsort.LdifValue;

public class TestLdifValue {

	@Test
	public void testConstructorValue() {
		try {
			LdifValue val = new LdifValue(null);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't create LdifValue from null");
		}

		LdifValue val = new LdifValue("val");
		assertThat(val).isNotNull();
		assertThat(val.getValue()).isEqualTo("val");
	}

	@Test
	public void testConstructorValueBase() {
		try {
			LdifValue val = new LdifValue(null, false);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't create LdifValue from null");
		}

		LdifValue val = new LdifValue("val", false);
		assertThat(val).isNotNull();
		assertThat(val.wasEncoded()).isFalse();
		assertThat(val.getValue()).isEqualTo("val");
		assertThat(val.getUnprocessedValue()).isEqualTo("val");

		val = new LdifValue("dmFs", true);
		assertThat(val).isNotNull();
		assertThat(val.wasEncoded()).isTrue();
		assertThat(val.getValue()).isEqualTo("dmFs");
		assertThat(val.getUnprocessedValue()).isEqualTo("val");
	}

	@Test
	public void testEquality() {
		LdifValue val1 = new LdifValue("val", false);
		assertThat(val1).isNotNull();
		LdifValue val2 = new LdifValue("val", false);
		assertThat(val2).isNotNull();
		LdifValue val3 = new LdifValue("another-value", false);
		assertThat(val3).isNotNull();
		
		assertThat(val1.hashCode()).isNotEqualTo(0);
		
		assertThat(val1).isNotSameAs(val2);
		assertThat(val1).isEqualTo(val1);
		assertThat(val1).isEqualTo(val2);
		
		assertThat(val1).isNotEqualTo(null);
		assertThat(val1).isNotEqualTo(val3);
		
		assertThat(val1).isNotEqualTo("something-special");

		val1 = new LdifValue("dmFs", true);
		assertThat(val1).isNotNull();
		val2 = new LdifValue("dmFs", true);
		assertThat(val2).isNotNull();
		assertThat(val1).isNotSameAs(val2);
		assertThat(val1).isEqualTo(val2);

		val1 = new LdifValue("val", false);
		assertThat(val1).isNotNull();
		val2 = new LdifValue("dmFs", true);
		assertThat(val2).isNotNull();
		assertThat(val1).isNotSameAs(val2);
		assertThat(val1).isNotEqualTo(val2);
	}

	@Test
	public void testGetter() {
		LdifValue val = new LdifValue("val", false);
		assertThat(val).isNotNull();
		assertThat(val.wasEncoded()).isFalse();
		assertThat(val.getValue()).isEqualTo("val");
		assertThat(val.toString()).isEqualTo("val");
		assertThat(val.getUnprocessedValue()).isEqualTo("val");
		assertThat(val.toUnprocessedString()).isEqualTo("val");

		val = new LdifValue("dmFs", true);
		assertThat(val).isNotNull();
		assertThat(val.wasEncoded()).isTrue();
		assertThat(val.getValue()).isEqualTo("dmFs");
		assertThat(val.toString()).isEqualTo("dmFs");
		assertThat(val.getUnprocessedValue()).isEqualTo("val");
		assertThat(val.toUnprocessedString()).isEqualTo("val");
	}

}

