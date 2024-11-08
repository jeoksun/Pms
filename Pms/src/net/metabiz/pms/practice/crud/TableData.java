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

    public TableData() { // 객체 완성시 파일 불러오기
        updateList();
    }
    public List<TableBeans> beanslist() {
        return list;
    }

    /*
     * AbstractTableModel 에서 getColumnName을 쓰면
     * 자동으로 헤더에 매핑되고 안쓰면 A,B,C,D 로 매핑되는듯
     */
    public String getColumnName(int cell) {
        return headers[cell];
    }

    public void updateList() {
        list = new ArrayList<TableBeans>(); // 리스트 초기화
        try {
            File file = new File("D:\\DEV\\workspace\\Pms\\file\\csv.csv");

            if (!file.exists() || file.length() == 0) {
                JOptionPane.showMessageDialog(null, "파일이 존재하지 않거나 비어 있습니다! 새로 생성합니다.");

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

                if (data.length == 4) {                 //인덱스 X 길이O
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
                JOptionPane.showMessageDialog(null, "파일이 존재하나 데이터가 없습니다! 새로 생성합니다.");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("ItemCode,ItemName,GTIN,ItemComment\n");
                }
            }
            else {
                System.out.println("파일에서 읽어온 데이터:");
                list.forEach(System.out::println);
            }

            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return list.size(); // 리스트의 Idx 만큼
    }

    @Override
    public int getColumnCount() { // 컬럼 갯수
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
