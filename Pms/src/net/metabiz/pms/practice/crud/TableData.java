package net.metabiz.pms.practice.crud;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class TableData extends AbstractTableModel {
    private List<TableBeans> list;
    private String[] headers = {"","�����ڵ�", "�����", "GTIN", "���" };
    private int[] columns = { 30, 80, 100, 130, 200};
    

    public TableData() { // ��ü �ϼ��� ���� �ҷ�����
        updateList();
    }
    public List<TableBeans> beanslist() {
        return list;
    }

    /*
     * AbstractTableModel ���� getColumnName�� ����
     * �ڵ����� ����� ���εǰ� �Ⱦ��� A,B,C,D �� ���εǴµ�
     */
    public String getColumnName(int cell) {
        return headers[cell];
    }

    public void updateList() {
        list = new ArrayList<TableBeans>(); // ����Ʈ �ʱ�ȭ
        File file = new File("D:\\DEV\\workspace\\Pms\\file\\csv.csv");

        if (!file.exists() || file.length() == 0) {
            JOptionPane.showMessageDialog(null, "������ �������� �ʰų� ��� �ֽ��ϴ�! ���� �����մϴ�.");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.write("ItemCode,ItemName,GTIN,ItemComment\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }

        try (Scanner sc = new Scanner(file)) {  // try-with-resources�� Scanner �ڵ����� �ݱ�
            boolean firstLine = true;
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (data.length == 4) {  // �ε��� X ���� O
                    TableBeans transaction = new TableBeans();
                    TableBeansBuilder tb = new TableBeansBuilder(transaction);
                    transaction = tb.itemCode(data[0])
                            .itemName(data[1])
                            .itemGTIN(Long.parseLong(data[2].trim()))
                            .itemComment(data[3]).transcation();
                    list.add(transaction);
                }
            }

            // �߰�: ������ �ε� ���� Ȯ��
            System.out.println("������ �ε� �Ϸ�, �� " + list.size() + "���� �׸��� �ε�Ǿ����ϴ�.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getRowCount() {
        return list.size(); // ����Ʈ�� Idx ��ŭ
    }

    /**
     * �÷� ���� 5���� ����� Index 0��°�� üũ�ڽ� 
     */
    @Override
    public int getColumnCount() {
        return 5;
    }
    /**
     * �÷� ���� 5�� 0��° Index�� üũ�ڽ� 
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
    
    public void fileUpdate() {
        File file = new File("D:\\DEV\\workspace\\Pms\\file\\csv.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {  // try-with-resources�� BufferedWriter �ڵ����� �ݱ�
            writer.write("ItemCode,ItemName,GTIN,ItemComment\n");

            for (TableBeans tb : list) {
                writer.write(tb.getItemCode() + ","
                        + tb.getItemName() + ","
                        + tb.getGtin() + ","
                        + tb.getItemComment() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;  // ù ��° ��(üũ�ڽ�)�� ���� ����
    }
    
    
    
    
    
    

}
