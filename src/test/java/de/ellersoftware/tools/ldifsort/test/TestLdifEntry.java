package de.ellersoftware.tools.ldifsort.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.ellersoftware.tools.ldifsort.LdifAttrValue;
import de.ellersoftware.tools.ldifsort.LdifEntry;

public class TestLdifEntry {

	@Test
	public void testConstructor() {
		try {
			LdifEntry entry = new LdifEntry(null);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException npe) {
			assertThat(npe).hasMessage("can't create entry with dn==null");
		}

		LdifEntry entry = new LdifEntry("dn: cn=test,ou=test");
		assertThat(entry).isNotNull();
		assertThat(entry.getDn().getValue()).isEqualTo("cn=test,ou=test");
	}

	@Test
	public void testWithSimpleValues() {
		LdifEntry entry = new LdifEntry("dn: cn=LBS-Schema, ou=schema");
		assertThat(entry).isNotNull();
		assertThat(entry.getDn().getValue()).isEqualTo("cn=LBS-Schema, ou=schema");
		assertThat(entry.getData()).isNotNull();

		entry.addAttribute("objectclass", "metaSchema");
		entry.addAttribute("objectclass", "top");
		entry.addAttribute("cn", "LBS-Schema");
		entry.addAttribute("m-dependencies", "core", false);
		entry.addAttribute("m-dependencies", "cosine");
		entry.addAttribute("m-dependencies", "inetorgperson");
		entry.addAttribute(new LdifAttrValue("attr", "value"));
		entry.addAttribute(new LdifAttrValue("attr", "value", false)); // duplicate should be ignored

		List<LdifAttrValue> data = entry.getData();
		assertThat(data).isNotEmpty();
		assertThat(data.size()).isEqualTo(7); // duplicate attr/value pair counts only once

		LdifAttrValue[] masterData = { new LdifAttrValue("attr", "value"),
				                       new LdifAttrValue("m-dependencies", "cosine"),
				                       new LdifAttrValue("cn", "LBS-Schema"),
				                       new LdifAttrValue("m-dependencies", "core"),
				                       new LdifAttrValue("objectclass", "top"),
				                       new LdifAttrValue("m-dependencies", "inetorgperson"),
				                       new LdifAttrValue("objectclass", "metaSchema") };
		assertThat(data).containsAll(Arrays.asList(masterData));
		assertThat(data).containsOnlyElementsOf(Arrays.asList(masterData));
		
		assertThat(entry.getAttributeValue("cn").getValue()).isEqualTo("LBS-Schema");
		assertThat(entry.getAttributeValue("m-dependencies")).isNotNull();
		assertThat(entry.getAttributeValue("m-dependencies").getValue()).isIn("inetorgperson", "cosine", "core");
		assertThat(entry.getAttributeValue("not-available-attribute")).isNull();
	}

	@Test
	public void testWithEncodedValues() {
		LdifEntry entry = new LdifEntry("dn: cn=LBS-Schema, ou=schema");
		assertThat(entry).isNotNull();
		assertThat(entry.getDn().getValue()).isEqualTo("cn=LBS-Schema, ou=schema");
		assertThat(entry.getData()).isNotNull();

		entry.addAttribute("objectclass", "bWV0YVNjaGVtYQ==", true);
		entry.addAttribute("cn", "LBS-Schema");

		List<LdifAttrValue> data = entry.getData();
		assertThat(data).isNotEmpty();
		assertThat(data.size()).isEqualTo(2); // duplicate attr/value pair counts only once

		LdifAttrValue[] masterData = { new LdifAttrValue("cn", "LBS-Schema"),
				                       new LdifAttrValue("objectclass", "bWV0YVNjaGVtYQ==", true) };
		assertThat(data).containsAll(Arrays.asList(masterData));
		assertThat(data).containsOnlyElementsOf(Arrays.asList(masterData));
	}


	//-----------------
//	private static LdifEntryBuilder $LdifEntry() {
//		return new LdifEntryBuilder();
//	}
//
//	private static LdifEntry a(LdifEntryBuilder builder) {
//		return builder.build();
//	}

}
