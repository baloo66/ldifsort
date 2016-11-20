package de.ellersoftware.tools.ldifsort.comparators;

import java.util.Comparator;

import de.ellersoftware.tools.ldifsort.LdifValue;

public class LdifNumberComparator implements Comparator<LdifValue> {

	public int compare(LdifValue value1, LdifValue value2) {
		if ((value1 == null) && (value2 == null)) {
			return 0;
		}
		if ((value1 == null) && (value2 != null)) {
			return -1;
		}
		if ((value1 != null) && (value2 == null)) {
			return 1;
		}
		return new Long(value1.getUnprocessedValue()).compareTo(new Long(value2.getUnprocessedValue()));
	}

}