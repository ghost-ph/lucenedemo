package com.patterncat.lucene.enDep;

import java.util.ArrayList;
import java.util.HashMap;

//机器翻译中的英文依存树到中文依存树的转换
public class TreeConverter {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //分词得到的词序列
        ArrayList<Token> seg = new ArrayList<Token>();
        seg.add(new Token("一",0,1));
        seg.add(new Token("书",1,5));

        // a book 的 依存文法树
        // a -det-> book
        ArrayList<ArrayList<TreeNode>> depTree = new ArrayList<ArrayList<TreeNode>>();

        TreeNode a = new TreeNode(seg.get(0));
        TreeNode book = new TreeNode(seg.get(1));
        a.dominator = book; //“本”的支配词是“书”
        a.relation = GrammaticalRelation.DETERMINER;

        TreeNode root = book;

        ArrayList<TreeNode> firstLevel = new ArrayList<TreeNode>();
        firstLevel.add(a);
        firstLevel.add(book);
        depTree.add(firstLevel);

        //String sent= getSentence(root,depTree);
        //System.out.println(sent);

        convert(root,depTree);
        String sent=TestDep.getSentence(root,depTree);
        System.out.println(sent);
    }

    static HashMap<String,String> quantityMap = new HashMap<String,String>();

    //个 只 根 台 次 顶 笔 名 位 篇 份 所 头 点 件 种 块 本 张 间 支 门 辆 条 枚 家 场 付 颗 把 艘 朵 匹 则 堂 封 片 项 棵 部 座 群 批 架 幅 对 中 处 扇 盏 顿 出 首 堵 节 人 阵 卷 粒 股 样 堆 届 段 口 层 丝 撮 句 枝 课 发 杆 排 套 圈 面 挺 盒 双 列 天 轮
    //个 条 片 篇 曲 堵 只 把 根 道 朵 支 间 句 门 台 株 出 头 辆 架 座 棵 首 匹 幅 本 面 块 部 扇 件 处 粒 期 项 所 份 家 床 盏 位 身 杆 艘 副 顶 卷 具 轮 枝 枚 桩 点 尊 场 吨 列 爿 届 剂 栋 幢 种 员 口 则 页 滴 户 垛 毫 体 尾 公 队 起 针 着 套 幕 级 册 团 堂 对 丸 领 行 元 张 颗 封 节 盘 名 眼 宗 管 次 阵 顿
    static{
        quantityMap.put("书", "本");
        quantityMap.put("笔", "支");
        quantityMap.put("文章", "篇");
    }

    public static void convert(TreeNode root,ArrayList<ArrayList<TreeNode>> depTree){
        int level = 0;
        ArrayList<TreeNode> currentLevel = depTree.get(level);

        for(TreeNode n:currentLevel){
            if(n.relation == GrammaticalRelation.DETERMINER){
                //创建新节点
                TreeNode newNode = new TreeNode (quantityMap.get(n.dominator.term),3);
                //新节点在原有层
                newNode.dominator = n.dominator;
                int pos = currentLevel.indexOf(n);
                currentLevel.set(pos, newNode);

                //创建新的层
                ArrayList<TreeNode> newLevel = new ArrayList<TreeNode>();
                newNode.child = newNode.clone();
                n.dominator = newNode.child;
                newLevel.add(n);
                newLevel.add(newNode.child);
                depTree.add(newLevel);
            }
        }
    }

}