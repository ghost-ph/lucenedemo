package com.patterncat.lucene.htmlParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestReadData {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Connection conn = null;
        try {
            String strurl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=D:/lg/work/dokechi/db1.mdb";
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            conn = DriverManager.getConnection(strurl);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from ALLCATALOG_SOLR ");
            while (rs.next()) {
                long id = rs.getLong("ID");
                System.out.println(id);
                String content = rs.getString("ProductName");
                System.out.println(content);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
