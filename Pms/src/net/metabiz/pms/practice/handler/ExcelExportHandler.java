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
         * 디렉토리, 파일 존재 여부 검증
         */
        if (!ExcelFileValidator.validateAndCreateDir()) {
            JOptionPane.showMessageDialog(null, "디렉토리 생성 실패");
            return;
        }
        if (!ExcelFileValidator.validateAndCreateFile()) {
            JOptionPane.showMessageDialog(null, "파일 생성 실패");
            return;
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("JTextField 입력값");
        TableModel model = table.getModel();
        Row headerRow = sheet.createRow(0);                 // 첫 번째 행은 헤더

        int columnCount = model.getColumnCount();
        int excludedColumnIndex = 0; 
        int columnIndex = 0;

        // 체크박스 열을 제외한 헤더를 엑셀에 입력
        for (int i = 0; i < columnCount; i++) {
            if (i == excludedColumnIndex) { // 체크박스 컬럼은 제외
                continue;
            }
            Cell cell = headerRow.createCell(columnIndex++);
            cell.setCellValue(model.getColumnName(i));
        }

        // 필터링된 데이터만 추출하기 위해 TableRowSorter 사용
        TableRowSorter<TableModel> sorter =  (TableRowSorter<TableModel>) table.getRowSorter();

        // 필터가 적용된 경우, 필터링된 데이터만 내보내기
        boolean isFiltered = (sorter != null && sorter.getRowFilter() != null);
        FileOutputStream export = null;

        try {
            export = new FileOutputStream(new File(PropertiesData.exportPath));
            int rowNum = 1; // 데이터는 두 번째 행부터 시작

            // 필터링된 데이터만 내보내기 (필터된 행의 원본 인덱스만 추출)
            for (int i = 0; i < model.getRowCount(); i++) {
                // 필터가 적용된 경우, 필터링된 행만 내보내기
                if (isFiltered) {
                    int viewRow = sorter.convertRowIndexToView(i); // 원본 모델의 index 그러니까 조회가된 로우의 인덱스들
                    if (viewRow == -1) {
                        continue; // 필터링된 행은 엑셀로 내보내지 않음
                    }

                    // 모델 인덱스로 변경
                    int modelRow = sorter.convertRowIndexToModel(viewRow); // 필터링된 행의 모델 인덱스
                    Row row = sheet.createRow(rowNum++);
                    columnIndex = 0;
                    for (int j = 0; j < columnCount; j++) {
                        if (j == excludedColumnIndex) { // 체크박스 컬럼은 제외
                            continue;
                        }
                        Cell cell = row.createCell(columnIndex++);
                        if (model.getValueAt(modelRow, j) != null) {
                            cell.setCellValue(model.getValueAt(modelRow, j).toString()); 
                        } else {
                            cell.setCellValue(""); 
                        }
                    }
                } else {
                    // 필터가 없으면 모든 행을 가져오기
                    Row row = sheet.createRow(rowNum++);
                    columnIndex = 0;
                    for (int j = 0; j < columnCount; j++) {
                        if (j == excludedColumnIndex) { // 체크박스 컬럼은 제외
                            continue;
                        }
                        Cell cell = row.createCell(columnIndex++);
                        if (model.getValueAt(i, j) != null) {
                            cell.setCellValue(model.getValueAt(i, j).toString()); 
                        } else {
                            cell.setCellValue(""); 
                        }
                    }
                }
            }

            // 엑셀 파일에 데이터를 기록
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
