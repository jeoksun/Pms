package net.metabiz.pms.practice.uirenderer.dailog;

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
import net.metabiz.pms.practice.data.TableBeans;
import net.metabiz.pms.practice.data.TableData;

public class DeleteDiaglog extends JDialog {
//    private int rowIdx;
    private TableData tableData;

    public DeleteDiaglog( TableData tableData) {
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
        JLabel messageLabel = new JLabel("���� ���� �Ͻðڽ��ϱ�?");
        messageLabel.setSize(10, 10);
        Font font = new Font("����", Font.BOLD, 16);
        messageLabel.setFont(font);
        centerPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton yesButton = new JButton("��");
        yesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delData();
            }
        });
        JButton noButton = new JButton("�ƴϿ�");
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
    /**
     * �ش� Index �� �����
     */
    private void delData() {
        for (int i = tableData.beansList().size() - 1; i >= 0; i--) {   
            TableBeans item = tableData.beansList().get(i);
            if (item.isCheckStatus()) {
                tableData.beansList().remove(i);
            }
        }
        
        tableData.updateList();
        tableData.fireTableDataChanged();
        
        dispose();
    }

}
