package net.metabiz.pms.practice.uirenderer.dailog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import net.metabiz.pms.practice.data.TableBeans;
import net.metabiz.pms.practice.data.TableData;
import net.metabiz.pms.practice.handler.CRUDFileHandler;
import net.metabiz.pms.practice.uirenderer.style.SwingStyle;
import net.metabiz.pms.practice.valid.InputDataValidator;

public class DetailWithUpdateDialog extends JDialog  {
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
        this.setSize(new Dimension(400, 400));
        this.setLocationRelativeTo(null);
        this.setModal(true);
        
        JPanel pnMain = new JPanel(new BorderLayout());
        JPanel pnUpdate = new JPanel(new GridBagLayout());
        pnUpdate.setBorder(new EmptyBorder(8, 8, 2, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.insets = new Insets(5, 5, 5, 5); //여백
        

        // 자재 코드 입력 필드
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnUpdate.add(new JLabel("자재코드:"), gbc);
        gbc.gridx = 1;
        itemCodeField = new JTextField(itemCode);
        pnUpdate.add(itemCodeField, gbc);

        // 자재명 입력 필드
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnUpdate.add(new JLabel("자재명:"), gbc);
        gbc.gridx = 1;
        itemNameField = new JTextField(itemName);
        pnUpdate.add(itemNameField, gbc);

        // GTIN 입력 필드
        gbc.gridx = 0;
        gbc.gridy = 2;
        pnUpdate.add(new JLabel("GTIN:"), gbc);
        gbc.gridx = 1;
        GTINField = new JTextField(GTIN.toString());
        pnUpdate.add(GTINField, gbc);

        // 설명 입력 필드
        gbc.gridx = 0;
        gbc.gridy = 3;
        pnUpdate.add(new JLabel("설명:"), gbc);
        gbc.gridx = 1;
        ItemCommentField = new JTextArea(itemComment);
        ItemCommentField.setPreferredSize(new Dimension(200, 100));
        pnUpdate.add(ItemCommentField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout());
        
        // 수정 버튼
        JButton modifyButton = new JButton("수정");
        SwingStyle.btnStyleChanger(modifyButton);
        modifyButton.addActionListener(e -> rowDataUpdate());
        btnPanel.add(modifyButton);

        // 닫기 버튼
        JButton closeButton = new JButton("닫기");
        SwingStyle.btnStyleChanger(closeButton);
        closeButton.addActionListener(e -> dispose());
        btnPanel.add(closeButton);
        
        pnMain.add(pnUpdate,BorderLayout.CENTER); 
        pnMain.add(btnPanel,BorderLayout.SOUTH);
        this.add(pnMain);
        this.setVisible(true);
    }


    private void rowDataUpdate() {
        String itemCode = itemCodeField.getText();
        String itemName = itemNameField.getText();
        String itemGTIN = GTINField.getText();
        String itemComment = ItemCommentField.getText();

        List<String> errorMsg = InputDataValidator.validate(itemCode, itemName, itemGTIN, itemComment);
        if(errorMsg.isEmpty()) {
            TableBeans updatedItem = tableData.beansList().get(rowIdx);
            updatedItem.setItemCode(itemCode);
            updatedItem.setItemName(itemName);
            updatedItem.setGtin(Long.parseLong(itemGTIN));
            updatedItem.setItemComment(itemComment);
            handler = new CRUDFileHandler();
            handler.fileUpdate(tableData.beansList());
            tableData.fireTableDataChanged();
            dispose();
        }else {
            for(int i = 0 ; i<errorMsg.size(); i++) {
                JOptionPane.showMessageDialog(null, errorMsg.get(i));
            }
            
        }
        


    }

}
