package com.patterncat.lucene.classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * 分类器，调用getCategoryName方法实现分类
 * @author luogang
 *
 */
public class Classifier {
    private static Trie trie = new Trie();
    private static Classifier categoryTrie = null;

    private static String FILE_CATEGORY_PATH;

    // 获得实例
    public static Classifier getInstance() {
        if (categoryTrie == null)
            categoryTrie = new Classifier();
        return categoryTrie;
    }

    static { // 取得静态路径 只执行一次
        InputStream inputFile = null;
        Properties propertie = new Properties();
        try {
            inputFile = Classifier.class.getClassLoader()
                    .getResourceAsStream("conf.properties");
            propertie.load(inputFile);
            FILE_CATEGORY_PATH = propertie.getProperty("categories");
        } catch (FileNotFoundException ex) {
            System.out.println("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("装载文件--->失败!");
            ex.printStackTrace();
        } finally {
            try {
                if (inputFile != null)
                    inputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Classifier() { // 读取分类规则
        try {
            BufferedReader br = null;
            File f = new File(FILE_CATEGORY_PATH);
            if (!f.exists()) {
                return;
            }

            br = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = br.readLine()) != null) {
                String words[] = line.split(" ");
                if (words.length < 3) {
                    continue;
                }
                int index = Arrays
                        .binarySearch(Categories.catNames, words[1]);
                if (index < 0) {
                    System.out.print("分词规则写的不正确");
                    System.exit(-1);
                }
                int degree = Integer.parseInt(words[2]);
                trie.add(words[0], new WordRelation(words[0], words[1], degree,
                        index));
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception e) {
            categoryTrie = null;
        }
    }

    public String getCategoryName(String... articals) {
        int[] degrees = Categories.createDegress();
        for (String content : articals) {
            trie.analysisAll(content, degrees);
        }
        int index = 0;
        int degree = degrees[0];
        for (int i = 1; i < degrees.length; i++) {
            if (degree < degrees[i]) {
                degree = degrees[i];
                index = i;
            }
        }
        if (degree <= 0) {
            return "";
        }
        return Categories.catNames[index];
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Classifier c = Classifier.getInstance();
        String s[] = new String[2];
        s[0] = "adfasd sdf reader dsf sdaf,ds.asf,pageada This is a very lg,ood song, I often hear.sdfaq musicsdfaas music.dsaf dsa.asdf.sadff nice ,music nice df nicenice";
        s[1] = "adfasd sdf reader dsf sdaf,ds.asf,pageada This is a very lg,ood song, I often hear.sdfaq musicsdfaas music.dsaf dsa.asdf.sadff nice ,music nice df nicenice";
        String category = c.getCategoryName(s[0], s[1]);
        System.out.print(category);
    }

}
