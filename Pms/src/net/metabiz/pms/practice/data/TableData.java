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
    private String[] headers = {"", "자재코드", "자재명", "GTIN", "비고"};
    private int[] columns = {30, 80, 100, 130, 200};
    private CRUDFileHandler handler;

    /**
     * 객체가 생성되면 updateList(); 메서드 호출
     */
    public TableData() {
        handler = new CRUDFileHandler();
        list = handler.readCsvData();
    }
    
    public void updateList() {
        handler.fileUpdate(list);
    }

    /**
     *  updateList() 에서 빈 List에 추가된 데이터를 담고있는 
     *  메소드 
     */
    public List<TableBeans> beansList() {
        return list;
    }

    /**
     * 컬럼의 이름 반환
     */
    @Override
    public String getColumnName(int columnIndex) {
        return headers[columnIndex];
    }
    
    /**
     * List의 size반환(행 개수)반환
     */
    @Override
    public int getRowCount() {
        return list.size();
    }
    
    /**
     * 컬럼 개수 반환
     */
    @Override
    public int getColumnCount() {
        return headers.length;  
    }
    
    /**
     * 특정 셀에 들어갈 값 반환
     * Jtable에 셀을 랜더링할때 호출
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
     * 업데이트 데이터 Table에 반영
     */
    public void renewal() {
        list = handler.readCsvData();
        fireTableDataChanged();
    }
    

    /**
     * 0번째 컬럼(CheckBox) 만 편집가능
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;  
    }
    
  


}
