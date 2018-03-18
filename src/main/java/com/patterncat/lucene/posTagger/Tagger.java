package com.patterncat.lucene.posTagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 词性标注类
 *
 * @author luogang
 *
 */
public class Tagger {
    private static Tagger dic = null;

    public static Tagger getInstance() {
        if (dic == null) {
            dic = new Tagger();
        }
        return dic;
    }

    /* 定义语料库的转移概率 */
    private int[][] transFreq = new int[PartOfSpeech.names.length][PartOfSpeech.names.length];
    // 每个词性的频次
    private int[] typeFreq = new int[PartOfSpeech.names.length];
    private int totalFreq; // 所有词的总频次

    /**
     *
     * @param curState
     *            前一个词性
     * @param toTranState
     *            后一个词性
     * @return
     */
    public double getTransProb(byte curState, byte toTranState) {
        return Math.log((0.9 * transFreq[curState][toTranState]
                / typeFreq[curState] + 0.1 * typeFreq[curState] / totalFreq));
    }

    /* 加载语料库 */
    public Tagger(){
        try {
            URI uri = Tagger.class.getClass().getResource(
                    "/com/lietu/bigramSeg/POSTransFreq.txt").toURI();
            InputStream file = new FileInputStream(new File(uri));

            BufferedReader read = new BufferedReader(new InputStreamReader(
                    file, "GBK"));

            String line = null;

            while ((line = read.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ":");

                int pre = PartOfSpeech.values.get(st.nextToken());
                int next = PartOfSpeech.values.get(st.nextToken());
                int frq = Integer.parseInt(st.nextToken());
                transFreq[pre][next] = frq;
                typeFreq[next] += frq;
                totalFreq += frq;
                if (pre == 0) {
                    typeFreq[0] += frq;
                }
                // System.out.println(pre+" : "+ next + ":" +
                // transFreq[pre][next]);
            }

            // for(int i=0;i<typeFreq.length;++i){
            // System.out.println(PartOfSpeech.names[i]+" "+typeFreq[i]);
            // }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static final double minValue = -1000000000.0 / 2.0;

    /**
     * 标注估计词性(隐马尔科夫模型,viterbi算法)
     *
     * @param observations
     *            要标注词性的结合
     * @return 估计的标注类型序列
     */
    public byte[] viterbi(ArrayList<WordTokenInf> observations) {
        // 增加start和end节点
        WordTypes startType = new WordTypes(1);
        startType.insert(0, PartOfSpeech.start, 1);
        observations.add(0, new WordTokenInf(-1, 0, "Start", startType));

        WordTypes endType = new WordTypes(1);
        endType.insert(0, PartOfSpeech.end, 100);
        observations.add(new WordTokenInf(-1, 0, "End", endType));

        // 初始化各个节点的隐状态,每个节点都有WordTypes.values().length种隐状态
        // 形成了一个row=stageLength,line=WordTypes.values().length的二维数组
        int stageLength = observations.size();
        double[][] prob = new double[stageLength][PartOfSpeech.names.length];
        for (int i = 0; i < stageLength; ++i) {
            // prob[i] = new double[WordType.values().length];
            for (int j = 0; j < PartOfSpeech.names.length; ++j){
                prob[i][j] = minValue;
                //System.out.println(prob[i][j]);
            }
        }

        // 在隐马尔科夫模型中,每个隐状态都有一个最佳前驱,此二维数组用来储存每个状态的最佳前驱
        byte[][] bestPre = new byte[stageLength][PartOfSpeech.names.length];
        for (int i = 0; i < stageLength; ++i) {
            for (int j = 0; j < PartOfSpeech.names.length; ++j)
                bestPre[i][j] = PartOfSpeech.unknow;
        }

        prob[0][PartOfSpeech.start] = 0;

        // viterbi算法计算最佳前驱

        // 遍历每一个观察值
        for (int stage = 1; stage < stageLength; stage++) {
            WordTokenInf nexInf = observations.get(stage);
            if (nexInf.data == null) {
                continue;
            }

			/* 遍历当前观察值所对应的每一个状态(词性) */
            for (int nextIndex = 0; nextIndex < nexInf.data.keys.length; ++nextIndex) {
                WordTokenInf preInf = observations.get(stage - 1);
                if (preInf.data == null) {
                    continue;
                }

                // 发射概率 = 中国作为名词出现的频率 / 名词在语料库中出现的频率 (正确)
                // 发射概率 = 中国作为名词出现的频率 / 中国出现的频率 (错误)
                byte nextPOS = nexInf.data.keys[nextIndex].pos;
                double weight = nexInf.data.vals[nextIndex];
                if(weight==0){
                    weight = 0.00001;
                }
                double emiprob = Math.log(weight / typeFreq[nextPOS]);

				/* 遍历能够到达当前观察值当前状态的上一时刻的每一个状态 */
                for (int prevIndex = 0; prevIndex < preInf.data.keys.length; ++prevIndex) {
                    // WordTypeInf preTypeInf = preIt.next();
                    byte prevPOS = preInf.data.keys[prevIndex].pos;
                    double transprob = this.getTransProb(prevPOS, nextPOS); // 转移概率的对数
                    double preProb = prob[stage - 1][prevPOS]; // 前驱最佳概率
                    // System.out.println("transprob " + transprob
                    // +" preProb"+preProb +" emiprob"+emiprob);
					/* log(前驱最佳概率) + log(发射概率) + log(转移概率) */
                    double currentprob = preProb + transprob + emiprob;

                    //System.out.println("currentprob " + currentprob + " " +
                    // prob[stage][nextPOS]);
                    if (prob[stage][nextPOS] < currentprob) { // 计算最佳前驱
                        prob[stage][nextPOS] = currentprob;
                        bestPre[stage][nextPOS] = prevPOS;
                    }
                }
            }
        }

        // 逆向求解路径
        byte currentTag = PartOfSpeech.end;
        byte[] bestTag = new byte[stageLength]; //存放最佳词性标注序列结果
        for (int i = (stageLength - 1); i > 1; i--) {
            bestTag[i - 1] = bestPre[i][currentTag]; //最佳前驱节点对应的词性
            // System.out.print(bestTag[i - 1] + " ");
            currentTag = bestTag[i - 1];
        }

        // 构造返回结果
        byte[] resultTag = new byte[stageLength - 2];
        System.arraycopy(bestTag, 1, resultTag, 0, resultTag.length);

        // 将ret之前添加的start和end节点去除
        observations.remove(stageLength - 1);
        observations.remove(0);

        return resultTag;
    }
}
