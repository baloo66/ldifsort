package de.ellersoftware.tools.ldifsort.comparators;

import java.util.Comparator;
import java.util.Locale;

import de.ellersoftware.tools.ldifsort.LdifEntry;
import de.ellersoftware.tools.ldifsort.LdifValue;

public class LdifEntryComparator 
       implements Comparator<LdifEntry> {

	Comparator<LdifValue> comparator = new LdifDNComparator();
	String key;

	public LdifEntryComparator(String sortKey, boolean sortAsDN, boolean sortNoCase, boolean sortNumeric, Locale sortLocale) {
		key = sortKey;
		if (sortAsDN) {
			comparator = new LdifDNComparator();
		} else if (sortNumeric) {
			comparator = new LdifNumberComparator();
		} else {
			comparator = new LdifStringComparator(sortNoCase, sortLocale);
		}
	}



	public int compare(LdifEntry value1, LdifEntry value2) {
		if (key.equals("dn")) {
			return comparator.compare(value1.getDn().getLdifValue(), value2.getDn().getLdifValue());
		} else {
			LdifValue ldifval1 = value1.getAttributeValue(key);
			LdifValue ldifval2 = value2.getAttributeValue(key);
			return comparator.compare(ldifval1, ldifval2);
		}
		
	}

}
