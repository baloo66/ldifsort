package de.ellersoftware.tools.ldifsort.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import org.junit.Test;

import de.ellersoftware.tools.ldifsort.LdifAttrValue;
import de.ellersoftware.tools.ldifsort.LdifValue;

public class TestLdifAttrValue {

	@Test
	public void testConstructorAttribValue() {
		try {
			LdifAttrValue attrval = new LdifAttrValue(null, "");
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't instantiate null attribute name or null value");
		}

		try {
			LdifAttrValue attrval = new LdifAttrValue("attr", null, false);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't instantiate null attribute name or null value");
		}

		try {
			LdifAttrValue attrval = new LdifAttrValue(null, "val");
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't instantiate null attribute name or null value");
		}

		LdifAttrValue attrval = new LdifAttrValue("attr", "val");
		assertThat(attrval).isNotNull();
		assertThat(attrval.getAttribute()).isEqualTo("attr");
		assertThat(attrval.getValue()).isEqualTo("val");
		
		attrval = new LdifAttrValue("attr", new LdifValue("that's-a-test"));
		assertThat(attrval).isNotNull();
		assertThat(attrval.getAttribute()).isEqualTo("attr");
		assertThat(attrval.getValue()).isEqualTo("that's-a-test");
	}

	@Test
	public void testConstructorAttribValueBase() {
		try {
			LdifAttrValue attrval = new LdifAttrValue(null, null, true);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't instantiate null attribute name or null value");
		}

		try {
			LdifAttrValue attrval = new LdifAttrValue("attr", null, true);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't instantiate null attribute name or null value");
		}

		try {
			LdifAttrValue attrval = new LdifAttrValue(null, "dmFs", true);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't instantiate null attribute name or null value");
		}

		LdifAttrValue attrval = new LdifAttrValue("attr", "dmFs", true);
		assertThat(attrval).isNotNull();
		assertThat(attrval.getAttribute()).isEqualTo("attr");
		assertThat(attrval.wasEncoded()).isTrue();
		assertThat(attrval.getValue()).isEqualTo("dmFs");
		assertThat(attrval.getLine()).isEqualTo("attr:: dmFs");
	}

	@Test
	public void testConstructorString() {
		try {
			LdifAttrValue attrval = new LdifAttrValue(null);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't instantiate null attribute-value-line");
		}
		try {
			LdifAttrValue attrval = new LdifAttrValue("");
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException npe) {
			assertThat(npe).hasMessage("can't instantiate from []");
		}
		try {
			LdifAttrValue attrval = new LdifAttrValue(":");
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException npe) {
			assertThat(npe).hasMessage("can't instantiate attribut from [:]");
		}
		try {
			LdifAttrValue attrval = new LdifAttrValue(":another-value");
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException npe) {
			assertThat(npe).hasMessage("can't instantiate attribut from [:another-value]");
		}

		
		LdifAttrValue attrval = new LdifAttrValue("attr: das-ist-ein-nicht-kodierter-text");
		assertThat(attrval.getAttribute()).isEqualTo("attr");
		assertThat(attrval.wasEncoded()).isFalse();
		assertThat(attrval.isDN()).isFalse();
		assertThat(attrval.getValue()).isEqualTo("das-ist-ein-nicht-kodierter-text");
		assertThat(attrval.getUnprocessedValue()).isEqualTo("das-ist-ein-nicht-kodierter-text");
		assertThat(attrval.getLine()).isEqualTo("attr: das-ist-ein-nicht-kodierter-text");

		attrval = new LdifAttrValue("attr:: dW5kLWRhcy1pc3QtZWluLWtvZGllcnRlci10ZXh0");
		assertThat(attrval.getAttribute()).isEqualTo("attr");
		assertThat(attrval.wasEncoded()).isTrue();
		assertThat(attrval.isDN()).isFalse();
		assertThat(attrval.getValue()).isEqualTo("dW5kLWRhcy1pc3QtZWluLWtvZGllcnRlci10ZXh0");
		assertThat(attrval.getUnprocessedValue()).isEqualTo("und-das-ist-ein-kodierter-text");
		assertThat(attrval.getLine()).isEqualTo("attr:: dW5kLWRhcy1pc3QtZWluLWtvZGllcnRlci10ZXh0");

		attrval = new LdifAttrValue("dn: ou=test,ou=nocheintest,o=test");
		assertThat(attrval.getAttribute()).isEqualTo("dn");
		assertThat(attrval.wasEncoded()).isFalse();
		assertThat(attrval.isDN()).isTrue();
		assertThat(attrval.getValue()).isEqualTo("ou=test,ou=nocheintest,o=test");
		assertThat(attrval.getUnprocessedValue()).isEqualTo("ou=test,ou=nocheintest,o=test");
		assertThat(attrval.getLine()).isEqualTo("dn: ou=test,ou=nocheintest,o=test");

	}

