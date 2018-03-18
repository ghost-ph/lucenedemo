package com.patterncat.lucene.probTagger;

public class POSNameMap {
    protected static short[] nHandleSet={1,4,24832,24932,24935,24942,25088,25344,25600,25703,25856,26112,26368,26624,26880,27136,27392,27648,27904,28160,28263,28274,28275,28276,28280,28282,28416,28672,28928,29184,29440,29696,29799,29952,30052,30055,30058,30060,30070,30074,30208,30308,30311,30318,30464,30720,30976,31232};
    //   "a", "ad", "ag", "an",  "b",  "c",  "d", "dg",  "e",  "f",  "g",  "h",  "i",  "j",  "k",  "l",  "m",  "n", "ng", "nr", "ns", "nt", "nx", "nz",  "o",  "p",  "q",  "r",  "s",  "t", "tg",  "u", "ud", "ug", "uj", "ul", "uv", "uz",  "v", "vd", "vg", "vn",  "w",  "x",  "y", "z"
    public static String[] sPOSRelated={"start","end","a", "ad", "ag", "an",  "b",  "c",  "d", "dg",  "e",  "f",  "g",  "h",  "i",  "j",  "k",  "l",  "m",  "n", "ng", "nr", "ns", "nt", "nx", "nz",  "o",  "p",  "q",  "r",  "s",  "t", "tg",  "u", "ud", "ug", "uj", "ul", "uv", "uz",  "v", "vd", "vg", "vn",  "w",  "x",  "y", "z"};
    //private static String[] sPOSRelated={"a", "ad", "ga", "an",  "f",  "c",  "d",  "d",  "e", "nd",  "g",  "h",  "i",  "j",  "k",  "l",  "m",  "n", "gn", "nh", "ns", "ni", "ws", "nz",  "o",  "p",  "q",  "r", "nl", "nt",  "gt", "u", "ud", "ug", "uj", "ul", "uv", "uz", "v",  "vd", "gv", "vn",  "w",  "x",  "u", "a"};
    public static String unknowPos = "@";

    public static String get(short n){
        int index = java.util.Arrays.binarySearch(nHandleSet,n);

        //System.out.println("index:"+index);
        if (index >=0)
        {
            return sPOSRelated[index];
        }
        return null;
    }

    /**
     *
     * @param POS of a word
     * @return return the code of the POS .
     */
    public static short getPOSCode(String POS)
    {
        int index = java.util.Arrays.binarySearch(sPOSRelated,POS );

        //System.out.println("index:"+index);
        if (index >=0 )
        {
            return nHandleSet[index];
        }
        return -1;
    }
}

