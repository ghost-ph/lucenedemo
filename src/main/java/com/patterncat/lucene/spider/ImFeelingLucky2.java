package com.patterncat.lucene.spider;

import java.net.*;
import java.io.*;

public class ImFeelingLucky2 {
    public static void main(String[] args) {
        try {
			/*String req = "http://www.google.com/search?" + "q="
					+ URLEncoder.encode("W3C", "UTF8") + "&" + "btnI="
					+ URLEncoder.encode("I'm Feeling Lucky", "UTF8");*/
            String req = "http://www.google.com.hk/url?sa=p&hl=zh-CN&pref=hkredirect&pval=yes&q=http://www.google.com.hk/search%3Fq%3DW3C%26btnI%3DI'm%2BFeeling%2BLucky&ust=1336443760491212&usg=AFQjCNF32goj_iLJBXRMART1RJm4hGDZ7w";
            HttpURLConnection con = (HttpURLConnection) (new URL(req))
                    .openConnection();
            con.setRequestProperty("User-Agent", "IXWT");
            con.setInstanceFollowRedirects(false);
            String loc = con.getHeaderField("Location");
            System.out.print("The prophet spoke thus: ");
            if (loc != null)
                System.out.println("Direct your browser to " + loc
                        + " and you shall find great happiness in life.");
            else
                System.out.println("I am sorry - my crystal ball is blank.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
