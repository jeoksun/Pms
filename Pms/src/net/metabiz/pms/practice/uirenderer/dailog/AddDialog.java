package net.metabiz.pms.practice.uirenderer.dailog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import javax.net.ssl.HandshakeCompletedListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import net.metabiz.pms.practice.data.ConstantData;
import net.metabiz.pms.practice.data.TableData;
import net.metabiz.pms.practice.handler.CRUDFileHandler;
import net.metabiz.pms.practice.valid.InputDataValidator;

public class AddDialog extends JDialog implements ActionListener {
    private JTextField itemCodeField;
    private JTextField itemNameField;
    private JTextField itemGTINField;
    private JTextArea itemCommentField;
    private JButton saveBtn;
    private List<String> list;
    private String writeData;
    private TableData tableData;
    private CRUDFileHandler handler;

    public AddDialog(TableData tableData) {
        this.tableData = tableData;
        init();
    }

    /**
     * Dialog 생성
     */
    private void init() {
        this.setTitle("자재 추가 항목");
        this.setSize(new Dimension(400,400)); // 크기조정
        this.setLocationRelativeTo(null);
        this.setModal(true);

        JPanel pnAddMain = new JPanel(new GridBagLayout());
        pnAddMain.setBorder(new EmptyBorder(8, 8, 8, 8)); // 전체여백조정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // 컴포넌트사이의 간격 설정

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnAddMain.add(new JLabel("자재코드:"), gbc);
        itemCodeField = new JTextField(20);
        gbc.gridx = 1;
        pnAddMain.add(itemCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnAddMain.add(new JLabel("자재명:"), gbc);
        itemNameField = new JTextField(20);
        gbc.gridx = 1;
        pnAddMain.add(itemNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnAddMain.add(new JLabel("GTIN:"), gbc);
        itemGTINField = new JTextField(20);
        gbc.gridx = 1;
        pnAddMain.add(itemGTINField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        pnAddMain.add(new JLabel("설명:"), gbc);
        itemCommentField = new JTextArea();
        gbc.gridx = 1;
        itemCommentField.setBorder(BorderFactory.createLineBorder(Color.gray));
        itemCommentField.setPreferredSize(new Dimension(100, 100));
        pnAddMain.add(itemCommentField, gbc);

        // 추가버튼
        saveBtn = new JButton("추가");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST; // 오른쪽 정렬
        pnAddMain.add(saveBtn, gbc);

        saveBtn.addActionListener(this); // 이벤트 등록?

        this.add(pnAddMain, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public List<String> inputData(String itemCode, String itemName, String GTIN, String itemComment) {
        list = new ArrayList<>();
        list.add(itemCode);
        list.add(itemName);
        list.add(GTIN);
        list.add(itemComment);
        return list;

    }

    public String inputDataParsing() {
        String ldp = "";
        for (String str : list) {
            System.out.println(str);
        }
        return ldp;
    }


    /**
     * TextField 비우기
     */
    public void cleanTextField() {
        itemCodeField.setText("");
        itemNameField.setText("");
        itemGTINField.setText("");
        itemCommentField.setText("");
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == saveBtn) {
            addBeforeDataValid();
        }
    }
    /**
     * 추가하기전 데이터 검증
     */
    
    public void addBeforeDataValid() {
        handler = new CRUDFileHandler();
        
        String itemCode = itemCodeField.getText();
        String itemName = itemNameField.getText();
        String itemGTIN = itemGTINField.getText();
        String itemComment = itemCommentField.getText();
        List<String> errorMsg = InputDataValidator.validate(itemCode, itemName, itemGTIN, itemComment);
        if (errorMsg.isEmpty()) {
            // 검증 통과
            writeData = itemCode + "," + itemName + "," + itemGTIN + "," + itemComment;
            handler.fileWriter(writeData);
        }
        else {
            for (int i = 0; i < errorMsg.size(); i++) {
                JOptionPane.showMessageDialog(null, errorMsg.get(i));
            }
        }
    }

}
