package net.metabiz.pms.practice.uirenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.table.TableCellEditor;
import javax.swing.JTable;
import net.metabiz.pms.practice.crud.TableData;
import net.metabiz.pms.practice.crud.TableBeans;

public class CheckBoxEditor extends DefaultCellEditor {
    private JCheckBox checkBox;
    private int row;
    private TableData tableData;

    /**
     * 생성될때 tableData를 받아 체크된 해당 로우의 상태 체크
     */
    public CheckBoxEditor(TableData tableData) {
        super(new JCheckBox());
        this.tableData = tableData;

        checkBox = (JCheckBox) getComponent();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);

        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (row >= 0) {
                    TableBeans rowBean = tableData.beanslist().get(row);
                    rowBean.setCheckStatus(checkBox.isSelected());
                    tableData.fireTableDataChanged();
                }

            }
        });
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
