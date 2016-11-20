package de.ellersoftware.tools.ldifsort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LdifEntry  {

	private static final String DN_PREFIX = "dn:";
	private LdifAttrValue dn;
	private Map<String, List<LdifValue>> data;



	public LdifEntry(String newDnLine) {
		if (newDnLine == null) {
			throw new NullPointerException("can't create entry with dn==null");
		}
		newDnLine = newDnLine.trim();
		if (newDnLine.length() == 0) {
			throw new IllegalArgumentException("can't create entry with dn is not provided");
		}
		if (newDnLine.startsWith(DN_PREFIX)) {
			dn = new LdifAttrValue(newDnLine);
		} else {
			throw new IllegalArgumentException("can't create entry with no dn in data line");
		}
		this.data = new HashMap<String, List<LdifValue>>();
	}



	public void addAttribute(String attribute, String value) {
		addAttribute(attribute, value, false);
	}

	public void addAttribute(String attribute, String value, boolean base64) {
		if (attribute == null) {
			throw new NullPointerException("can't add attribute with null name");
		}
		if (value == null) {
			throw new NullPointerException("can't add attribute with null value");
		}
		LdifValue ldifvalue = new LdifValue(value, base64);
		addAttribute(attribute, ldifvalue);
	}

	public void addAttribute(LdifAttrValue attrValue) {
		if (attrValue == null) {
			throw new NullPointerException("can't add null attribute");
		}
		addAttribute(attrValue.getAttribute(), attrValue.getLdifValue());
	}

	public void addAttribute(String attribute, LdifValue ldifValue) {
		List<LdifValue> attributeValues = data.get(attribute);
		if (attributeValues == null) {
			attributeValues = new ArrayList<LdifValue>();
			attributeValues.add(ldifValue);
			data.put(attribute, attributeValues);
		} else {
			if (!(attributeValues.contains(ldifValue))) {
				attributeValues.add(ldifValue);
			}
		}
	}


	public LdifAttrValue getDn() {
		return this.dn;
	}


	public List<LdifAttrValue> getData() {
		List<LdifAttrValue> result = new ArrayList<LdifAttrValue>();

		for (Map.Entry<String, List<LdifValue>> entry : data.entrySet()) {
		    String key = entry.getKey();
		    List<LdifValue> list = entry.getValue();
		    for (LdifValue ldifValue : list) {
		    	if (ldifValue != null) {
		    		result.add(new LdifAttrValue(key, ldifValue.getValue(), ldifValue.wasEncoded()));
		    	} else {
		    		result.add(new LdifAttrValue(key, ""));
		    	}
			}
		}

		return result;
	}
	
	
	public LdifValue getAttributeValue(String key) {
		List<LdifValue> storedValues = data.get(key);
		if ((storedValues != null) && (storedValues.size() > 0)) {
			return storedValues.get(0);
		} else {
			return null;
		}
	}

}
