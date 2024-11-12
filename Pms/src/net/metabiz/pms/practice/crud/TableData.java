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
    private String[] headers = {"","자재코드", "자재명", "GTIN", "비고" };
    private int[] columns = { 30, 80, 100, 130, 200};
    

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
        File file = new File("D:\\DEV\\workspace\\Pms\\file\\csv.csv");

        if (!file.exists() || file.length() == 0) {
            JOptionPane.showMessageDialog(null, "파일이 존재하지 않거나 비어 있습니다! 새로 생성합니다.");
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

        try (Scanner sc = new Scanner(file)) {  // try-with-resources로 Scanner 자동으로 닫기
            boolean firstLine = true;
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (data.length == 4) {  // 인덱스 X 길이 O
                    TableBeans transaction = new TableBeans();
                    TableBeansBuilder tb = new TableBeansBuilder(transaction);
                    transaction = tb.itemCode(data[0])
                            .itemName(data[1])
                            .itemGTIN(Long.parseLong(data[2].trim()))
                            .itemComment(data[3]).transcation();
                    list.add(transaction);
                }
            }

            // 추가: 데이터 로드 상태 확인
            System.out.println("데이터 로딩 완료, 총 " + list.size() + "개의 항목이 로드되었습니다.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getRowCount() {
        return list.size(); // 리스트의 Idx 만큼
    }

    /**
     * 컬럼 개수 5개로 만들고 Index 0번째에 체크박스 
     */
    @Override
    public int getColumnCount() {
        return 5;
    }
    /**
     * 컬럼 개수 5개 0번째 Index는 체크박스 
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {  // try-with-resources로 BufferedWriter 자동으로 닫기
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
        return columnIndex == 0;  // 첫 번째 열(체크박스)만 편집 가능
    }
    
    
    
    
    
    

}
