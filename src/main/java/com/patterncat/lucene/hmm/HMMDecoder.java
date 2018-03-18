package com.patterncat.lucene.hmm;

import java.util.ArrayList;

/*description:HMM的解码类*/
public class HMMDecoder {
    ArrayList<State> states;// 所有可能的状态集{start，cow，duck，end}

    public ArrayList<Node>[] al;// 稀疏的二维矩阵节点 ，静态数组维表示显状态序列号，
    // 动态数组维表示隐状态的Node节点
    public ArrayList<Symbol> symbols;// 显状态序列

    public HMMDecoder() {
        states = new ArrayList<State>();
    }

    public void addStartState(State st) {
        states.add(st);
    }

    public void addNormalState(State st) {
        states.add(st);
    }

    public void addEndState(State st) {
        states.add(st);
    }

    // for debugging.
    public void show() {
        for (int i = 0; i < al.length; i++) {
            for (Node node : al[i]) {
                System.out.print(node.tag.name + " " + node.prob + " ");
            }
            System.out.println();
        }
    }

    // initialize for decoding
    public void initialize(ArrayList<Symbol> syms) {
        symbols = syms;
        // 初始化二维数组
        al = new ArrayList[symbols.size()];
        for (int i = 0; i < symbols.size(); i++) {
            al[i] = new ArrayList<Node>(states.size());
        }
        // 添加初始节点，即把start发射概率不为零的节点添加到al[0]中
        for (int j = 0; j < states.size(); j++) {
            double emitProb = states.get(j).emitprob(symbols.get(0));
            if (emitProb > 0) {
                System.out.println(states.get(j).name + " emit:"
                        + symbols.get(0).name + " " + emitProb);
                Node tmNode = new Node(states.get(j));
                tmNode.prob = emitProb;
                tmNode.bestPrev = null;
                al[0].add(tmNode);
            }
        }
        // 初试化其他节点
        for (int i = 1; i < symbols.size(); i++) {
            for (int j = 0; j < states.size(); j++) {
                double emitProb = states.get(j).emitprob(symbols.get(i));
                if (emitProb > 0) {
                    System.out.println(states.get(j).name + " emit:"
                            + symbols.get(i).name + " " + emitProb);
                    Node tmNode = new Node(states.get(j));
                    tmNode.prob = 0.0;
                    tmNode.bestPrev = new Node(states.get(0));
                    al[i].add(tmNode);
                }
            }
        }
    }

    // 前向累积过程
    public void proceed_decoding() {
        int stage, i0, i1;
        Symbol sym1;
        State s0;
        State s1;
        double prob = 0.0;
        double emitprob;
        show();

        // stage表示显状态序号，跳过start状态
        for (stage = 1; stage < al.length; stage++) {
            // i1表示显状态序号stage对应的隐状态节点维的序号
            for (i1 = 0; i1 < al[stage].size(); i1++) {
                // i0表示i1上一显状态维的隐状态节点编号
                for (i0 = 0; i0 < al[stage - 1].size(); i0++) {
                    System.out.println("i0:" + i0 + " i1:" + i1 + " stage:"
                            + stage);
                    sym1 = symbols.get(stage);
                    // 上一显状态维的隐状态节点
                    Node prevNode = al[stage - 1].get(i0);
                    // 当前显状态维的隐状态节点
                    Node nextNode = al[stage].get(i1);
                    s0 = prevNode.tag;// 上一显状态维的隐状态
                    s1 = nextNode.tag;// 当前显状态维的隐状态
                    // 上一显状态维的隐状态节点累积概率
                    prob = prevNode.prob;
                    // 上一显状态维的隐状态到当前显状态维的隐状态的转移概率
                    double transprob = s0.transprob(s1);

                    prob = prob * transprob;
                    emitprob = s1.emitprob(sym1);
                    System.out
                            .println("state:" + s1.name + " emit:" + emitprob);
                    // 这一步的累积概率是上一步的累积概率 * 上一个状态到这一个状态的转移概率 * 当前节点的发射概率
                    prob = prob * emitprob;
                    System.out.println("prob:" + prob);
                    // 找到当前节点的最大累积概率

                    if (nextNode.prob <= prob) {
                        // 记录当前节点的最大累积概率
                        nextNode.prob = prob;
                        // 记录当前节点的最佳前驱
                        nextNode.bestPrev = prevNode;
                    }

                }
            }
        }
    }

    // 反向回溯寻找最佳路径过程
    public ArrayList<Node> backward() {
        ArrayList<Node> maxNode = new ArrayList<Node>();
        Node endNode = al[symbols.size() - 1].get(0);// 结束阶段对应的最大隐状态
        for (Node i = endNode; i != null; i = i.bestPrev) {
            maxNode.add(i);// 被选中的隐状态加入到结果路径
        }
        return maxNode;
    }
}
