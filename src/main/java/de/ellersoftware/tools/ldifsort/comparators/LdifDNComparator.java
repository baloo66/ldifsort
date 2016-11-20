package de.ellersoftware.tools.ldifsort.comparators;

import java.util.Comparator;

import de.ellersoftware.tools.ldifsort.LdifValue;

public class LdifDNComparator implements Comparator<LdifValue> {

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

		String[] parts1 = value1.getUnprocessedValue().split(",");
		String[] parts2 = value2.getUnprocessedValue().split(",");

		int len1 = parts1.length;
		int len2 = parts2.length;

		int lim = Math.min(len1, len2);

		int k = 0;
        while (k < lim) {
        	String s1 = parts1[len1 - k - 1].trim();
        	String s2 = parts2[len2 - k - 1].trim();
        	int c = compareKeyValue(s1, s2);
            if (c != 0) {
                return c;
            }
            k++;
        }
        return len1 - len2;
	}

	

	private static int compareKeyValue(String keyValue1, String keyValue2) {
		if ((keyValue1 == null) && (keyValue2 == null)) { // both null is equal
			return 0;
		}
		if ((keyValue1 == null) && (keyValue2 != null)) { // first null is lower
			return -1;
		}
		if ((keyValue1 != null) && (keyValue2 == null)) { // second null is higher
			return 1;
		}
		
		// ok, there ARE two arguments - split em
		String[] tokens = keyValue1.split("="); // split first key=value pair on = sign 
		if (tokens.length != 2) { 
			throw new IllegalArgumentException("first value no key=value pair");
		}
		String key1 = tokens[0].trim().toLowerCase();
		String value1 = tokens[1].trim().toLowerCase();
		
		tokens = keyValue2.split("="); // split second key=value pair on = sign
		if (tokens.length != 2) {
			throw new IllegalArgumentException("second value no key=value pair");
		}
		String key2 = tokens[0].trim().toLowerCase();
		String value2 = tokens[1].trim().toLowerCase();
		
		int compareResult = key1.compareTo(key2); // first compare the keys
		if (compareResult == 0) { // if keys are equal, compare values
			compareResult = value1.compareTo(value2);
		}
		return compareResult;
	}

}