package net.metabiz.pms.practice.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import net.metabiz.pms.practice.data.PropertiesData;
import net.metabiz.pms.practice.data.TableData;
import net.metabiz.pms.practice.valid.ExcelFileValidator;

public class ExcelExportHandler {
    
    public ExcelExportHandler(JTable table) throws Exception {
        exportExcelFile(table);
    }
    
    public static void exportExcelFile(JTable table) throws Exception {

        /**
         * 디렉토리 or 파일 검증
         */
        if (!ExcelFileValidator.validateAndCreateDir()) {
            JOptionPane.showMessageDialog(null, "디렉토리 생성 실패");
            return;
        }
        if (!ExcelFileValidator.validateAndCreateFile()) {
            JOptionPane.showMessageDialog(null, "파일 생성 실패");
            return;
        }

        /**
         * 테이블에 있는 데이터 일단 가져오기
         */
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("JTextField 입력값");
        TableModel model = table.getModel();//Table에서 Model 가져오기
        Row headerRow = sheet.createRow(0); // 첫 번째 행은 헤더

        int columnCount = model.getColumnCount();
        int excludedColumnIndex = 0;        // 체크박스 열은 제외할 변수 
        int columnIndex = 0;

        /**
         * 체크박스열 엑셀 export시 제외
         */
        for (int i = 0; i < columnCount; i++) {
            if (i == excludedColumnIndex) {  // 체크박스 컬럼은 제외
                continue;
            }
            Cell cel = headerRow.createCell(columnIndex++);
            cel.setCellValue(model.getColumnName(i));
        }

        /**
         * 테이블 데이터(header 제외한 row) 가져오기 (체크박스 컬럼 제외)
         * 필터링된 데이터만 가져오기
         */
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();

        // sorter가 null인 경우를 처리
        boolean isFiltered = (sorter != null && sorter.getRowFilter() != null);
        
        FileOutputStream export = null;

//        try (FileOutputStream export = new FileOutputStream(new File(PropertiesData.exportPath))) {
        try {
            export = new FileOutputStream(new File(PropertiesData.exportPath));
            int rowNum = 1; // 데이터는 첫 번째 행부터 시작

            for (int i = 0; i < model.getRowCount(); i++) {
                int viewRow;
                if (isFiltered) {
                    // 조회 필터가 적용된경우에만 적용
                    viewRow = sorter.convertRowIndexToView(i);
                    if (viewRow == -1) {
                        continue; // 필터링되어 표시되지 않는 행은 무시
                    }
                } else {
                    // 필터가 적용되지않은 경우!
                    viewRow = i;
                }

                Row row = sheet.createRow(rowNum++); // 첫 번째 행은 헤더니까 +1부터 데이터 시작
                columnIndex = 0;
                for (int j = 0; j < columnCount; j++) {
                    if (j == excludedColumnIndex) { // 체크박스 컬럼은 제외
                        continue;
                    }
                    Cell cell = row.createCell(columnIndex++);
                    if (model.getValueAt(viewRow, j) != null) {
                        cell.setCellValue(model.getValueAt(viewRow, j).toString()); // 테이블에서 값 가져오기
                    } else {
                        cell.setCellValue(""); // 값이 없으면 빈 문자열
                    }
                }
            }

            // 엑셀 파일 쓰기
            wb.write(export);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage());
        } finally {
            wb.close();
            export.close();
            JOptionPane.showMessageDialog(null, "Export success!");
        }
    }
}
