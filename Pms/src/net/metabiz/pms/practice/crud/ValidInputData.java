package net.metabiz.pms.practice.crud;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ValidInputData extends JDialog {
    private boolean validStatus = true;
    private String result = "��� ������ ���� �Ϸ�";

    private List<TableBeans> list;

    public ValidInputData(String[] input) {
        init();
        itemCodeValid(input[0]);
        itemNameValid(input[1]);
        itemGTINValid(input[2]);
        itemCommentValid(input[3]);

    }

    private void init() {
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        this.add(new JLabel(result));
    }

    private String itemCodeValid(String input) {
        String regex = "^[A-Z]{2}\\d{8}$";
        result = regex.equals(input) ? this.result : "������ ���� ����ġ";
        return result;
    }

    private String itemNameValid(String input) {
        String regex = "^[A-Z]{2}\\d{8}$"; // CSV���� ����Ʈ�� ����� �ҷ��ͼ�
        result = regex.equals(input) ? this.result : "����� �ߺ�";
        return result;
    }

    private String itemGTINValid(String input) {
        String regex = "^[A-Z]{2}\\d{8}$";
        result = regex.equals(input) ? this.result : "������ ���� ����ġ";
        return result;
    }

    private String itemCommentValid(String input) {
        String regex = "^[A-Z]{2}\\d{8}$";
        result = regex.equals(input) ? this.result : "���� ����";
        return result;
    }

    private void showDialog() {

    }

    public String isCheck(TableBeans data) {
        String result = "";
        
        boolean codeCheck = false;
        boolean nameCheck = false;
        boolean gtinCheck = false;

        if (list != null && list.size() > 0) {
            // ����
            for (TableBeans tr : list) {
                if(tr.getGtin() == data.getGtin()) {
                    gtinCheck = true;
                    break;
                }else if(tr.getItemCode().equals(data.getItemCode())) {
                    
                }
            }

        }
        
        if(gtinCheck) {
            result = "GTIN �ߺ�";
        }

        // �߰�
        
        
        

        return result;
    }

}