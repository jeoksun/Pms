package net.metabiz.pms.practice.uirenderer.dailog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import net.metabiz.pms.practice.data.TableBeans;
import net.metabiz.pms.practice.data.TableData;
import net.metabiz.pms.practice.handler.CRUDFileHandler;

public class DetailWithUpdateDialog extends JDialog  {
    private JPanel pnUpdateMain;
    private String itemCode;
    private String itemName;
    private Long GTIN;
    private String itemComment;
    private int rowIdx;

    private JTextField itemCodeField;
    private JTextField itemNameField;
    private JTextField GTINField;
    private JTextArea ItemCommentField;

    private TableData tableData;
    private CRUDFileHandler handler;

    public DetailWithUpdateDialog(String itemCode, String itemName, String GTIN, String itemComment, int rowIdx,TableData tableData) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.GTIN = Long.parseLong(GTIN);
        this.itemComment = itemComment;
        this.rowIdx = rowIdx;
        this.tableData = tableData;
        detailUI();
    }

    private void detailUI() {
        this.setTitle("자재 상세");
        this.setSize(new Dimension(500, 400));
        this.setLocationRelativeTo(null);
        this.setModal(true);

        pnUpdateMain = new JPanel(new GridBagLayout());
        pnUpdateMain.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnUpdateMain.add(new JLabel("자재코드:"), gbc);

        gbc.gridx = 1;
        itemCodeField = new JTextField(itemCode);
        pnUpdateMain.add(itemCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnUpdateMain.add(new JLabel("자재명:"), gbc);

        gbc.gridx = 1;
        itemNameField = new JTextField(itemName);
        pnUpdateMain.add(itemNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnUpdateMain.add(new JLabel("GTIN:"), gbc);

        gbc.gridx = 1;
        GTINField = new JTextField(GTIN.toString());
        pnUpdateMain.add(GTINField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        pnUpdateMain.add(new JLabel("설명:"), gbc);

        gbc.gridx = 1;
        ItemCommentField = new JTextArea(itemComment);
        ItemCommentField.setPreferredSize(new Dimension(200, 100));
        pnUpdateMain.add(ItemCommentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        JButton modifyButton = new JButton("수정");
        modifyButton.addActionListener(e -> rowDataUpdate());
        pnUpdateMain.add(modifyButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dispose());
        pnUpdateMain.add(closeButton, gbc);

        this.add(pnUpdateMain, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private void rowDataUpdate() {
        String itemCode = itemCodeField.getText();
        String itemName = itemNameField.getText();
        String itemGTIN = GTINField.getText();
        String itemComment = ItemCommentField.getText();

        TableBeans updatedItem = tableData.beansList().get(rowIdx);
        updatedItem.setItemCode(itemCode);
        updatedItem.setItemName(itemName);
        updatedItem.setGtin(Long.parseLong(itemGTIN));
        updatedItem.setItemComment(itemComment);
        handler = new CRUDFileHandler();
        handler.fileUpdate(tableData.beansList());
//        tableData.readCsvData();
        tableData.fireTableDataChanged();
        dispose();


    }

}
