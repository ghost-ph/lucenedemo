package com.patterncat.lucene.index;

/**
 * BuildSettings is a class to support {@link BuildMP}. It takes the
 * command-line arguments and parses them into static members. The command-line
 * arguments are proccessed like so:
 * <P>
 * There will be a number of options followed by a list of files. The list of
 * files to process must be at the end of the argument list. The command-line
 * options are specified by preceding a predefined string with a dash. For
 * example, the way to specify the number of threads is by using the switch
 * <I>-t</I>. The <I>-t</I> switch in particular must have an integer value as
 * the next argument.
 * <P>
 * Each parameter also has a default value. The default inverted index filename,
 * for example, is "inverted.index"
 * <P>
 * The predefined switches are:<BR>
 * <I>-t</I>: Specify the number of threads. Next argument must be an integer.<BR>
 * <I>-n</I>: Use a fixed number of terms from each document. Next argument must
 * be an integer.<BR>
 * <I>-p</I>: Use a probability cutoff to select terms from each document. Next
 * argument must be a decimal.<BR>
 * <I>-ii</I>: Specify the inverted index filename. Next argument must be the
 * filename.<BR>
 * <I>-di</I>: Specify the document index filename. Next argument must be the
 * filename.<BR>
 * <I>-append</I>: Indices will be loaded and added to if this switch is
 * present.<BR>
 * <I>-sw</I>: Specify a file containing stop words, one per line. Next argument
 * must be the filename.<BR>
 * <I>-cs</I>: Specify a file containing case-sensitive substitutions, one per
 * line. The word to be replaced must be the the first word on the line. The
 * replacement must follow the word by a space. Next argument must be the
 * filename.<BR>
 */
public class BuildSettings {
    // all settings are defined here

}
