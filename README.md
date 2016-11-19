# ldifsort

## NAME

ldifsort.jar - Sorts an LDIF file from standard input by the specified key
attribute. The sorted version is written to standard outpu.

## DESCRIPTION

Sort an LDIF file by the specified key attribute.

## SYNOPSIS

`java -jar ldifsort.jar **-k** **keyattr** [**-andc**]`

-k

    Specifies the key attribute for making sort comparisons. If 'dn' is specified, sorting is done by the full DN string, which can be composed of different attributes for different entries.
-a

    Specifies that attributes within a given entry should also be sorted. This has the side effect of removing all comments and line continuations in the LDIF file.
-n

    Specifies numeric comparisons on the key attribute. Otherwise string comparisons are done.
-d

    Specifies that the key attribute is a DN. Comparisons are done on a DN-normalized version of attribute values. This is the default behavior if 'dn' is passed as the argument to -k.
-c

    Specifies case-insensitive comparisons on the key attribute. This is the default behavior if 'dn' is passed as the argument to -k.

## AUTHOR

Alexander Eller
