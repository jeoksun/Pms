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
         * 디렉토리 or 파일 검증
         */
        if(!FileValidator.validateAndCreateDir()) {
            JOptionPane.showMessageDialog(null, "디렉토리 생성 실패");
            return;
        }
        
        if (!FileValidator.validateAndCreateFile()) {
            JOptionPane.showMessageDialog(null, "파일 생성 실패");
            return;
        }

        /**
         * 테이블에 있는 데이터 일단 가져오기
         */
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("JTextField 입력값");
        TableModel model = table.getModel();
        Row headerRow = sheet.createRow(0); // 첫번쨰 row는 header

        /**
         * 테이블 컬럼명 가져오기
         */
        for (int i = 0; i < model.getColumnCount(); i++) {
            Cell cel = headerRow.createCell(i);
            cel.setCellValue(model.getColumnName(i));
        }

        /**
         * 테이블 데이터(header 제외한 row) 가져오기
         */
        for (int i = 0; i < model.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1); // 첫번째 row가 헤더이니까 +1부터 데이터 시작
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
