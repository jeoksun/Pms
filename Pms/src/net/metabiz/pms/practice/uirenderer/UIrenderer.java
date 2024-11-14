package net.metabiz.pms.practice.uirenderer;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import net.metabiz.pms.practice.data.TableData;
import net.metabiz.pms.practice.handler.ExcelExportHandler;
import net.metabiz.pms.practice.uirenderer.checkbox.CheckBoxEditor;
import net.metabiz.pms.practice.uirenderer.checkbox.CheckBoxRenderer;
import net.metabiz.pms.practice.uirenderer.dailog.AddDialog;
import net.metabiz.pms.practice.uirenderer.dailog.DeleteDiaglog;
import net.metabiz.pms.practice.uirenderer.dailog.DetailWithUpdateDialog;
import net.metabiz.pms.practice.uirenderer.style.SwingStyle;

public class UIrenderer extends JFrame {

    private static UIrenderer instance;
    
    private JTextField itemCodeFilter; // �����ڵ� �ؽ�Ʈ�ʵ�
    private JTextField itemNameFilter; // ����� �ؽ�Ʈ�ʵ�
    private JTextField gtinFilter; // GTIN �ؽ�Ʈ�ʵ�
    private JTextField commentFilter; // ��� �ؽ�Ʈ�ʵ�
    private JTable searchResultTable;
    private TableData tableData; // ���̺� ������ �������� ��ü
    private int row; // Table row ������ ���� ����
    private AddDialog addDialog; // ���� �߰� �� ȣ�� ��ü
    private DeleteDiaglog delData; // ���� ���� �� ȣ�� ��ü
    private DetailWithUpdateDialog detailUI; // Row Ŭ�� �� �� ��ü (��������)
    private ExcelExportHandler export; // Export btn Ŭ���� ȣ��(�������� export) ��ü
    private CheckBoxRenderer checkBoxRenderer; // CheckBox ������
    private CheckBoxEditor checkBoxEditor; // CheckBox ����
    
    public static UIrenderer getInstance() {
        if(instance == null) {
            instance = new UIrenderer();
        }
        return instance;
    }
    private UIrenderer() {
        init();
    }
  

    public void init() {
        /*****************
         * this = Jframe *
         *****************/

        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);
        this.setTitle("PMS ���� ���� �ý���");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pnMain = new JPanel(new BorderLayout(10, 10)); // ������ �߰��Ͽ� ������Ʈ �� ������ �����

        /**
         * ��� ���� ����
         * ��� Layout ����
         */
        JPanel pnMainNorth = new JPanel(new BorderLayout(10, 10));
        JPanel pnMainNorthNorth = new JPanel(new BorderLayout());
        JPanel pnMainNorthCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        /**
         * ��� ���� Ÿ��Ʋ Label
         * pnMainNorthNorth ���� ���� Ÿ��Ʋ **Label**�� ��
         */
        JLabel mainTitleLabel = new JLabel("���� ���� �ý���");
        mainTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainTitleLabel.setForeground(new Color(0, 51, 102)); // Ÿ��Ʋ ����
        pnMainNorthNorth.add(mainTitleLabel, BorderLayout.NORTH);

        /**
         * pnMainNorthCenter
         * ��ȸ �� �� TextField, �̺�Ʈ
         */
        pnMainNorthCenter.add(new JLabel("�����ڵ�:"));
        itemCodeFilter = new JTextField(15);
        SwingStyle.textFieldStyleChanger(itemCodeFilter);
        itemCodeFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(itemCodeFilter);

        pnMainNorthCenter.add(new JLabel("�����:"));
        itemNameFilter = new JTextField(15);
        SwingStyle.textFieldStyleChanger(itemNameFilter);
        itemNameFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(itemNameFilter);

        pnMainNorthCenter.add(new JLabel("GTIN:"));
        gtinFilter = new JTextField(15);
        SwingStyle.textFieldStyleChanger(gtinFilter);
        gtinFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(gtinFilter);

        pnMainNorthCenter.add(new JLabel("���:"));
        commentFilter = new JTextField(15);
        SwingStyle.textFieldStyleChanger(commentFilter);
        commentFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(commentFilter);

        pnMainNorth.add(pnMainNorthNorth, BorderLayout.NORTH);
        pnMainNorth.add(pnMainNorthCenter, BorderLayout.CENTER);

        // �߾� - ���̺� ����
        JPanel pnMainCenter = new JPanel(new BorderLayout(10, 10));

        JPanel pnMainCenterNorth = new JPanel(new BorderLayout());
        JLabel searchResultLabel = new JLabel("��� ��ȸ ���̺�");
        searchResultLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        pnMainCenterNorth.add(searchResultLabel, BorderLayout.NORTH);

        pnMainCenter.add(pnMainCenterNorth, BorderLayout.NORTH);

        /**
         * ���̺� ������ �ʱ�ȭ �� ���̺� ����
         */
        tableData = new TableData();
        searchResultTable = new JTable(tableData);

        // ���̺� �� �� ũ�� �� üũ�ڽ� ���� ����
        searchResultTable.setRowHeight(30); // �� �� ���� ����
        searchResultTable.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(40); // üũ�ڽ� width ũ��
        for (int i = 1; i < searchResultTable.getColumnCount(); i++) {// �ٸ��÷� width ũ��
            searchResultTable.getColumnModel().getColumn(i).setPreferredWidth(300);
        }

