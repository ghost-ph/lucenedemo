package com.patterncat.lucene.bdb;

import java.io.File;
import java.util.SortedMap;

import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * 存储URL的数据库
 *
 * @author luogang
 *
 */
public class URLDB {
    Environment env;
    public SortedMap<String, String> map;
    Database db;
    Database catalogDb;

    public URLDB(){
        String dir = "./db/";
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(false);
        envConfig.setAllowCreate(true);

        env = new Environment(new File(dir), envConfig);

        //使用一个通用的数据库配置
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(false);

        dbConfig.setAllowCreate(true); //允许创建新的数据库文件
        //如果有序列化绑定则需要类别数据库
        catalogDb = env.openDatabase(null, "catalog", dbConfig);
        ClassCatalog catalog = new StoredClassCatalog(catalogDb);

        //关键字数据类型是字符串
        TupleBinding<String> keyBinding =
                TupleBinding.getPrimitiveBinding(String.class);

        //值数据类型也是字符串
        SerialBinding<String> dataBinding =
                new SerialBinding<String>(catalog, String.class);

        db = env.openDatabase(null, "url", dbConfig);
        map = new StoredSortedMap<String, String>
                (db, keyBinding, dataBinding, true);
    }

    public void close(){
        db.close();//关闭存储数据库
        catalogDb.close();//关闭元数据库


        env.sync(); //同步内存中的数据到硬盘
        env.close(); //关闭环境变量
        env = null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String dir = "./db/";
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(false);
        envConfig.setAllowCreate(true);

        Environment env = new Environment(new File(dir), envConfig);

        //使用一个通用的数据库配置
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(false);

        dbConfig.setAllowCreate(true);
        //如果有序列化绑定则需要类别数据库
        Database catalogDb = env.openDatabase(null, "catalog", dbConfig);
        ClassCatalog catalog = new StoredClassCatalog(catalogDb);

        //关键字数据类型是字符串
        TupleBinding<String> keyBinding =
                TupleBinding.getPrimitiveBinding(String.class);

        //值数据类型也是字符串
        SerialBinding<String> dataBinding =
                new SerialBinding<String>(catalog, String.class);

        Database db = env.openDatabase(null, "url", dbConfig);

        //创建一个映射
        SortedMap<String, String> map = new StoredSortedMap<String, String>
                (db, keyBinding, dataBinding, true);
        //把抓取过的URL地址作为关键字放入映射
        String url = "http://www.lietu.com";
        map.put(url, url);
        if(map.containsKey(url)){
            System.out.println("已抓取");
        }
    }

}
