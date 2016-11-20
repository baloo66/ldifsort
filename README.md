# ldifsort

Jave based sort utility for ldif entry files ...

## NAME

ldifsort.jar - Sorts an LDIF file from standard input by the specified key
attribute. The sorted version is written to standard outpu. During sort,
all comments are removed.

## DESCRIPTION

Sort an LDIF file by the specified key attribute. All comments in the LDIF input will be removed.

## SYNOPSIS

`java -jar ldifsort.jar [-k keyattr] [-andch] [-l locale]`

**-k** : Specifies the key attribute for making sort comparisons. If omitted, sorting is done by the dn, which can be composed of different attributes for different entries.

**-a** : Specifies that attributes within an entry should also be sorted.

**-n** : Specifies numeric comparisons on the key attribute. Otherwise string comparisons are done.

**-d** : Specifies that the key attribute is a DN. Comparisons are done on a DN-normalized version of attribute values. This is the default behavior if 'dn' is passed as the argument to -k.

**-c** : Specifies case-insensitive comparisons on the key attribute. This is the default behavior if 'dn' is passed as the argument to -k.

**-h** : shows a short help

**-l** : Specifies the locale to use for comparison, eg. en_US or de_DE - if ommitted, the default locale of the JVM is used.

## AUTHOR

Alexander Eller