	@Test
	public void testWasEncoded() {
		LdifAttrValue value = new LdifAttrValue("attr", "dasisteinlangerwertderumgebrochenwerdensollte");
		assertThat(value.wasEncoded()).isFalse();

		value = new LdifAttrValue("attr", "dasisteinlangerwertderumgebrochenwerdensollte", false);
		assertThat(value.wasEncoded()).isFalse();

		value = new LdifAttrValue("attr", "VGVzdGRhdGVu", true);
		assertThat(value.wasEncoded()).isTrue();
		assertThat(value.getLine()).isEqualTo("attr:: VGVzdGRhdGVu");
	}

	@Test
	public void testToString() {
		LdifAttrValue value = new LdifAttrValue("attr", "dasisteinlangerwertderumgebrochenwerdensollte");
		assertThat(value.toString()).isEqualTo("attr: dasisteinlangerwertderumgebrochenwerdensollte");
	}

	@Test
	public void testGetLine() {
		LdifAttrValue value = new LdifAttrValue("attr", "dasisteinlangerwertderumgebrochenwerdensollte");
		assertThat(value.getLine()).isEqualTo("attr: dasisteinlangerwertderumgebrochenwerdensollte");
	}

	@Test
	public void testGetLineWidth() {
		LdifAttrValue value = new LdifAttrValue("attr", "dasisteinlangerwertderumgebrochenwerdensollte");
		assertThat(value.getLine(10)).isEqualTo("attr: dasi\n steinlang\n erwertder\n umgebroch\n enwerdens\n ollte");
		assertThat(value.getLine(20)).isEqualTo("attr: dasisteinlange\n rwertderumgebrochen\n werdensollte");
		assertThat(value.getLine(49)).isEqualTo("attr: dasisteinlangerwertderumgebrochenwerdensoll\n te");
		assertThat(value.getLine(50)).isEqualTo("attr: dasisteinlangerwertderumgebrochenwerdensollt\n e");
		assertThat(value.getLine(51)).isEqualTo("attr: dasisteinlangerwertderumgebrochenwerdensollte");
		assertThat(value.getLine(52)).isEqualTo("attr: dasisteinlangerwertderumgebrochenwerdensollte");
		assertThat(value.getLine(1000)).isEqualTo("attr: dasisteinlangerwertderumgebrochenwerdensollte");
	}


	@Test
	public void testGetLdifValue() {
		LdifAttrValue value = new LdifAttrValue("attr", "dasisteinlangerwertderumgebrochenwerdensollte");
		assertThat(value.getLdifValue()).isEqualTo(new LdifValue("dasisteinlangerwertderumgebrochenwerdensollte", false));
	}



	@Test
	public void testEquality() {
		LdifAttrValue val1 = new LdifAttrValue("attr", "val");
		assertThat(val1).isNotNull();
		LdifAttrValue val2 = new LdifAttrValue("attr", "val");
		assertThat(val2).isNotNull();
		LdifAttrValue val3 = new LdifAttrValue("attrib", "val");
		assertThat(val3).isNotNull();
		LdifAttrValue val4 = new LdifAttrValue("attr", "value");
		assertThat(val4).isNotNull();
		LdifAttrValue val5 = new LdifAttrValue("attr:");
		assertThat(val5).isNotNull();
		
		assertThat(val1).isEqualTo(val1);
		assertThat(val1).isNotEqualTo(null);
		assertThat(val1).isNotEqualTo("something-special");

		assertThat(val1).isNotSameAs(val2).isNotSameAs(val3).isNotSameAs(val4);

		assertThat(val1).isEqualTo(val2);
		assertThat(val1).isNotEqualTo(val3);
		assertThat(val2).isNotEqualTo(val4);
		assertThat(val1).isNotEqualTo(val5);
		assertThat(val5).isNotEqualTo(val1);
		assertThat(val2).isEqualTo(val1);
		
		assertThat(val1.hashCode()).isNotEqualTo(0);

		
		
	}
}
