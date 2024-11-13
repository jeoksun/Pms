package net.metabiz.pms.practice.uirenderer.checkbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.table.TableCellEditor;
import net.metabiz.pms.practice.data.TableBeans;
import net.metabiz.pms.practice.data.TableData;
import javax.swing.JTable;

public class CheckBoxEditor extends DefaultCellEditor {
    private JCheckBox checkBox;
    private int row;
    private TableData tableData;

    /**
     * �����Ǹ� tableData�� �޾� üũ�� �ش� �ο��� ���� üũ
     */
    public CheckBoxEditor(TableData tableData) {
        super(new JCheckBox());                 // �� ���� ���� �ϵ��� DefaultCellEditor ����
        this.tableData = tableData;

        checkBox = (JCheckBox) getComponent();  // ������ ������Ʈ�� üũ�ڽ��� ��������ȯ ����?
        checkBox.setHorizontalAlignment(SwingConstants.CENTER); //��Ÿ�� = Center

        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                changeItem();
            }
        });
    }

    private void changeItem() {
        if (0 <= row) {
            TableBeans rowBean = tableData.beansList().get(row);
            rowBean.setCheckStatus(checkBox.isSelected());      //Check ? true:fasle
            tableData.fireTableDataChanged();
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        checkBox.setSelected(value != null && (Boolean) value);
        return checkBox;
    }

    @Override
    public Object getCellEditorValue() {
        return checkBox.isSelected();
    }
}
