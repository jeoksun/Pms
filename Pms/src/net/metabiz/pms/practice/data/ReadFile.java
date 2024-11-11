package net.metabiz.pms.practice.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReadFile extends FileReader {
    private static final String filePath = "/Pms/file/pathInfo.properties";
    
    private static final File file = new File(filePath);
    
    public ReadFile(File file) throws FileNotFoundException {
        super(file);
        
    }

    

}
