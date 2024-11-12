// CheckBoxRenderer.java
package net.metabiz.pms.practice.uirenderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

    public CheckBoxRenderer() {
        setHorizontalAlignment(CENTER);  // 체크박스를 중앙 정렬
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // 체크박스 상태에 따라 색상 변경
        if (value instanceof Boolean) {
            setSelected((Boolean) value);  // Boolean 값에 맞춰 체크박스 상태 설정
        } else {
            setSelected(false);  // 기본값 설정
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());  // 선택된 셀의 배경 색
        } else {
            setBackground(table.getBackground());  // 기본 배경 색
        }
        
        return this;  // 렌더러로 체크박스를 반환
    }
}
