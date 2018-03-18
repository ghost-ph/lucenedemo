package com.patterncat.lucene.spider;

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

public class URLSeen {
    /**
     * @param args
     */
    public static void main(String[] args) {
        String dir = "C:/search/db/";
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(false);
        envConfig.setAllowCreate(true);

        Environment env = new Environment(new File(dir), envConfig);

        // use a generic database configuration
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(false);
        dbConfig.setAllowCreate(true);
        // catalog is needed for serial bindings (java serialization)
        Database catalogDb = env.openDatabase(null, "catalog", dbConfig);
        ClassCatalog catalog = new StoredClassCatalog(catalogDb);

        // use Integer tuple binding for key entries
        TupleBinding<String> keyBinding =
                TupleBinding.getPrimitiveBinding(String.class);

        // use String serial binding for data entries
        SerialBinding<String> dataBinding =
                new SerialBinding<String>(catalog, String.class);

        Database db = env.openDatabase(null, "url", dbConfig);
        // create a map view of the database
        SortedMap<String, String> map = new StoredSortedMap<String, String>
                (db, keyBinding, dataBinding, true);
        String url = "http://www.lietu.com";
        map.put(url, null);
        if(map.containsKey(url))
        {
            System.out.println("已抓取"+url);
        }
        System.out.println("所有已抓取的URL:");
        for(String seenUrl:map.keySet())
        {
            System.out.println("已抓取:"+seenUrl);
        }
        db.close();
        catalogDb.close();
        env.close();
    }
}

