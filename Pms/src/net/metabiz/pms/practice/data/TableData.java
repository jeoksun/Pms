package net.metabiz.pms.practice.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import net.metabiz.pms.practice.data.beansbuilder.TableBeansBuilder;
import net.metabiz.pms.practice.handler.CRUDFileHandler;

public class TableData extends AbstractTableModel {
  

    private List<TableBeans> list;
    private String[] headers = {"", "�����ڵ�", "�����", "GTIN", "���"};
    private int[] columns = {30, 80, 100, 130, 200};
    private CRUDFileHandler handler;

    /**
     * ��ü�� �����Ǹ� updateList(); �޼��� ȣ��
     */
    public TableData() {
        handler = new CRUDFileHandler();
        list = handler.readCsvData();
    }
    
    public void updateList() {
        handler.fileUpdate(list);
    }

    /**
     *  updateList() ���� �� List�� �߰��� �����͸� ����ִ� 
     *  �޼ҵ� 
     */
    public List<TableBeans> beansList() {
        return list;
    }

    /**
     * �÷��� �̸� ��ȯ
     */
    @Override
    public String getColumnName(int columnIndex) {
        return headers[columnIndex];
    }
    
    /**
     * List�� size��ȯ(�� ����)��ȯ
     */
    @Override
    public int getRowCount() {
        return list.size();
    }
    
    /**
     * �÷� ���� ��ȯ
     */
    @Override
    public int getColumnCount() {
        return headers.length;  
    }
    
    /**
     * Ư�� ���� �� �� ��ȯ
     * Jtable�� ���� �������Ҷ� ȣ��
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TableBeans bean = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return bean.isCheckStatus();
            case 1:
                return bean.getItemCode();
            case 2:
                return bean.getItemName();
            case 3:
                return bean.getGtin();
            case 4:
                return bean.getItemComment();
            default:
                return null;
        }
    }
    /**
     * ������Ʈ ������ Table�� �ݿ�
     */
    public void renewal() {
        list = handler.readCsvData();
        fireTableDataChanged();
    }
    

    /**
     * 0��° �÷�(CheckBox) �� ��������
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;  
    }
    
  


}
