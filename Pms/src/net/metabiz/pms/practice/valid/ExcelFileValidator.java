package net.metabiz.pms.practice.valid;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.metabiz.pms.practice.data.PropertiesData;

/**
 * Properties file�� �а� ���� ����
 * ���� Ȯ�� ��ü
 */
public class ExcelFileValidator {
    
    /**
       ���丮 ���� ���� Ȯ�� �� ������ ���� 
     * @throws Exception 
     */
    public static boolean validateAndCreateDir() throws Exception {
        PropertiesData.readProperties();
        
        String[] exportPathS = PropertiesData.proPath.split("/");
        File file = new File(exportPathS[0]);
        
        if(!file.exists()) {
            boolean dirCreated = file.mkdir();
            if(dirCreated) {
              JOptionPane.showMessageDialog(null, "���丮�� ���������ʾ� �����մϴ�");
              return true;
            }else {
                JOptionPane.showMessageDialog(null, "���丮 ���� ����");
                return false;
            }
        }
        return true;
        
        
    }
    
    
    /**
    ������ ���� ���� Ȯ�� �� ������ ������ ���� 
  */
 public static boolean validateAndCreateFile() throws Exception {
     PropertiesData.readProperties();
     System.out.println(PropertiesData.exportPath);
     File file = new File(PropertiesData.exportPath);
     
     if(!file.exists()) {
         boolean fileCreate = file.createNewFile();
         if(fileCreate) {
             JOptionPane.showMessageDialog(null, "������ ���� �����մϴ�");
             return true;
         }else {
             JOptionPane.showMessageDialog(null, "���� ���� ���� ");
             return false;
         }
         
     }
     return true;
     
 }

    
}
