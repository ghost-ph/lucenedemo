package com.patterncat.lucene.htmlParser;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionAccess {
    public static void main(String[] args) {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); // 动态加载和创建Driver对象
            String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=d:\\db1.mdb";// 创建，并初始化数据库url
            Connection con = DriverManager.getConnection(url); // 创建，并初始化数据库连接con
            if (con != null) // 查看连接是否成功
            {
                System.out.println("数据库连接成功");
            }
        } catch (Exception e) // 捕获异常
        {
            System.out.println("数据库连接失败");
            e.printStackTrace(); // 输出异常信息
        }
    }
}
