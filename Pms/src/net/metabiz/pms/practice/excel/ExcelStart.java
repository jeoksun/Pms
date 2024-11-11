package net.metabiz.pms.practice.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import net.metabiz.pms.practice.data.FileValidator;
import net.metabiz.pms.practice.data.PropertiesData;

public class ExcelStart {
    
    public ExcelStart(JTable table) throws Exception {
        exportExcelFile(table);
    }
    

    public static void exportExcelFile(JTable table) throws Exception {
        
        /**
         * ���丮 or ���� ����
         */
        if(!FileValidator.validateAndCreateDir()) {
            JOptionPane.showMessageDialog(null, "���丮 ���� ����");
            return;
        }
        
        if (!FileValidator.validateAndCreateFile()) {
            JOptionPane.showMessageDialog(null, "���� ���� ����");
            return;
        }

        /**
         * ���̺� �ִ� ������ �ϴ� ��������
         */
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("JTextField �Է°�");
        TableModel model = table.getModel();
        Row headerRow = sheet.createRow(0); // ù���� row�� header

        /**
         * ���̺� �÷��� ��������
         */
        for (int i = 0; i < model.getColumnCount(); i++) {
            Cell cel = headerRow.createCell(i);
            cel.setCellValue(model.getColumnName(i));
        }

        /**
         * ���̺� ������(header ������ row) ��������
         */
        for (int i = 0; i < model.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1); // ù��° row�� ����̴ϱ� +1���� ������ ����
            for (int j = 0; j < model.getColumnCount(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(model.getValueAt(i, j).toString());

            }

        }
        
        System.out.println(PropertiesData.exportPath);
        FileOutputStream export = new FileOutputStream(new File(PropertiesData.exportPath));
        wb.write(export);
        export.close();
        wb.close();
        
        
        
        

    }

}
