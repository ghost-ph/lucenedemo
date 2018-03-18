package com.patterncat.lucene.bseg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AtomTernarySearchTrie {

    private static AtomTernarySearchTrie dic = null;
    /**
     *
     * @return the singleton of basic dictionary
     */
    public static AtomTernarySearchTrie getInstance()
    {
        if (dic == null)
        {
            dic = new AtomTernarySearchTrie("AtomWords.txt");
        }
        return dic;
    }

    public final class TSTNode {
        public String data=null;

        protected TSTNode loNode;
        protected TSTNode eqNode;
        protected TSTNode hiNode;

        public char splitChar;

        public TSTNode(char key) {
            this.splitChar = key;
        }

        public String toString(){
            return "data  是"+data+"   spliter是"+splitChar;
        }
    }

    public TSTNode rootNode;
    public long n = 0;//语料库中的词的总数

    public AtomTernarySearchTrie(String str) {
        try {
            FileReader filereadnew = new FileReader(str);
            BufferedReader read = new BufferedReader(filereadnew);
            String temstr = "";
            try {
                while ((temstr = read.readLine()) != null) {
                    TSTNode node = null;
                    if (rootNode == null) {
                        rootNode = new TSTNode(temstr.charAt(0));
                    }
                    int charIndex = 0;
                    TSTNode currentNode = rootNode;
                    while (true) {
                        if (currentNode == null) {
                            break;
                        }
                        int compa = (temstr.charAt(charIndex) - currentNode.splitChar);
                        if (compa == 0) {
                            charIndex++;
                            if (charIndex == temstr.length()) {
                                node = currentNode;
                                break;
                            }
                            currentNode = currentNode.eqNode;
                        } else if (compa < 0) {
                            currentNode = currentNode.loNode;
                        } else {
                            currentNode = currentNode.hiNode;
                        }
                    }
                    if (node == null) {
                        currentNode=creatTSTNode(temstr);
                        currentNode.data = temstr;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //创建一个结点
    public TSTNode creatTSTNode(String key)throws NullPointerException,IllegalArgumentException {
        if(key==null){
            throw new NullPointerException("空指针异常");
        }
        int charIndex = 0;
        TSTNode currentNode = rootNode;
        if (rootNode == null) {
            rootNode = new TSTNode(key.charAt(0));
        }
        while (true) {
            int compa = (key.charAt(charIndex) - currentNode.splitChar);
            if (compa == 0) {
                charIndex++;
                if (charIndex == key.length()) {
                    return currentNode;
                }
                if (currentNode.eqNode == null) {
                    currentNode.eqNode = new TSTNode(key.charAt(charIndex));
                }
                currentNode = currentNode.eqNode;
            } else if (compa < 0) {
                if (currentNode.loNode == null) {
                    currentNode.loNode = new TSTNode(key.charAt(charIndex));
                }
                currentNode = currentNode.loNode;
            } else {
                if (currentNode.hiNode == null) {
                    currentNode.hiNode = new TSTNode(key.charAt(charIndex));
                }
                currentNode = currentNode.hiNode;
            }
        }
    }

    public String matchLong(String key,int offset) {
        String ret = null;
        if (key == null || rootNode == null || "".equals(key)) {
            return ret;
        }
        TSTNode currentNode = rootNode;
        int charIndex = offset;
        while (true) {
            if (currentNode == null) {
                return ret;
            }
            int charComp = key.charAt(charIndex) - currentNode.splitChar;

            if (charComp == 0) {
                charIndex++;

                if(currentNode.data != null){
                    ret = currentNode.data; //候选最长匹配词
                }
                if (charIndex == key.length()) {
                    return ret; //已经匹配完
                }
                currentNode = currentNode.eqNode;
            } else if (charComp < 0) {
                currentNode = currentNode.loNode;
            } else {
                currentNode = currentNode.hiNode;
            }
        }
    }

    public int matchEnglish(int start, String sentence) {
        int i = start;
        for (; i < sentence.length();) {
            char c = sentence.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                ++i;
            } else {
                break;
            }
        }
        return i;
    }

    /**
     *
     * @param start 开始位置
     * @param key 关键词
     * @param length 长度
     * @return 返回第一个不是英文的位置
     */
    public int matchEnglish (int start, char[] key, int length)
    {
        int i = start;
        while(i<length)
        {
            char c = key[i];
            if( c>='a'&&c<='z' || c>='A'&&c<='Z' )
            {
                ++i;
            }
            else
            {
                break;
            }
        }

        return i;
    }

    public int matchNum(int start, String sentence) {
        int i = start;
        for (; i < sentence.length();) {
            char c = sentence.charAt(i);
            if (c >= '0' && c <= '9') {
                ++i;
            } else {
                break;
            }
        }
        return i;
    }

    public int matchNum (int start, char[] key, int length)
    {
        int i = start;
        while(i<length)
        {
            char c = key[i];
            if( c>='0'&&c<='9' )
            {
                ++i;
            }
            else
            {
                break;
            }
        }

        return i;
    }

    public static class PrefixRet {
        public ArrayList<String> values;
        public int end; //记录下次匹配的开始位置
    }

    //如果匹配上则返回true，否则返回false
    public String getMatch(String sentence, int offset) {
        String ret = "";
        if (sentence == null || rootNode == null || "".equals(sentence)) {
            System.out.println("ddd");
            return "";
        }
        int endIndex = matchEnglish(offset, sentence);
        if (endIndex != offset) {
            return sentence.substring(offset,endIndex);
        }

        endIndex = matchNum(offset, sentence);
        if (endIndex != offset) {
            return sentence.substring(offset,endIndex);
        }

        TSTNode currentNode = rootNode;
        int charIndex = offset;
        while (currentNode != null) {

            int charComp = sentence.charAt(charIndex) - currentNode.splitChar;
            if (charComp == 0) {
                charIndex++;
                if(currentNode.data != null){
                    ret = currentNode.data;
                }
                if (charIndex == sentence.length()) {
                    return ret;
                }
                currentNode = currentNode.eqNode;
            } else if (charComp < 0) {
                currentNode = currentNode.loNode;
            } else {
                currentNode = currentNode.hiNode;
            }
        }
        return ret;
    }

    public char[] getMatch(char[] sentence, int offset,int len) {
        char[] ret = null;
        if (sentence == null || rootNode == null || len==0) {
            //System.out.println("ddd");
            return ret;
        }
        int endIndex = matchEnglish(offset, sentence,len);
        if (endIndex != offset) {
            //english
            char[] english = new char[endIndex - offset];
            System.arraycopy(sentence, offset, english, 0, english.length);
            return english;
        }

        endIndex = matchNum(offset, sentence,len);
        if (endIndex != offset) {
            //number
            char[] num = new char[endIndex - offset];
            System.arraycopy(sentence, offset, num, 0, num.length);
            return num;
        }

        TSTNode currentNode = rootNode;
        int charIndex = offset;
        while (currentNode != null) {

            int charComp = sentence[charIndex] - currentNode.splitChar;
            if (charComp == 0) {
                charIndex++;
                if(currentNode.data != null){
                    ret = currentNode.data.toCharArray();
                }
                if (charIndex == len) {
                    return ret;
                }
                currentNode = currentNode.eqNode;
            } else if (charComp < 0) {
                currentNode = currentNode.loNode;
            } else {
                currentNode = currentNode.hiNode;
            }
        }
        return ret;
    }

}
