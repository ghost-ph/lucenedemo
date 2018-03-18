package com.patterncat.lucene.cnClassify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 中文文本分类，采用关键词加权分类方法
 * @author luogang
 */
public class Classifier {
    TernarySearchTrie dic;
    String[] categories;

    private Classifier() { // 读取分类规则
        String classfyRule = System.getProperty("user.dir")
                + "/dic/classfyrule.txt";
        String categoryFile = System.getProperty("user.dir")
                + "/dic/category.txt";

        categories = readcategory(categoryFile);

        dic = new TernarySearchTrie(classfyRule,
                categories);
    }

    private static Classifier categoryTrie = null;

    // 获得实例
    public static Classifier getInstance() {
        if (categoryTrie == null)
            categoryTrie = new Classifier();
        return categoryTrie;
    }

    public static String[] readcategory(String fileName) {
        InputStream file;
        try {
            file = new FileInputStream(new File(fileName));
            BufferedReader read = new BufferedReader(new InputStreamReader(
                    file, "GBK"));
            String line;
            Set<String> catset = new HashSet<String>();
            while ((line = read.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                catset.add(st.nextToken());
            }
            String[] category = catset.toArray(new String[catset.size()]);
            Arrays.sort(category);
            return category;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String getCategoryName(String sentence) {
        //用一个数组存储一个商品属于某一个类别的隶属度
        int degrees[] = new int[categories.length];
        //计算隶属度
        int offset = 0;
        for (offset = 0; offset < sentence.length(); offset++) {
            // System.err.println("offset="+offset);
            TernarySearchTrie.WordRelation wr = dic.matchLong(sentence, offset);
            if (wr == null) {
                continue;
            }
            for (int i = 0; i < wr.degree.length; i++) {
                // System.err.println(wr.degree[i]);
                degrees[i] += wr.degree[i];
            }
            // System.out.println(sentence+" match:"+ret);
        }

        // 商品归类到隶属度最大的类别
        int index = 0;
        int maxDegree = degrees[0];
        for (int i = 1; i < degrees.length; i++) {
            if (maxDegree < degrees[i]) {
                maxDegree = degrees[i];
                index = i;
            }
        }

        // System.out.println(categories[index]);
        return categories[index]; //返回最大隶属度对应的类别
    }

    public static void main(String[] args) {
        Classifier c = Classifier.getInstance();
        String category = c.getCategoryName("韩国料理");
        System.out.print(category);
    }
}

