package de.ellersoftware.tools.ldifsort.comparators;

import java.util.Comparator;

import de.ellersoftware.tools.ldifsort.LdifAttrValue;

public class LdifAttrValueComparator 
       implements Comparator<LdifAttrValue> {

	public int compare(LdifAttrValue value1, LdifAttrValue value2) {
		return value1.getAttribute().compareTo(value2.getAttribute());
	}

}
