package com.patterncat.lucene.basic;

public class TestString {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //char[] name = {'M','i','k','e'};
        String name = "Mike";
        for(int i=0;i<name.length();++i){
            System.out.println(name.charAt(i));
        }
        String words = "Mike say:\"hello\"";
        System.out.println(words);

        String dir = "c:\\windows\\";
        System.out.println(dir);

		/*int i= 10;
		String val = String.valueOf(i);*/

        String val = "100";
        int i = Integer.parseInt(val);
        System.out.println(i);
    }
}
