package com.patterncat.lucene.basic;

import java.io.FileWriter;
import java.io.IOException;

public class WriteTextFile {
    public static void main ( String[] args ) throws IOException
    {
        String fileName = "reaper.txt" ;

        FileWriter writer = new FileWriter( fileName );

        writer.write( "Behold her, single in the field,\n"  );
        writer.write( "Yon solitary Highland Lass!\n"  );
        writer.write( "Reaping and singing by herself;\n" );
        writer.write( "Stop here, or gently pass!\n"  );

        writer.close();
    }

}
