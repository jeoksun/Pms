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
    private String[] headers = { "ItemCode", "ItemName", "GTIN", "ItemComment" };

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
        try {
            File file = new File("D:\\DEV\\workspace\\Pms\\file\\csv.csv");

            if (!file.exists() || file.length() == 0) {
                JOptionPane.showMessageDialog(null, "������ �������� �ʰų� ��� �ֽ��ϴ�! ���� �����մϴ�.");

                if (!file.exists()) {
                    file.createNewFile();

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.write("ItemCode,ItemName,GTIN,ItemComment\n");
                    }
                }
               
                return;
            }

            Scanner sc = new Scanner(file);
            boolean firstLine = true;

            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (data.length == 4) {                 //�ε��� X ����O
                    TableBeans transaction = new TableBeans();
                    TableBeansBuilder tb = new TableBeansBuilder(transaction);
                    transaction = tb.itemCode(data[0])
                            .itemName(data[1])
                            .itemGTIN(Long.parseLong(data[2]))
                            .itemComment(data[3]).transcation();
                    list.add(transaction);
                }
            }

            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(null, "������ �����ϳ� �����Ͱ� �����ϴ�! ���� �����մϴ�.");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("ItemCode,ItemName,GTIN,ItemComment\n");
                }
            }
            else {
                System.out.println("���Ͽ��� �о�� ������:");
                list.forEach(System.out::println);
            }

            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return list.size(); // ����Ʈ�� Idx ��ŭ
    }

    @Override
    public int getColumnCount() { // �÷� ����
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getItemCode();
            case 1:
                return list.get(rowIndex).getItemName();
            case 2:
                return list.get(rowIndex).getGtin();
            case 3:
                return list.get(rowIndex).getItemComment();
            default:
                return null;

        }
    }
    
    public void fileUpdate() {
        try {
            File file = new File("D:\\DEV\\workspace\\Pms\\file\\csv.csv");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ItemCode,ItemName,GTIN,ItemComment\n");

                for (TableBeans tb : list) {
                    writer.write(tb.getItemCode() + ","
                            + tb.getItemName() + ","
                            + tb.getGtin() + ","
                            + tb.getItemComment() + "\n");
                }
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
