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
         * ���丮, ���� ���� ���� ����
         */
        if (!ExcelFileValidator.validateAndCreateDir()) {
            JOptionPane.showMessageDialog(null, "���丮 ���� ����");
            return;
        }
        if (!ExcelFileValidator.validateAndCreateFile()) {
            JOptionPane.showMessageDialog(null, "���� ���� ����");
            return;
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("JTextField �Է°�");
        TableModel model = table.getModel();
        Row headerRow = sheet.createRow(0);                 // ù ��° ���� ���

        int columnCount = model.getColumnCount();
        int excludedColumnIndex = 0; 
        int columnIndex = 0;

        // üũ�ڽ� ���� ������ ����� ������ �Է�
        for (int i = 0; i < columnCount; i++) {
            if (i == excludedColumnIndex) { // üũ�ڽ� �÷��� ����
                continue;
            }
            Cell cell = headerRow.createCell(columnIndex++);
            cell.setCellValue(model.getColumnName(i));
        }

        // ���͸��� �����͸� �����ϱ� ���� TableRowSorter ���
        TableRowSorter<TableModel> sorter =  (TableRowSorter<TableModel>) table.getRowSorter();

        // ���Ͱ� ����� ���, ���͸��� �����͸� ��������
        boolean isFiltered = (sorter != null && sorter.getRowFilter() != null);
        FileOutputStream export = null;

        try {
            export = new FileOutputStream(new File(PropertiesData.exportPath));
            int rowNum = 1; // �����ʹ� �� ��° ����� ����

            // ���͸��� �����͸� �������� (���͵� ���� ���� �ε����� ����)
            for (int i = 0; i < model.getRowCount(); i++) {
                // ���Ͱ� ����� ���, ���͸��� �ุ ��������
                if (isFiltered) {
                    int viewRow = sorter.convertRowIndexToView(i); // ���� ���� index �׷��ϱ� ��ȸ���� �ο��� �ε�����
                    if (viewRow == -1) {
                        continue; // ���͸��� ���� ������ �������� ����
                    }

                    // �� �ε����� ����
                    int modelRow = sorter.convertRowIndexToModel(viewRow); // ���͸��� ���� �� �ε���
                    Row row = sheet.createRow(rowNum++);
                    columnIndex = 0;
                    for (int j = 0; j < columnCount; j++) {
                        if (j == excludedColumnIndex) { // üũ�ڽ� �÷��� ����
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
                    // ���Ͱ� ������ ��� ���� ��������
                    Row row = sheet.createRow(rowNum++);
                    columnIndex = 0;
                    for (int j = 0; j < columnCount; j++) {
                        if (j == excludedColumnIndex) { // üũ�ڽ� �÷��� ����
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

            // ���� ���Ͽ� �����͸� ���
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
