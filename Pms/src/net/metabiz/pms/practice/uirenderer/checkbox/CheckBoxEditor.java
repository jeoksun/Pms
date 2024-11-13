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
     * 생생되면 tableData를 받아 체크된 해당 로우의 상태 체크
     */
    public CheckBoxEditor(TableData tableData) {
        super(new JCheckBox());                 // 셀 편집 가능 하도록 DefaultCellEditor 전달
        this.tableData = tableData;

        checkBox = (JCheckBox) getComponent();  // 편집할 컴포넌트를 체크박스로 강제형변환 느낌?
        checkBox.setHorizontalAlignment(SwingConstants.CENTER); //스타일 = Center

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
