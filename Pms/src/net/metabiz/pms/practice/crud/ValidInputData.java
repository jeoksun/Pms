package net.metabiz.pms.practice.crud;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ValidInputData extends JDialog {
    private boolean validStatus = true;
    private String result = "모든 데이터 검증 완료";

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
        result = regex.equals(input) ? this.result : "데이터 형식 불일치";
        return result;
    }

    private String itemNameValid(String input) {
        String regex = "^[A-Z]{2}\\d{8}$"; // CSV파일 리스트로 자재명만 불러와서
        result = regex.equals(input) ? this.result : "자재명 중복";
        return result;
    }

    private String itemGTINValid(String input) {
        String regex = "^[A-Z]{2}\\d{8}$";
        result = regex.equals(input) ? this.result : "데이터 형식 불일치";
        return result;
    }

    private String itemCommentValid(String input) {
        String regex = "^[A-Z]{2}\\d{8}$";
        result = regex.equals(input) ? this.result : "길이 제한";
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
            // 검증
            for (TableBeans tr : list) {
                if(tr.getGtin() == data.getGtin()) {
                    gtinCheck = true;
                    break;
                }else if(tr.getItemCode().equals(data.getItemCode())) {
                    
                }
            }

        }
        
        if(gtinCheck) {
            result = "GTIN 중복";
        }

        // 추가
        
        
        

        return result;
    }

}