        /**
         * CheckBox���� ���ͷ� ������ �ʱ�ȭ
         * 0��° �÷� {""} ���� üũ�ڽ� �ο�
         */
        checkBoxRenderer = new CheckBoxRenderer();
        checkBoxEditor = new CheckBoxEditor(tableData);
        searchResultTable.getColumnModel().getColumn(0).setCellRenderer(checkBoxRenderer);
        searchResultTable.getColumnModel().getColumn(0).setCellEditor(checkBoxEditor);

        /**
         * üũ�ڽ� �� �� Ŭ���� �̺�Ʈ
         */
        searchResultTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cellClickEvent();
            }
        });

        /**
         * ���̺� ��ũ�������� ���α�
         */
        JScrollPane scrTable = new JScrollPane(searchResultTable);
        pnMainCenter.add(scrTable, BorderLayout.CENTER);

        /**
         * �ϴ� ��ư ����
         */
        JPanel pnMainSouth = new JPanel(new BorderLayout(10, 10));
        JPanel pnMainSouthSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        /**
         * �߰� �̺�Ʈ
         */
        JButton addBtn = new JButton("�� ��");
        SwingStyle.btnStyleChanger(addBtn);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowAddEvent();
            }
        });

        /**
         * ���� �̺�Ʈ
         */
        JButton delBtn = new JButton("�� ��");
        SwingStyle.btnStyleChanger(delBtn);
        delBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delData = new DeleteDiaglog(tableData);
            }
        });

        /**
         * ���� export �̺�Ʈ
         */
        JButton exportBtn = new JButton("EXPORT");
        SwingStyle.btnStyleChanger(exportBtn);
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    export = new ExcelExportHandler(searchResultTable);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        /**
         * ��ư �߰�
         */
        pnMainSouthSouth.add(addBtn);
        pnMainSouthSouth.add(delBtn);
        pnMainSouthSouth.add(exportBtn);

        pnMainSouth.add(pnMainSouthSouth, BorderLayout.CENTER); // ������ �ϴ� ������ south�� ��ư�ֱ�
        /**
         * ���ο� �뽺, ����, ��콺 ���
         */
        pnMain.add(pnMainNorth, BorderLayout.NORTH);
        pnMain.add(pnMainCenter, BorderLayout.CENTER);
        pnMain.add(pnMainSouth, BorderLayout.SOUTH);

        /**
         * ������ �뽺, ����, ��콺�� ��� main�� frame�� �߰� ��Ű�鼭 �ϼ�
         */
        this.add(pnMain);
        this.setVisible(true);
        /************
         * ������ ��*
         ***********/
    }

    /**
     * CheckBox ���� �ٸ� �÷� �� Ŭ���� �߻� �̺�Ʈ
     */
    private void cellClickEvent() {

        if (searchResultTable.getSelectedColumn() == 0) { // Ŭ���� ���� �÷��� 0(üũ�ڽ� ��)�� ��� �̺�Ʈ ó������ ����
            return; // üũ�ڽ��� Ŭ���� ��� �ٷ� ���Ͻ��Ѽ� �ƹ��ϵ�����
        }

        row = searchResultTable.getSelectedRow(); // default�� -1, checkbox�� 0
        if (row != -1) {
            /**
             * ������ row�� ���� �����ͼ� ���� �� detail��ü�� �Ѹ�
             */
            String itemCode = tableData.getValueAt(row, 1).toString();
            String itemName = tableData.getValueAt(row, 2).toString();
            String itemGTIN = tableData.getValueAt(row, 3).toString();
            String itemComment = tableData.getValueAt(row, 4).toString();

            detailUI = new DetailWithUpdateDialog(itemCode, itemName, itemGTIN, itemComment, row, tableData);
        }
    }

    /**
     * �ο� �߰� �̺�Ʈ
     */
    private void rowAddEvent() {
        addDialog = new AddDialog(tableData);
        tableData.renewal();
        int lastIdx = searchResultTable.getRowCount() - 1;
        if (0 <= lastIdx) {
            searchResultTable.scrollRectToVisible(searchResultTable.getCellRect(lastIdx, 0, true));
        }

    }

    /**
     * �ؽ�Ʈ�ʵ忡 �Է��Ҷ����� �̺�Ʈ �߻�
     */
    private KeyListener createKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilter();
            }
        };
    }

    /**
     * Ű�Է� �̺�Ʈ �߻��� ��ü
     */
    private void applyFilter() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableData);
        searchResultTable.setRowSorter(sorter);
        List<RowFilter<TableModel, Object>> filters = new ArrayList<>();
        filters.add(RowFilter.regexFilter(getItemCodeFilter(), 1));
        filters.add(RowFilter.regexFilter(getItemNameFilter(), 2));
        filters.add(RowFilter.regexFilter(getGtinFilter(), 3));
        filters.add(RowFilter.regexFilter(getCommentFilter(), 4));

        /**
         * andFilter�� List�� �Ķ���ͷ� ���� ����÷� �ε�����  
         * �Է��Ѱ��� ���ؼ� ��ġ�ϴ� �����͸� ��ȯ�ϰ� ������
         */
        RowFilter<TableModel, Object> rowFilter = RowFilter.andFilter(filters);
        sorter.setRowFilter(rowFilter);
    }

    /**
     * Ʈ�� ó�� ���� ���ּ� ���ϰ��� ��ȸ �ؽ�Ʈ �ʵ忡 ����
     */
    private String getItemCodeFilter() {
        return itemCodeFilter.getText().trim();
    }

    private String getItemNameFilter() {
        return itemNameFilter.getText().trim();
    }

    private String getGtinFilter() {
        return gtinFilter.getText().trim();
    }

    private String getCommentFilter() {
        return commentFilter.getText().trim();
    }



}
