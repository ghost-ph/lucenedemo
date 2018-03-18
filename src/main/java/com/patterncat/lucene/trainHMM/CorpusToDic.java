package com.patterncat.lucene.trainHMM;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CorpusToDic {
    /*
     * creat hashmap conllection use to contain words part of speech
     */
    HashMap<String,Word> map = new HashMap<String,Word>(); //存储词和对应的词性信息,
    int[][] transMatrix = new int[46][46]; //转移频率计数矩阵
    CountPOS posSumCount=new CountPOS();
    public static void main(String[] args) {
        CorpusToDic corpus = new CorpusToDic();
        corpus.corpusModel();
        corpus.test();
    }

    public void test(){
        String testWord = "成果";
        System.out.println(testWord+" 的词频率：\n"+this.getWord(testWord));

        System.out.println("词性的总频率：\n"+posSumCount);
        System.out.println (testWord+" 发射概率：\n"+this.getWord(testWord).getFireProbability(posSumCount));

        System.out.println("转移频率计数取值测试：\n "+this.getTransMatrix("n","w"));

        this.printTransMatrix();
    }


    /*
     * 词性标注转换成数字
     */
    public static int getPOSId(String word) {
        word = word.toLowerCase();
        if (word.equals("a")) {
            return 0;
        } else if (word.equals("ad")) {
            return 1;
        } else if (word.equals("ag")) {
            return 2;
        } else if (word.equals("an")) {
            return 3;
        } else if (word.equals("b")) {
            return 4;
        } else if (word.equals("c")) {
            return 5;
        } else if (word.equals("d")) {
            return 6;
        } else if (word.equals("dg")) {
            return 7;
        } else if (word.equals("e")) {
            return 8;
        } else if (word.equals("f")) {
            return 9;
        } else if (word.equals("g")) {
            return 10;
        } else if (word.equals("h")) {
            return 11;
        } else if (word.equals("i")) {
            return 12;
        } else if (word.equals("j")) {
            return 13;
        } else if (word.equals("k")) {
            return 14;
        } else if (word.equals("l")) {
            return 15;
        } else if (word.equals("m")) {
            return 16;
        } else if (word.equals("n")) {
            return 17;
        } else if (word.equals("ng")) {
            return 18;
        } else if (word.equals("nr")) {
            return 19;
        } else if (word.equals("ns")) {
            return 20;
        } else if (word.equals("nt")) {
            return 21;
        } else if (word.equals("nx")) {
            return 22;
        } else if (word.equals("nz")) {
            return 23;
        } else if (word.equals("o")) {
            return 24;
        } else if (word.equals("p")) {
            return 25;
        } else if (word.equals("q")) {
            return 26;
        } else if (word.equals("r")) {
            return 27;
        } else if (word.equals("s")) {
            return 28;
        } else if (word.equals("t")) {
            return 29;
        } else if (word.equals("tg")) {
            return 30;
        } else if (word.equals("u")) {
            return 31;
        } else if (word.equals("ud")) {
            return 32;
        } else if (word.equals("ug")) {
            return 33;
        } else if (word.equals("uj")) {
            return 34;
        } else if (word.equals("ul")) {
            return 35;
        } else if (word.equals("uv")) {
            return 36;
        } else if (word.equals("uz")) {
            return 37;
        } else if (word.equals("v")) {
            return 38;
        } else if (word.equals("vd")) {
            return 39;
        } else if (word.equals("vg")) {
            return 40;
        } else if (word.equals("vn")) {
            return 41;
        } else if (word.equals("w")) {
            return 42;
        } else if (word.equals("x")) {
            return 43;
        } else if (word.equals("y")) {
            return 44;
        } else if (word.equals("z")) {
            return 45;
        } else {
            //System.out.println("error :"+word);
            return -1;
        }
    }

    public static String getPOSFromId(int id) {
        if (id==0) {
            return "a";
        } else if (id==1) {
            return "ad";
        } else if (id == 2) {
            return "ag";
        } else if (id ==3) {
            return "an";
        } else if (id==4) {
            return "b";
        } else if (id==5) {
            return "c";
        } else if (id==6) {
            return "d";
        } else if (id==7) {
            return "dg";
        } else if (id==8) {
            return "e";
        } else if (id==9) {
            return "f";
        } else if (id==10) {
            return "g";
        } else if (id==11) {
            return "h";
        } else if (id==12) {
            return "i";
        } else if (id==13) {
            return "j";
        } else if (id==14) {
            return "k";
        } else if (id==15) {
            return ("l");
        } else if (id==16) {
            return ("m");
        } else if (id==17) {
            return ("n");
        } else if (id==18) {
            return ("ng");
        } else if (id==19) {
            return ("nr");
        } else if (id==20) {
            return ("ns");
        } else if (id==21) {
            return ("nt");
        } else if (id==22) {
            return ("nx");
        } else if (id==23) {
            return ("nz");
        } else if (id==24) {
            return ("o");
        } else if (id==25) {
            return ("p");
        } else if (id==26) {
            return ("q");
        } else if (id==27) {
            return ("r");
        } else if (id==28) {
            return ("s");
        } else if (id==29) {
            return ("t");
        } else if (id==30) {
            return ("tg");
        } else if (id==31) {
            return ("u");
        } else if (id==32) {
            return ("ud");
        } else if (id==33) {
            return ("ug");
        } else if (id==34) {
            return ("uj");
        } else if (id==35) {
            return ("ul");
        } else if (id==36) {
            return ("uv");
        } else if (id==37) {
            return ("uz");
        } else if (id==38) {
            return ("v");
        } else if (id==39) {
            return ("vd");
        } else if (id==40) {
            return ("vg");
        } else if (id==41) {
            return ("vn");
        } else if (id==42) {
            return ("w");
        } else if (id==43) {
            return ("x");
        } else if (id==44) {
            return ("y");
        } else if (id==45) {
            return ("z");
        } else {
            //System.out.println("error :"+word);
            return "";
        }
    }
    /*
     * get matrix values
     */
    public int getTransMatrix(String a,String b){
        return transMatrix[getPOSId(a)][getPOSId(b)];
    }

    public void initMatrix() {
        for (int i = 0; i < 46; i++) {
            for (int j = 0; j < 46; j++) {
                transMatrix[i][j] = 0;
            }
        }
    }

    public void printTransMatrix() {

        for (int i = 0; i < 46; i++) {

            for (int j = 0; j < 46; j++) {
                System.out.print(transMatrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public Word getWord(String temp){
        return map.get(temp);
    }

    /*
     * add part of speed to word
     */
    public void addWordSpeed(String[] a) {
        if (!map.containsKey(a[0])) {
            Word word=new Word();
            //a. get b. set
            word.addFreq(a[1]);
            //	System.out.println("观察对象是否有值 : " + word);
            map.put(a[0], word);
            //testHashMap();
        } else {
            Word word=map.get(a[0]);
            word.addFreq(a[1]);
        }
    }

    /*
     * 测试hashmap
     */
    public void testHashMap(){
        for(Object m: map.entrySet()){
            System.out.println("dd : " + m);
        }
    }

    /*
     * add word speed to matrix
     */
    public void addWordPOSToMatrix(int pre,int next) {
        if (pre >=0 && next >=0) {
            transMatrix[pre][next] = transMatrix[pre][next] + 1;
        }
    }

    public void corpusModel() {
        try {
            FileReader in = new FileReader("199801.txt");
            BufferedReader buffer = new BufferedReader(in);
            String line;
            int linecount=0;
            try {
                while ((line = buffer.readLine()) != null) {
                    linecount++;
                    if(line.equals(""))
                        continue;
                    String[] wordAndPOS = line.split("  ");
                    countPOS(wordAndPOS);
                    // 对Hash的操作
                    splitWord(wordAndPOS);
                    // 用于对矩阵的操作
                    addTransMatrix(wordAndPOS);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void countPOS(String[] temp){
        for (int i = 0; i < temp.length; i++) {
            String[] a = temp[i].split("/");
            posSumCount.addFreq(getPOSId(a[1]));
        }
    }

    public void splitWord(String[] temp) {
        for (int i = 0; i < temp.length; i++) {
            String[] a = temp[i].split("/");
            addWordSpeed(a);
        }
    }

    public void addTransMatrix(String[] wordAndPOS) {
        for (int i = 0; i < wordAndPOS.length - 1; i++) {
            int j = i + 1;
            String[] a = wordAndPOS[i].split("/");
            String[] b = wordAndPOS[j].split("/");
            int pre = getPOSId(a[1]);//前一个词性ID
            int next = getPOSId(b[1]);//后一个词性ID
            addWordPOSToMatrix(pre,next);
        }
    }
}
