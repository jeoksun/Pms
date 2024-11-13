package net.metabiz.pms.practice.valid;

import java.util.ArrayList;
import java.util.List;

public class InputDataValidator {

    public static List<String> validate(String itemCode, String itemName, String GTIN, String comment) {
        List<String> errorMsg = new ArrayList<>();
        if (itemCode == null || itemCode.trim().equals("")) {
            errorMsg.add("������ �ڵ�� �ʼ� �� �Դϴ�!");
        }

        if (itemName == null || itemName.trim().equals("")) {
            errorMsg.add("������� �ʼ� �� �Դϴ�!");
        }

        if (GTIN == null || GTIN.trim().equals("")) {
            errorMsg.add("GTIN�� �ʼ� �� �Դϴ�!");
        }
        else {
            try {
                Long.parseLong(GTIN);
            } catch (NumberFormatException e) {
                errorMsg.add("GTIN�� ���ڿ��߸��մϴ�!");
            }
        }

        if (comment == null || comment.trim().equals("")) {
            errorMsg.add("���� �ʼ� ���Դϴ�!");
        }

        return errorMsg;

    }

}
