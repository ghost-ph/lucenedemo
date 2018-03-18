package com.patterncat.lucene.bdb;

import java.io.File;

import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class Berkeley {
    public static <EnvironmenConfig> void envconfig() {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(false);
        envConfig.setAllowCreate(true);
        File envDir = new File("d://");

        try {
            // 新建环境变量
            Environment exampleEnv = new Environment(envDir, envConfig);
            String databaseName = "ToDoTaskList.db";
            DatabaseConfig dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            dbConfig.setTransactional(false);
            // 打开用来存储类信息的数据库
            // 用来存储类信息的数据库数据库，不要求能共存储重复的关键字
            dbConfig.setSortedDuplicates(false);

            Database myClassDb = exampleEnv.openDatabase(null, databaseName,
                    dbConfig);
            // 初始化catalog类
            StoredClassCatalog catalog = new StoredClassCatalog(myClassDb);
            TupleBinding<String> keyBinding = TupleBinding
                    .getPrimitiveBinding(String.class);
            // 把value作为对象的序列化方式存储
            SerialBinding<String> valueBinding = new SerialBinding<String>(
                    catalog, String.class);
            Database store = exampleEnv.openDatabase(null, databaseName,
                    dbConfig);
            // 建立数据存储映射
            StoredSortedMap<String, String> map = new StoredSortedMap<String, String>(
                    store, keyBinding, valueBinding, true);
            // 释放环境变量
            // exampleEnv.syncReplication();
            store.close();
            myClassDb.close();
            exampleEnv.close();
            exampleEnv = null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        envconfig();
    }
}
