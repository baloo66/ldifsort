package de.ellersoftware.tools.ldifsort;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.ellersoftware.tools.ldifsort.comparators.LdifAttrValueComparator;

public class ldifsort {

	/*
	 * ldifsort -k dn [-andc] < infile > outfile
	 *
	 * -k -> attribute to sort (Spezialfall: dn)
	 * -a attribute im entry mitsortieren
	 * -n key-Attribut wird numerisch verglichen
	 * -d key-Attribut ist dn -> Vergleich wird auf DN-normalisierte Form durchgeführt (automatisch bei -k dn)
	 * -c case-insensitive (automatisch bei -k dn)
	 * -l -> locale to use for sort (default: default-locale)
	 */
	public static void main(String[] args) {

		Options options = createCmdLineOptions();

		CommandLine cmdLine = parseCommandLine(args, options);
		if (cmdLine == null) {
			return;
		}

		String sortKey = cmdLine.getOptionValue('k');
		String sortLocaleString = cmdLine.getOptionValue('l');

		boolean sortAttributes = cmdLine.hasOption('a');

		boolean sortNumeric = cmdLine.hasOption('n');
		boolean sortAsDN = cmdLine.hasOption('d');
		boolean sortNoCase = cmdLine.hasOption('c');

		if (sortKey == null) {
			sortKey = "dn";
		}

		if (sortKey.equalsIgnoreCase("dn")) {
			sortKey = sortKey.toLowerCase();
			sortAsDN = true;
			sortNumeric = false;
			sortNoCase = true;
		}

		Locale sortLocale = Locale.getDefault();
		if (sortLocaleString != null) {
			Locale[] allLocales = Locale.getAvailableLocales();
			for (Locale locale : allLocales) {
				if (sortLocaleString.equals(locale.toString())) {
					sortLocale = locale;
				}
			}
		}

		if (sortAsDN) {
			sortNumeric = false;
			sortNoCase = true;
		}

		if (sortNumeric) {
			sortAsDN = false;
			sortNoCase = false;
		}

		if (sortNoCase) {
			sortNumeric = false;
		}

		LdifReader reader = new LdifReader(new BufferedReader(new InputStreamReader(System.in)));
		reader.parse();

		reader.sortOnAttribute(sortKey, sortAsDN, sortNoCase, sortNumeric, sortLocale);

		List<LdifEntry> entries = reader.getEntries();
		if (entries.size() > 0) {
			outputVersion(System.out);
			for (LdifEntry entry : entries) {
				outputEntry(System.out, entry, sortAttributes);
			}
		}
	}

	private static void outputEntry(PrintStream printStream, LdifEntry entry, boolean sortAttributes) {
		printStream.println();
		printStream.println(entry.getDn().toString());
		List<LdifAttrValue> attrValues = entry.getData();
		if (sortAttributes) {
			Collections.sort(attrValues, new LdifAttrValueComparator());
		}
		for (LdifAttrValue attrValue : attrValues) {
			printStream.println(attrValue.getLine());
		}
	}

	private static void outputVersion(PrintStream printStream) {
		printStream.println("version: 1");
	}

	private static CommandLine parseCommandLine(String[] args, Options options) {
		CommandLineParser cmdLineParser = new DefaultParser();
		CommandLine cmdLine = null;
		try {
			cmdLine = cmdLineParser.parse(options, args);
			if (cmdLine.hasOption('h')) {
				System.out.println("\n--------+++++### ldifsort - (c) 2016 by Alexander Eller Softwareloesungen ###+++++--------\n");
				new HelpFormatter().printHelp("ldifsort [-k <attribute>] [-andch] [-l locale]", options);
				System.out.println("\n--------");
				return null;
			}
		} catch (ParseException pvException) {
			System.out.println("\n--------+++++### ldifsort - (c) 2016 by Alexander Eller Softwareloesungen ###+++++--------\n\n");
			System.out.println("ERROR parsing command line: " + pvException.getMessage());
			System.out.println();
			new HelpFormatter().printHelp("ldifsort [-k <attribute>] [-andch] [-l locale]", options);
			System.out.println("\n--------");
			return null;
		}
		return cmdLine;
	}

	private static Options createCmdLineOptions() {
		Options options = new Options();

		options.addOption(Option.builder("h")
				                .longOpt("help")
				                .desc("show this help")
				                .build());
		options.addOption(Option.builder("k")
				                .longOpt("key")
				                .numberOfArgs(1)
				                .optionalArg(false)
				                .desc("key attribute - if ommitted, dn is used")
				                .build());
		options.addOption(Option.builder("l")
                                .longOpt("locale")
                                .numberOfArgs(1)
                                .optionalArg(false)
                                .desc("locale to use, eg. en_US or de_DE - if ommitted, default locale (" + Locale.getDefault() + ") is used")
                                .build());
		options.addOption(Option.builder("a")
				                .longOpt("sort-attr")
				                .desc("sort attributes within entry")
				                .build());
		options.addOption(Option.builder("n")
				                .longOpt("sort-num")
				                .desc("sort key attribute as numeric - default is alpha")
				                .build());
		options.addOption(Option.builder("d")
				                .longOpt("is-dn")
				                .desc("sort key attribute as dn - activated for key dn")
				                .build());
		options.addOption(Option.builder("c")
				                .longOpt("sort-nocase")
				                .desc("sort key attribute case insensitive - activated for key dn")
				                .build());
		return options;
	}

}
