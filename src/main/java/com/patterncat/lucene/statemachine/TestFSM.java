package com.patterncat.lucene.statemachine;

import java.util.ArrayList;

public class TestFSM {

    public static int matchDigital(int start, String sentence) {
        int i = start;
        int count = sentence.length();
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

    public static void toEvent(String s)
    {
        for(int i=0; i<s.length();)
        {
            int ret = matchDigital(i,s);
            int diff = ret - i;

            InputEvent event = null;
            if(diff==4)
            {
                event = InputEvent.yearNum;
                i=ret;
            }
            else if(diff==2)
            {
                event = InputEvent.digital2;
                i=ret;
            }
            else if(diff==1)
            {
                event = InputEvent.digital1;
                i=ret;
            }
            else if(s.charAt(i)=='年')
            {
                event = InputEvent.yearUnitEvent;
                ++i;
            }
            else if(s.charAt(i)=='月')
            {
                event = InputEvent.monthUnitEvent;
                ++i;
            }
            else if(s.charAt(i)=='日')
            {
                event = InputEvent.dayUnitEvent;
                ++i;
            }
            else if(s.charAt(i)=='-')
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if(s.charAt(i)=='－')
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if (s.charAt(i) == '时') {
                event = InputEvent.timeUnitEvent;
                ++i;
            } else if (s.charAt(i) == '分') {
                event = InputEvent.minuteUnitEvent;
                ++i;
            } else if (s.charAt(i) == '秒') {
                event = InputEvent.secondUnitEvent;
                ++i;
            } else if (s.charAt(i) == ':') {
                event = InputEvent.colonEvent;
                ++i;
            } else if (s.charAt(i) == '：') {
                event = InputEvent.colonEvent;
                ++i;
            } else if (s.charAt(i) == ' ') {
                System.out.println("blankEvent");
                event = InputEvent.blankEvent;
                ++i;
            }

            if(event == null)
            {
                return;
            }

            System.out.println("event:"+event);
        }
    }

    public static boolean isDate(String s)
    {
        FSMDate fsm = FSMDate.getInstance();
        ArrayList<Integer> matchPos = new ArrayList<Integer>();

        for(int i=0; i<s.length(); )
        {
            int ret = matchDigital(i,s);
            int diff = ret - i;

            InputEvent event = null;
            if(diff==4)
            {
                event = InputEvent.yearNum;
                i=ret;
            }
            else if(diff==2)
            {
                event = InputEvent.digital2;
                i=ret;
            }
            else if(diff==1)
            {
                event = InputEvent.digital1;
                i=ret;
            }
            else if(s.charAt(i)=='年')
            {
                event = InputEvent.yearUnitEvent;
                ++i;
            }
            else if(s.charAt(i)=='月')
            {
                event = InputEvent.monthUnitEvent;
                ++i;
            }
            else if(s.charAt(i)=='日')
            {
                event = InputEvent.dayUnitEvent;
                ++i;
            }
            else if(s.charAt(i)=='-')
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if(s.charAt(i)=='－')
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if (s.charAt(i) == '时') {
                event = InputEvent.timeUnitEvent;
                ++i;
            } else if (s.charAt(i) == '分') {
                event = InputEvent.minuteUnitEvent;
                ++i;
            } else if (s.charAt(i) == '秒') {
                event = InputEvent.secondUnitEvent;
                ++i;
            } else if (s.charAt(i) == ':') {
                event = InputEvent.colonEvent;
                ++i;
            } else if (s.charAt(i) == '：') {
                event = InputEvent.colonEvent;
                ++i;
            } else if (s.charAt(i) == ' ') {
                System.out.println("blankEvent");
                event = InputEvent.blankEvent;
                ++i;
            }

            if(event == null)
            {
                return false;
            }

            System.out.println("event:"+event);
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

    //把输入串转换成有限状态机可以接收的事件
    public static int matchDate(String s, int start)
    {
        FSMDate fsm = FSMDate.getInstance();
        int matchPos = start;

        for(int i=start; i<s.length(); )
        {
            int ret = matchDigital(i,s);
            int diff = ret - i;

            InputEvent event = null;
            if(diff==4)
            {
                event = InputEvent.yearNum;
                i=ret;
            }
            else if(diff==2)
            {
                event = InputEvent.digital2;
                i=ret;
            }
            else if(diff==1)
            {
                event = InputEvent.digital1;
                i=ret;
            }
            else if(s.charAt(i)=='年')
            {
                event = InputEvent.yearUnitEvent;
                ++i;
            }
            else if(s.charAt(i)=='月')
            {
                event = InputEvent.monthUnitEvent;
                ++i;
            }
            else if(s.charAt(i)=='日')
            {
                event = InputEvent.dayUnitEvent;
                ++i;
            }
            else if(s.charAt(i)=='-')
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if(s.charAt(i)=='－')
            {
                event = InputEvent.splitEvent;
                ++i;
            }
            else if (s.charAt(i) == '时') {
                event = InputEvent.timeUnitEvent;
                ++i;
            } else if (s.charAt(i) == '分') {
                event = InputEvent.minuteUnitEvent;
                ++i;
            } else if (s.charAt(i) == '秒') {
                event = InputEvent.secondUnitEvent;
                ++i;
            } else if (s.charAt(i) == ':') {
                event = InputEvent.colonEvent;
                ++i;
            } else if (s.charAt(i) == '：') {
                event = InputEvent.colonEvent;
                ++i;
            } else if (s.charAt(i) == ' ') {
                System.out.println("blankEvent");
                event = InputEvent.blankEvent;
                ++i;
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

    public static String getDate(String news)
    {
        for(int start = 0;start<news.length();)
        {
            int datePos = matchDate(news,start);
            if(datePos>start )
            {
                String splitString = news.substring(start,datePos);
                return splitString;
            }
            else
            {
                ++start;
            }
        }
        return null;
    }

    ///////////////////////////////////////
    public static void main(String[] args) throws Exception {
        String s = "2007-10-30 20:44";
        //"2009年05月08日 12:23";
        //"2009-05月08日";
        //"2009-05-08日";
        //"2009－5-8";
        //"2009－5月8";
        //toEvent(s);
        //System.out.println(isDate(s));
        System.out.println(getDate(s));
    }
}

