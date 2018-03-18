package com.patterncat.lucene.statemachine;

import java.util.ArrayList;

public class TelMatcher {

    public static int matchDigital(int start, String sentence) {
        int i = start;
        int count = sentence.length();
        //判断是否是数字
        while (i < count) {
            char c = sentence.charAt(i);
            if (c >= '0' && c <= '9' || c >= '０' && c <= '９') {
                ++i;
            } else {
                break;
            }
        }
        return i;
    }

    //把输入串转换成有限状态机可以接收的事件
    public static int matchTel(String s, int start)
    {
        FSMTel fsm = FSMTel.getInstance();
        int matchPos = start;

        for(int i=start; i<s.length(); )
        {
            int ret = matchDigital(i,s);
            //System.out.println(ret);
            int diff = ret - i;

            InputEvent event = null;
            //System.out.println(s.charAt(i));

            if(diff==1) //一位数字
            {
                event=InputEvent.codeOne;
                i=ret;
            }
            else if(diff==2)
            {
                event=InputEvent.phoneNum2;
                i=ret;
            }
            else if (diff==3)//代码内容
            {
                event=InputEvent.code;
                i=ret;
            }
            else if (diff==4) //号码四位
            {
                event=InputEvent.phoneNumber;
                i=ret;
            }
            else if (diff==5) //号码五位
            {
                event=InputEvent.phoneNum5;
                i=ret;
            }
            else if (diff==6)//6位
            {
                event=InputEvent.phoneNum6;
                i=ret;
            }
            else if (diff==7)//7位
            {
                event=InputEvent.phoneNum7;
                i=ret;
            }
            else if(s.charAt(i)==')')//代码结束
            {
                //System.out.println(s.charAt(i));
                event=InputEvent.codeEnd;
                ++i;
            }
            else if(s.charAt(i)=='）')//代码结束
            {
                //System.out.println(s.charAt(i));
                event=InputEvent.codeEnd;
                ++i;
            }else if(s.charAt(i)=='(')//半码分隔符
            {
                event=InputEvent.codeStart;
                ++i;
            }
            else if(s.charAt(i)=='（')//全码分隔符
            {
                event=InputEvent.codeStart;
                ++i;
            }
            else if(s.charAt(i)=='/')//半码分隔符
            {
                event=InputEvent.splitEventx;
                ++i;
            }
            else if(s.charAt(i)=='／')//全码分隔符
            {
                event=InputEvent.splitEventx;
                ++i;
            }
            else if(s.charAt(i)=='-')//半码分隔符
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if(s.charAt(i)=='－')//全码分隔符
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if (s.charAt(i)=='.')//半码分隔符
            {
                event=InputEvent.splitEventD;
                ++i;
            }
            else if (s.charAt(i)=='．')//全码分隔符
            {
                event=InputEvent.splitEventD;
                ++i;
            }
            else if (s.charAt(i)=='+')//半码分隔符
            {
                event=InputEvent.splitEventJ;
                ++i;
            }
            else if (s.charAt(i)=='＋')//全码分隔符
            {
                event=InputEvent.splitEventJ;
                ++i;
            }
            else if (s.charAt(i) == ' ' || s.charAt(i) == '　')//空格
            {
                //System.out.println("blankEvent");
                event = InputEvent.blankEvent;
                ++i;
            }

            if(event == null)
            {
                return matchPos;
            }
            //接受的事件
            //			System.out.println("event "+event);
            MatchType matchRet = fsm.consumeEvent(event);
            //System.out.println(matchRet);
            //System.out.println("char:"+s.charAt(i));
            if(matchRet == MatchType.Match)
            {
                matchPos = i;
                //return true;
            }
            else if(matchRet == MatchType.MisMatch)
            {
                //return false;
                break;
            }
        }
        return matchPos;
    }

    public static boolean isTel(String s)
    {
        FSMTel fsm = FSMTel.getInstance();
        ArrayList<Integer> matchPos = new ArrayList<Integer>();
        //把输入串转换成有限状态机可以接收的事件
        for(int i=0; i<s.length(); )
        {
            int ret = matchDigital(i,s);
            int diff = ret - i;

            InputEvent event = null;
            //代码开始
            if(s.charAt(i)=='(')
            {
                event=InputEvent.codeStart;
                ++i;
            }
            else if (s.charAt(i)=='（')//全码
            {
                event=InputEvent.codeStart;
                ++i;
            }
            else if(s.charAt(i)=='/')//半码
            {
                event=InputEvent.splitEventx;
                ++i;
            }
            else if(s.charAt(i)=='／')//全码
            {
                event=InputEvent.splitEventx;
                ++i;
            }

            else if(s.charAt(i)==')')//代码结束
            {
                event=InputEvent.codeEnd;
                ++i;
            }
            else if(s.charAt(i)=='）')//全码结束
            {
                event=InputEvent.codeEnd;
                ++i;
            }

            else if(s.charAt(i)=='-')//半码分隔符
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if(s.charAt(i)=='－')//全码分隔符
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if (s.charAt(i)=='+')//半码分隔符
            {
                event=InputEvent.splitEventJ;
                ++i;
            }
            else if (s.charAt(i)=='＋')//全码分隔符
            {
                event=InputEvent.splitEventJ;
                ++i;
            }
            else if(diff==1) //一位数字
            {
                event=InputEvent.codeOne;
                i=ret;
            }
            else if(diff==2)
            {
                event=InputEvent.phoneNum2;
                i=ret;
            }
            else if (diff==3)//代码内容
            {
                event=InputEvent.code;
                i=ret;
            }
            else if (diff==4) //号码四位
            {
                event=InputEvent.phoneNumber;
                i=ret;
            }
            else if (diff==6)
            {
                event=InputEvent.phoneNum6;
                i=ret;
            }
            else if (diff==5) //号码四位
            {
                event=InputEvent.phoneNum5;
                i=ret;
            }
            else if (diff==7)
            {
                event=InputEvent.phoneNum7;
                i=ret;
            }
            else if (s.charAt(i) == ' ' || s.charAt(i) == '　')
            {
                //System.out.println("blankEvent");
                event = InputEvent.blankEvent;
                ++i;
            }

            if(event == null)
            {
                return false;
            }

            //接受事件
            //System.out.println(event);
            MatchType matchRet = fsm.consumeEvent(event);
            //System.out.println(matchRet);
            //System.out.println("char:"+s.charAt(i));
            if(matchRet == MatchType.Match)
            {
                matchPos.add(i);
                //return true;
            }
            else if(matchRet == MatchType.MisMatch)
            {
                //return false;
                break;
            }
        }

        if(matchPos.size()>0)
        {
            return true;
        }
        return false;
    }

    public static void main(String[] args){
        String s="007-812-1147670";
        //String s="12-45-45";
        System.out.println(isTel(s));
    }
}
