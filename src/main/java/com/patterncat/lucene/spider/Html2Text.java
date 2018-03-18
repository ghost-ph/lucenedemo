package com.patterncat.lucene.spider;

public class Html2Text {
    StringBuffer t = new StringBuffer();
    int offset =0;

    public String html2text(String text) {
        int start = findStart(text);
        if(start>offset)
        {
            t.append(text.substring(offset, start));
            ++offset;

            //if is number
            //get number
            //else
            //matchLongEscapTrie()
        }
        else
        {
            t.append(text.substring(offset));
        }
        return t.toString();
    }

    private int findStart(String text)
    {
        for (int i = offset; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '&') {
                return i;
            }
        }
        return 0;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Html2Text htmlConvert = new Html2Text();
        String content = "中国工人";
        content = htmlConvert.html2text(content);
        System.out.println(content);
    }

}

