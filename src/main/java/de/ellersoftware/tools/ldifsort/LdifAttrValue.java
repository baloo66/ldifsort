package de.ellersoftware.tools.ldifsort;

public class LdifAttrValue {

	private final static String DN_ATTRIBUTE = "dn";
	
	private String attribute;
	private LdifValue ldifValue;



	public LdifAttrValue(String newAttribute, String newValue) {
		this(newAttribute, newValue, false);
	}

	public LdifAttrValue(String newAttribute, String newValue, boolean base64) {
		if ((newAttribute == null) || (newValue == null)) {
			throw new NullPointerException("can't instantiate null attribute name or null value");
		}
		this.attribute = newAttribute.trim();
		this.ldifValue = new LdifValue(newValue, base64);
	}

	public LdifAttrValue(String newAttribute, LdifValue newValue) {
		if ((newAttribute == null) || (newValue == null)) {
			throw new NullPointerException("can't instantiate null attribute name or null value");
		}
		this.attribute = newAttribute.trim();
		this.ldifValue = newValue;
	}

	public LdifAttrValue(String attributeValueLine) {
		if (attributeValueLine == null) {
			throw new NullPointerException("can't instantiate null attribute-value-line");
		}
		attributeValueLine = attributeValueLine.trim();
		int pos = attributeValueLine.indexOf(':'); // first colon (right after attribute name)
		if (pos == -1) {
			throw new IllegalArgumentException("can't instantiate from [" + attributeValueLine + "]");
		}
		String attributeValue = attributeValueLine.substring(0,  pos).trim();
		if (attributeValue.length() <= 0) {
			throw new IllegalArgumentException("can't instantiate attribut from [" + attributeValueLine + "]");
		}
		this.attribute = attributeValue;
		String value = attributeValueLine.substring(pos + 1).trim();
		if (value.length() > 0) {
			if (value.startsWith(":")) { // base64
				value = value.substring(1).trim();
				this.ldifValue = new LdifValue(value, true);
			} else {
				this.ldifValue = new LdifValue(value, false);
			}
		} else {
			this.ldifValue = null;
		}
	}



	public boolean wasEncoded() {
		return ldifValue.wasEncoded();
	}



	public String getAttribute() {
		return this.attribute;
	}



	public boolean isDN() {
		return this.attribute.toLowerCase().equals(DN_ATTRIBUTE);
	}



	public String getUnprocessedValue() {
		return this.ldifValue.toUnprocessedString();
	}



	public String getValue() {
		return this.ldifValue.toString();
	}



	public LdifValue getLdifValue() {
		return this.ldifValue;
	}



	public String toString() {
		return getLine(0);
	}



	public String getLine() {
		return getLine(76);
	}



	public String getLine(int maxWidth) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.attribute);
		buffer.append(':');
		if (ldifValue.wasEncoded()) {
			buffer.append(':');
		}
		buffer.append(' ');
		buffer.append(ldifValue.toString());

		StringBuffer result;
		if ((maxWidth > 0) && (buffer.length() > maxWidth)) {
			result = new StringBuffer(buffer.length() + 100);
			int pos = 0;
			int chars2copy = 0;
			while (pos < buffer.length()) {
				if (pos > 0) {
					result.append("\n ");
				}
				if (pos > 0) {
					chars2copy = maxWidth - 1;
				} else {
					chars2copy = maxWidth;
				}
				if ((pos + chars2copy) > (buffer.length())) {
					chars2copy = buffer.length() - pos;
				}
				result.append(buffer.substring(pos, (pos + chars2copy)));
				pos = pos + chars2copy;
			}
		} else {
			result = buffer;
		}
		return result.toString();
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result + ((ldifValue == null) ? 0 : ldifValue.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LdifAttrValue other = (LdifAttrValue) obj;
		if (attribute == null) {
			if (other.attribute != null) {
				return false;
			}
		} else if (!attribute.equals(other.attribute)) {
			return false;
		}
		if (ldifValue == null) {
			if (other.ldifValue != null) {
				return false;
			}
		} else if (!ldifValue.equals(other.ldifValue)) {
			return false;
		}
		return true;
	}

}
