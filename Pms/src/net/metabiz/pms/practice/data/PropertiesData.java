package net.metabiz.pms.practice.data;

import java.io.FileReader;
import java.util.Properties;

/**
 *  Properties file을 읽고 해당 키값을 저장
 *  export할 경로 지정
 */
public class PropertiesData {

    public static Properties prop = null;
    public static String exportPath = "";              //file/excel.xlsx
    public static String proPath = "exportPath"; 
    
    
    public static void readProperties() throws Exception {
        prop = new Properties();
        prop.load(new FileReader("file/pathInfo.properties"));
        exportPath =getPathInfo(proPath);
        
    }
    
    public static String getPathInfo(String key) {
        if(prop !=null) {
            return prop.getProperty(key);
        }
        return null;
    }
    
    
}
