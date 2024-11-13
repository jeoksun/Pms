package net.metabiz.pms.practice.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import net.metabiz.pms.practice.data.ConstantData;
import net.metabiz.pms.practice.data.TableBeans;
import net.metabiz.pms.practice.data.beansbuilder.TableBeansBuilder;

public class CRUDFileHandler {

    /**
     * CSV FILE이 없으면 생성 후
     * HEADER 적용까지
     */
    private void handleFileCreation(File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.write(ConstantData.HEADER);
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "파일이 존재하지 않거나 비어 있습니다! 새로 생성합니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Csv file 읽기
     */
    public List<TableBeans> readCsvData() {
        List<TableBeans> list = new ArrayList<>();

        File file = new File(ConstantData.FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            handleFileCreation(file);
        }

        try (Scanner sc = new Scanner(file)) {
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                if (data.length == 4) {
                    TableBeans bean = new TableBeansBuilder(new TableBeans())
                            .itemCode(data[0])
                            .itemName(data[1])
                            .itemGTIN(Long.parseLong(data[2].trim()))
                            .itemComment(data[3])
                            .transcation();
                    list.add(bean); // Bean List 추가
                }
            }
            System.out.println("데이터 로딩 완료, 총 " + list.size() + "개의 항목이 로드되었습니다.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(TableBeans tb : list) {
            System.out.println(tb);
        }
        return list;
    }

    /**
     * Csv File에 업데이트 (다시 쓰기)
     */
    public void fileUpdate(List<TableBeans> list) {
        File file = new File(ConstantData.FILE_PATH);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(ConstantData.HEADER);
            for (TableBeans tb : list) {
                writer.write(String.format("%s,%s,%d,%s\n", tb.getItemCode(), tb.getItemName(), tb.getGtin(), tb.getItemComment()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     *  Csv File에 추가 (다시 쓰기)
     */
    public void fileWriter(String inputData) {
        System.out.println("fileWriter : " + inputData);
        boolean fileExist = false;

        try {
            File file = new File(ConstantData.FILE_PATH);
            fileExist = file.exists();
            if (fileExist) {
                FileWriter fw = new FileWriter(file, true);
                fw.append("\n");
                fw.append(inputData);
                JOptionPane.showMessageDialog(null, "추가 성공");
                fw.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "잘못된 추가 형식");
        }

    }

}
