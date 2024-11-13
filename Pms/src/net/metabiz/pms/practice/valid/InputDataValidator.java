package net.metabiz.pms.practice.valid;

import java.util.ArrayList;
import java.util.List;

public class InputDataValidator {

    public static List<String> validate(String itemCode, String itemName, String GTIN, String comment) {
        List<String> errorMsg = new ArrayList<>();
        if (itemCode == null || itemCode.trim().equals("")) {
            errorMsg.add("아이템 코드는 필수 값 입니다!");
        }

        if (itemName == null || itemName.trim().equals("")) {
            errorMsg.add("자재명은 필수 값 입니다!");
        }

        if (GTIN == null || GTIN.trim().equals("")) {
            errorMsg.add("GTIN은 필수 값 입니다!");
        }
        else {
            try {
                Long.parseLong(GTIN);
            } catch (NumberFormatException e) {
                errorMsg.add("GTIN은 숫자여야만합니다!");
            }
        }

        if (comment == null || comment.trim().equals("")) {
            errorMsg.add("비고는 필수 값입니다!");
        }

        return errorMsg;

    }

}
