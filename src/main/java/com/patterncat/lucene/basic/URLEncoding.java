package com.patterncat.lucene.basic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URLEncoding {

    /**
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        String input = "%D6%D0%B9%FA";//"%B0%AE%B5%CFsad%C9%FA&*"; // "%B0%A1"
        String codingName = getURLEncoding(input);
        System.out.println(codingName);
        System.out.println(URLDecoder.decode(input, codingName));
    }

    public static String getURLEncoding(String url) {
        List<String> codes = new ArrayList<String>();
        CharType currentSate = null;
        char c1='\0';
        char c2='\0';
        for(int i=0; i<url.length(); ++i) {
            char currentChar = url.charAt(i);
            if(currentChar == '%') {
                if(currentSate == CharType.Code2 )
                {
                    char[] s1 = {c1,c2};
                    codes.add(new String(s1));
                }
                currentSate = CharType.Enter;
            }else if(currentSate==CharType.Enter)
            {
                c1 = currentChar;
                currentSate = CharType.Code1;
            }else if(currentSate==CharType.Code1)
            {
                c2 = currentChar;
                currentSate = CharType.Code2;
            }else if(currentSate==CharType.Code2)
            {
                char[] s1 = {c1,c2};
                codes.add(new String(s1));
                currentSate = null;
                return getEncodeByList(codes);
            }
        }
        if(currentSate==CharType.Code2)
        {
            char[] s1 = {c1,c2};
            codes.add(new String(s1));
        }
        return getEncodeByList(codes);
    }

    public static String getEncodeByList(List<String> code) {
        //for(String s : code)
        //{
        //	System.out.println(s);
        //}
        if(code.size() >= 2 && code.size()%2 == 1 && code.size()%3 == 0) {
            return "utf8";
        }
        else if(code.size() >= 2 && code.size()%2 == 0 && code.size()%3 != 0) {
            return "gbk";
        }
        else if(code.size()%6 == 0) {
            for(int m=0;m<code.size();m = m+6) {
                if(isUtf8(code.get(m), code.get(m+1), code.get(m+2))==false && isGbk(code.get(m), code.get(m+1))==true && isGbk(code.get(m+2), code.get(m+3))==true) {
                    return "gbk";
                } else if(isUtf8(code.get(m), code.get(m+1), code.get(m+2))==true && isGbk(code.get(m), code.get(m+1))==false) {
                    return "utf8";
                }
                if(isUtf8(code.get(m+3), code.get(m+4), code.get(m+5))==false && isGbk(code.get(m+2), code.get(m+3))==true && isGbk(code.get(m+4), code.get(m+5))==true) {
                    return "gbk";
                } else if(isUtf8(code.get(m+3), code.get(m+4), code.get(m+5))==true && isGbk(code.get(m+2), code.get(m+3))==false) {
                    return "utf8";
                }
            }
        }
        return "utf8";
    }

    public static String getEncode(String url) {
        String[] str = url.split("%");
        if(str != null) {
            List<String> code = new ArrayList<String>();
            for(int i=1;i<str.length; i++) {
                System.out.println(str[i]+"---");
                if(str[i].length() > 2) {
                    str[i] = str[i].substring(0,2);
                }
                System.out.println(str[i]);
                if(str[i].compareTo("80")>=0) {
                    code.add(str[i]);
                }
            }
            return getEncodeByList(code);
        }
        return "utf8";
    }

    public static String getEncoding(String url) {
        List<String> code = new ArrayList<String>();
        int flag = 3;
        if(url.length()>=3) {
            for(int i=3; i<url.length(); i=flag) {
                if(url.charAt(i-3) == '%'&& (url.charAt(i) == '%') && (url.charAt(i-2) == '8'||url.charAt(i-2) == '9'||(url.charAt(i-2)>='A'&&url.charAt(i-2)<='F'))
                        && ((url.charAt(i-1) >= '0'&&url.charAt(i-1) <= '9')||(url.charAt(i-1)>='A'&&url.charAt(i-1)<='F'))) {
                    char[] c1 = {url.charAt(i-2),url.charAt(i-1)};
                    //System.out.println(new String(c1));
                    code.add(new String(c1));
                    flag = flag+3;
                } else if(url.charAt(i-3) == '%'&& url.charAt(i) != '%' && (url.charAt(i-2) == '8'||url.charAt(i-2) == '9'||(url.charAt(i-2)>='A'&&url.charAt(i-2)<='F'))
                        && ((url.charAt(i-1) >= '0'&&url.charAt(i-1) <= '9')||(url.charAt(i-1)>='A'&&url.charAt(i-1)<='F'))) {
                    char[] c1 = {url.charAt(i-2),url.charAt(i-1)};
                    //System.out.println(new String(c1));
                    code.add(new String(c1));
                    return getEncodeByList(code);
                } else {
                    flag++;
                }
                if(flag == url.length()) {
                    char[] c1 = {url.charAt(flag-2),url.charAt(flag-1)};
                    //System.out.println(new String(c1));
                    code.add(new String(c1));
                }
            }
        }
        return getEncodeByList(code);
    }


    public static boolean isUtf8(String code1,String code2,String code3) {
        if (code1.compareTo("E4") >= 0 && code1.compareTo("E9") <= 0 &&
                code2.compareTo("80") >= 0 && code2.compareTo("BF") <= 0 &&
                code3.compareTo("80")>=0 &&code3.compareTo("BF")<=0) {
            return true;
        }
        return false;
    }

    public static boolean isGbk(String code1,String code2) {

        if (code1.compareTo("B0") >= 0 && code1.compareTo("F7") <= 0 &&
                code2.compareTo("A0")>=0 &&code2.compareTo("FF")<=0) {
            return true;
        }
        return false;
    }
}
