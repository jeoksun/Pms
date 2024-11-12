package net.metabiz.pms.practice.crud;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DeleteData extends JDialog {
//    private int rowIdx;
    private TableData tableData;

    public DeleteData( TableData tableData) {
//        this.rowIdx = rowIdx;
        this.tableData = tableData;
        delInit();
    }

    private void delInit() {
        this.setSize(new Dimension(400, 200));
        this.setVisible(true);
        this.setModal(true);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new FlowLayout());
        JLabel messageLabel = new JLabel("정말 삭제 하시겠습니까?");
        messageLabel.setSize(10, 10);
        Font font = new Font("굴림", Font.BOLD, 16);
        messageLabel.setFont(font);
        centerPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton yesButton = new JButton("예");
        yesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delData();
            }
        });
        JButton noButton = new JButton("아니요");
        noButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        southPanel.add(yesButton);
        southPanel.add(noButton);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        this.add(panel);

    }

    private void delData() {
        for (int i = tableData.beanslist().size() - 1; i >= 0; i--) {   //공유 List
            TableBeans item = tableData.beanslist().get(i);
            if (item.isCheckStatus()) {
                tableData.beanslist().remove(i);
            }
        }

        
        tableData.fireTableDataChanged();
        tableData.fileUpdate(); 
        
        dispose();
    }

}
