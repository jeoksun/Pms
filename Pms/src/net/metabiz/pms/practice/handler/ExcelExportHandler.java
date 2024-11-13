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
         * ���丮 or ���� ����
         */
        if (!ExcelFileValidator.validateAndCreateDir()) {
            JOptionPane.showMessageDialog(null, "���丮 ���� ����");
            return;
        }
        if (!ExcelFileValidator.validateAndCreateFile()) {
            JOptionPane.showMessageDialog(null, "���� ���� ����");
            return;
        }

        /**
         * ���̺� �ִ� ������ �ϴ� ��������
         */
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("JTextField �Է°�");
        TableModel model = table.getModel();//Table���� Model ��������
        Row headerRow = sheet.createRow(0); // ù ��° ���� ���

        int columnCount = model.getColumnCount();
        int excludedColumnIndex = 0;        // üũ�ڽ� ���� ������ ���� 
        int columnIndex = 0;

        /**
         * üũ�ڽ��� ���� export�� ����
         */
        for (int i = 0; i < columnCount; i++) {
            if (i == excludedColumnIndex) {  // üũ�ڽ� �÷��� ����
                continue;
            }
            Cell cel = headerRow.createCell(columnIndex++);
            cel.setCellValue(model.getColumnName(i));
        }

        /**
         * ���̺� ������(header ������ row) �������� (üũ�ڽ� �÷� ����)
         * ���͸��� �����͸� ��������
         */
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();

        // sorter�� null�� ��츦 ó��
        boolean isFiltered = (sorter != null && sorter.getRowFilter() != null);
        
        FileOutputStream export = null;

//        try (FileOutputStream export = new FileOutputStream(new File(PropertiesData.exportPath))) {
        try {
            export = new FileOutputStream(new File(PropertiesData.exportPath));
            int rowNum = 1; // �����ʹ� ù ��° ����� ����

            for (int i = 0; i < model.getRowCount(); i++) {
                int viewRow;
                if (isFiltered) {
                    // ��ȸ ���Ͱ� ����Ȱ�쿡�� ����
                    viewRow = sorter.convertRowIndexToView(i);
                    if (viewRow == -1) {
                        continue; // ���͸��Ǿ� ǥ�õ��� �ʴ� ���� ����
                    }
                } else {
                    // ���Ͱ� ����������� ���!
                    viewRow = i;
                }

                Row row = sheet.createRow(rowNum++); // ù ��° ���� ����ϱ� +1���� ������ ����
                columnIndex = 0;
                for (int j = 0; j < columnCount; j++) {
                    if (j == excludedColumnIndex) { // üũ�ڽ� �÷��� ����
                        continue;
                    }
                    Cell cell = row.createCell(columnIndex++);
                    if (model.getValueAt(viewRow, j) != null) {
                        cell.setCellValue(model.getValueAt(viewRow, j).toString()); // ���̺��� �� ��������
                    } else {
                        cell.setCellValue(""); // ���� ������ �� ���ڿ�
                    }
                }
            }

            // ���� ���� ����
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
