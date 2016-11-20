package de.ellersoftware.tools.ldifsort.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import de.ellersoftware.tools.ldifsort.LdifValue;

public class LdifStringComparator implements Comparator<LdifValue> {

	private boolean caseinsensitive;
	private Collator collator;
	private Locale locale;



	public LdifStringComparator(boolean caseinsensitive, Locale locale) {
		this.caseinsensitive = caseinsensitive;
		this.locale = locale;
		this.collator = Collator.getInstance(locale);
	}



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
		if (this.caseinsensitive) {
			return this.collator.compare(value1.getUnprocessedValue().toLowerCase(locale), value2.getUnprocessedValue().toLowerCase(locale));
		} else {
			return this.collator.compare(value1.getUnprocessedValue(), value2.getUnprocessedValue());
		}
		
	}

}