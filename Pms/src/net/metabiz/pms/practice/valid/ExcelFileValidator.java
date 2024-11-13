package net.metabiz.pms.practice.valid;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.metabiz.pms.practice.data.PropertiesData;

/**
 * Properties file을 읽고 파일 존재
 * 여부 확인 객체
 */
public class ExcelFileValidator {
    
    /**
       디렉토리 존재 여부 확인 후 없으면 생성 
     * @throws Exception 
     */
    public static boolean validateAndCreateDir() throws Exception {
        PropertiesData.readProperties();
        
        String[] exportPathS = PropertiesData.proPath.split("/");
        File file = new File(exportPathS[0]);
        
        if(!file.exists()) {
            boolean dirCreated = file.mkdir();
            if(dirCreated) {
              JOptionPane.showMessageDialog(null, "디렉토리가 존재하지않아 생성합니다");
              return true;
            }else {
                JOptionPane.showMessageDialog(null, "디렉토리 생성 실패");
                return false;
            }
        }
        return true;
        
        
    }
    
    
    /**
    파일이 존재 여부 확인 후 파일이 없으면 생성 
  */
 public static boolean validateAndCreateFile() throws Exception {
     PropertiesData.readProperties();
     System.out.println(PropertiesData.exportPath);
     File file = new File(PropertiesData.exportPath);
     
     if(!file.exists()) {
         boolean fileCreate = file.createNewFile();
         if(fileCreate) {
             JOptionPane.showMessageDialog(null, "파일이 없어 생성합니다");
             return true;
         }else {
             JOptionPane.showMessageDialog(null, "파일 생성 실패 ");
             return false;
         }
         
     }
     return true;
     
 }

    
}
