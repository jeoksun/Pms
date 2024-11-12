// CheckBoxRenderer.java
package net.metabiz.pms.practice.uirenderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

    public CheckBoxRenderer() {
        setHorizontalAlignment(CENTER);  // üũ�ڽ��� �߾� ����
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // üũ�ڽ� ���¿� ���� ���� ����
        if (value instanceof Boolean) {
            setSelected((Boolean) value);  // Boolean ���� ���� üũ�ڽ� ���� ����
        } else {
            setSelected(false);  // �⺻�� ����
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());  // ���õ� ���� ��� ��
        } else {
            setBackground(table.getBackground());  // �⺻ ��� ��
        }
        
        return this;  // �������� üũ�ڽ��� ��ȯ
    }
}
