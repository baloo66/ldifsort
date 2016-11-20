package de.ellersoftware.tools.ldifsort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.ellersoftware.tools.ldifsort.comparators.LdifEntryComparator;

public class LdifReader {

	private BufferedReader reader;
	private int version = -1;
	private static final String DN = "dn: ";
	private static final String VERSION = "version: ";
	private static final int DEFAULT_VERSION = 1;
	private List<LdifEntry> entries;



	public LdifReader(Reader in) {
		initReader(new BufferedReader(in));
	}

	public LdifReader(InputStream in) {
		initReader(new BufferedReader(new InputStreamReader(in, Charset.defaultCharset())));
	}

	public LdifReader(String ldifFileName) {
		this(new File(ldifFileName));
	}

	public LdifReader(File file) {
		if (!file.exists()) {
			String msg = String.format("can't find file [%s]", file.getAbsoluteFile());
			throw new IllegalArgumentException(msg);
		}
		if (!file.canRead()) {
			String msg = String.format("can't read file [%s]", file.getAbsoluteFile());
			throw new IllegalStateException(msg);
		}
		try {
			initReader(new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset())));
		} catch (FileNotFoundException fnfe) {
			String msg = String.format("can't find file [%s]", file.getAbsoluteFile());
			throw new IllegalStateException(msg);
		}
	}



	public void parse() {
		this.entries.clear();
		String readedLine;
		LdifEntry curEntry = null;
		String dataLine = null;
		try {
			while ((readedLine = reader.readLine()) != null) {
				if (readedLine.length() == 0) {
					if (dataLine != null) {
						if (!(dataLine.startsWith(DN))) {
							curEntry.addAttribute(new LdifAttrValue(dataLine.toString()));
						}
						this.entries.add(curEntry);
						curEntry = null;
						dataLine = null;
					}
				} else {
					if (readedLine.charAt(0) == '#') {
						// comments are not relevant for sort - especially they can't be assigned to an entry, so forget ...
					} else if (readedLine.startsWith(VERSION)) {
						// only version 1 is one time allowed ...
						if (version != -1) {
							throw new IllegalStateException("the version line is only allowed one time ...");
						} else {
							version = Integer.parseInt(readedLine.substring(VERSION.length()).trim());
							if (version != DEFAULT_VERSION) {
								throw new IllegalStateException("only version " + DEFAULT_VERSION + " supported ...");
							}
						}
					} else if (readedLine.charAt(0) == ' ') {
						dataLine = dataLine + readedLine.substring(1);
					} else {
						if (dataLine != null) {
							if (dataLine.startsWith(DN)) {
								curEntry = new LdifEntry(dataLine);
							} else {
								curEntry.addAttribute(new LdifAttrValue(dataLine));
							}
							dataLine = null;
						}
						dataLine = readedLine.trim();
					}
				}
			}
			if (dataLine != null) {
				if (!(dataLine.startsWith(DN))) {
					curEntry.addAttribute(new LdifAttrValue(dataLine.toString()));
				}
				this.entries.add(curEntry);
				curEntry = null;
				dataLine = null;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	private void initReader(BufferedReader newReader) {
		this.version = -1;
		this.entries = new ArrayList<LdifEntry>();
		this.reader = newReader;
	}



	public List<LdifEntry> getEntries() {
		return this.entries;
	}



	public void sortOnAttribute(String sortKey, boolean sortAsDN, boolean sortNoCase, boolean sortNumeric, Locale sortLocale) {
		if ((this.entries == null) || (this.entries.size() <= 1)) {
			return;
		}
		this.entries.sort(new LdifEntryComparator(sortKey, sortAsDN, sortNoCase, sortNumeric, sortLocale));
	}
}
