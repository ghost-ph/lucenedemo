package com.patterncat.lucene.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringTokenizer;

public class URLEncoding {

    /**
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        //char c = '\u9fa5';//'\u4e00';
        System.out.println('\u9fa5' - '\u4e00');
        int num = 0;
        for(char c = '\u4e00';c<='\u9fa5';++c)
        {
            ++num;
            if(num>1000)
            {
                break;
            }
            //System.out.println(c);
            System.out.println(URLEncoder.encode(""+c, "UTF-8"));
        }
		/*String input = "%E6%B5%B7%E6%8A%A5%E7%BD%91"; //"%B0%A1"
		String codingName=getEncoding(input);
		System.out.println(codingName);
		System.out.println(URLDecoder.decode(input, codingName));
		System.out.println(URLDecoder.decode(input, "utf-8"));*/
    }

    public static String getEncoding(String url) {
        StringTokenizer st = new StringTokenizer(url,"%");
        String code1 = st.nextToken();
        String code2 = st.nextToken();
        System.out.println(code1);
        if(code1.compareTo("B1")>=0 && code1.compareTo("F7")<=0)
        {
            return "GBK";
        }
        else if(code1.equals("B0")&&code2.compareTo("A1")>=0)
        {
            return "GBK";
        }
        System.out.println(st.nextToken());
        return "UTF-8";
    }
}
