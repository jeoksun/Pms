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
     * CSV FILE�� ������ ���� ��
     * HEADER �������
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
            JOptionPane.showMessageDialog(null, "������ �������� �ʰų� ��� �ֽ��ϴ�! ���� �����մϴ�.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Csv file �б�
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
                    list.add(bean); // Bean List �߰�
                }
            }
            System.out.println("������ �ε� �Ϸ�, �� " + list.size() + "���� �׸��� �ε�Ǿ����ϴ�.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(TableBeans tb : list) {
            System.out.println(tb);
        }
        return list;
    }

    /**
     * Csv File�� ������Ʈ (�ٽ� ����)
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
     *  Csv File�� �߰� (�ٽ� ����)
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
                JOptionPane.showMessageDialog(null, "�߰� ����");
                fw.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "�߸��� �߰� ����");
        }

    }

}
