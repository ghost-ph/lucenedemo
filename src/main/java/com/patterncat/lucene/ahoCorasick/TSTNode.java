package com.patterncat.lucene.ahoCorasick;

import java.util.LinkedList;

public class TSTNode {

    protected TSTNode loNode; //左边节点

    protected TSTNode eqNode; //中间节点

    protected TSTNode hiNode; //右边节点

    protected TSTNode ptNode; //相对父节点

    /**  节点的值  */
    public POSInf data=null;// data属性可以存储词原文和词性、词频等相关的信息

    protected char splitchar; //本节点表示的字符

    protected TSTNode _failure;//匹配失败后跳转的节点

    private LinkedList<String> _results;//存储模式串的数组变量

    public LinkedList<String> getResult(){
        return this._results;
    }

    //将模式串中不在_results中的模式串添加进来
    public void addResult(String result) {
        if (_results.contains(result))//如果已经包含该模式则不增加到模式串中
            return;
        _results.add(result);
    }

    public String getPath()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.splitchar);
        TSTNode parent = ptNode;
        while (parent!=null)
        {
            sb.append(parent.splitchar);
            parent = parent.ptNode;
        }
        return sb.reverse().toString();
    }

	/*public String getPath()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.splitchar);
		TSTNode parent = ptNode;
		TSTNode child = this;
		if(parent==null)
		{
			return String.valueOf(this.splitchar);
		}
		while (true)
		{
			//System.out.println("splitchar:"+parent.splitchar);
			if(parent.eqNode==child)
			{
				sb.append(parent.splitchar);
			}
			parent = parent.ptNode;
			child = parent;
			if(parent==null)
			{
				break;
			}
		}
		return sb.reverse().toString();
	}*/

    /**
     *  构造方法
     *
     *@param  splitchar  该节点表示的字符
     */
    protected TSTNode(char splitchar, TSTNode parent) {
        this.splitchar = splitchar;
        this.ptNode = parent;
        _results = new LinkedList<String>();//存储所有没有重复的模式串的数组
    }
    public String toString()
    {
        return "splitchar:"+ splitchar;
    }
}
