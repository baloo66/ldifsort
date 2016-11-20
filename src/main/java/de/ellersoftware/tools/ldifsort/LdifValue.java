package de.ellersoftware.tools.ldifsort;

import java.util.Arrays;
import java.util.Base64;

public class LdifValue {

	private byte[] data;
	private boolean wasEncoded;



	public LdifValue(String value) {
		initLdifValue(value, false);
	}

	public LdifValue(String value, boolean base64) {
		initLdifValue(value, base64);
	}



	protected void initLdifValue(String value, boolean base64) {
		if (value == null) {
			throw new NullPointerException("can't create LdifValue from null");
		}
		if (base64) {
			this.data = Base64.getDecoder().decode(value.trim());
			this.wasEncoded = true;
		} else {
			this.data = value.trim().getBytes();
			this.wasEncoded = false;
		}
	}



	public String getValue() {
		if (wasEncoded) {
			return new String(Base64.getEncoder().encode(data));
		} else {
			return new String(data);
		}
	}



	public String getUnprocessedValue() {
		return new String(data);
	}



	public String toUnprocessedString() {
		return this.getUnprocessedValue();
	}



	public String toString() {
		return this.getValue();
	}



	public boolean wasEncoded() {
		return this.wasEncoded;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + (wasEncoded ? 1231 : 1237);
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
		LdifValue other = (LdifValue) obj;	
		if (!Arrays.equals(data, other.data)) {
			return false;
		}
		if (wasEncoded != other.wasEncoded) {
			return false;
		}
		return true;
	}

}
