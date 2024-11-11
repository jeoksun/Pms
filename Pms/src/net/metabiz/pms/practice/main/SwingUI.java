package net.metabiz.pms.practice.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import net.metabiz.pms.practice.crud.AddItemDialog;
import net.metabiz.pms.practice.crud.DeleteData;
import net.metabiz.pms.practice.crud.DetailUI;
import net.metabiz.pms.practice.crud.TableData;
import net.metabiz.pms.practice.excel.ExcelStart;
import net.metabiz.pms.practice.uirenderer.CheckBoxRenderer;

public class SwingUI extends JFrame {
    private JPanel pnMain;
    private JPanel pnMainNorth;
    private JPanel pnMainNorthNorth;
    private JPanel pnMainNorthCenter;
    private JTextField searchTextField;

    private JPanel pnMainCenter;
    private JPanel pnMainCenterNorth;
    private JPanel pnMainCenterCenter;

    private JPanel pnMainSouth;
    private JPanel pnMainSouthSouth;
    private JPanel pnMainSouthSouthSouth;
    
    private JButton searchBtn;
    private JButton addBtn;
    private JButton delBtn;
    private JButton exportBtn;

    private JTable searchResultTable;
    private DefaultTableModel tableModel;

    private AddItemDialog addDialog;
    private DeleteData delData;
    private TableData tableData;

    private DetailUI detailUI;
    
    private int row;
    private ExcelStart export;

    public SwingUI() {
        init();
    }

    public void init() {
        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);
        this.setTitle("PMS ���� ���� �ý���");
        this.setDefaultCloseOperation(2);
        pnMain = new JPanel(new BorderLayout()); // �����г� BorderLayout

        pnMainNorth = new JPanel(new BorderLayout()); // �����гο� NORTH�� �� ���� �����г�(���������� ��ĥ)
        pnMainNorthNorth = new JPanel(new BorderLayout());

        pnMainNorthNorth.setPreferredSize(new Dimension(200, 200));
        JLabel mainTitleLabel = new JLabel("���� ���� �ý���");
        mainTitleLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
        pnMainNorthNorth.add(mainTitleLabel, BorderLayout.NORTH);

        pnMainNorthCenter = new JPanel(new FlowLayout());

        searchTextField = new JTextField("");
        searchTextField.setPreferredSize(new Dimension(200, 31));
        searchTextField.addKeyListener(new KeyAdapter() {
            /*
             * ��ȸ �Է½� �ٷ� ����Ǵ� ��ȸ ������ �̺�Ʈ
             */
            public void keyReleased(KeyEvent e) {
                String searchData = searchTextField.getText();
                TableRowSorter<AbstractTableModel> trs = new TableRowSorter<>(tableData);
                searchResultTable.setRowSorter(trs);
                trs.setRowFilter(RowFilter.regexFilter(searchData));                //��ȸ�ʵ忡 �Է��� ���� �������� ���

            }
        });

        pnMainNorthCenter.add(searchTextField);

        searchBtn = new JButton("��ȸ");
        searchBtn.setPreferredSize(new Dimension(60, 30));
        pnMainNorthCenter.add(searchBtn);

        // BorderLayour.North �� North(Label), Center(TextField,Btn) ���
        pnMainNorth.add(pnMainNorthNorth, BorderLayout.NORTH);
        pnMainNorth.add(pnMainNorthCenter, BorderLayout.CENTER);

        pnMainCenter = new JPanel(new BorderLayout());
        pnMainCenterNorth = new JPanel(new BorderLayout());
        pnMainCenterCenter = new JPanel(new BorderLayout());

        JLabel searchResultLabel = new JLabel("��� ��ȸ ���̺�");
        searchResultLabel.setHorizontalAlignment(JLabel.HORIZONTAL);

        pnMainCenterNorth.add(searchResultLabel);
        pnMainCenter.add(pnMainCenterNorth, BorderLayout.NORTH);

        tableData = new TableData();
        searchResultTable = new JTable(tableData);
        searchResultTable.setRowHeight(30);
        searchResultTable.setFont(new Font("Sansserif", Font.BOLD, 16));
        
        searchResultTable.getColumnModel().getColumn(1).setCellRenderer(new CheckBoxRenderer());
        searchResultTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        searchResultTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               row = searchResultTable.getSelectedRow();
                System.out.println("�ο��ε��� : " + row);
                if (row != -1) {
                    String itemCode = tableData.getValueAt(row, 0).toString();
                    String itemName = tableData.getValueAt(row, 1).toString();
                    String itemGTIN = tableData.getValueAt(row, 2).toString();
                    String itemComment = tableData.getValueAt(row, 3).toString();

                    detailUI = new DetailUI(itemCode, itemName, itemGTIN, itemComment, row, tableData);
                }
            }

        });

        JTableHeader header = searchResultTable.getTableHeader();
        header.setBackground(new Color(92, 179, 255));
        JScrollPane scrTable = new JScrollPane(searchResultTable);
        pnMainCenter.add(scrTable,BorderLayout.CENTER);

        // �߰�, ���� Btn ��ġ ����
        pnMainSouth = new JPanel(new BorderLayout());
        pnMainSouth.setPreferredSize(new Dimension(100, 100));
        pnMainSouthSouth = new JPanel(new FlowLayout());
        pnMainSouthSouth.setPreferredSize(new Dimension(300, 300));
        pnMainSouth.add(pnMainSouthSouth, BorderLayout.EAST);
        addBtn = new JButton("�� ��");
        addBtn.addActionListener(new ActionListener() {
            /*
             * �߰� ��ư Ŭ�� ��
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                addDialog = new AddItemDialog(tableData);
                tableData.fireTableDataChanged();
                int lastIdx = searchResultTable.getRowCount()-1;
                if(0<=lastIdx) {
                    searchResultTable.scrollRectToVisible(searchResultTable.getCellRect(lastIdx, 0, true));
                }
//                TableRowSorter<AbstractTableModel> trs = new TableRowSorter<>(tableData);
//                searchResultTable.setRowSorter(trs);
//                trs.setRowFilter(RowFilter.regexFilter(searchTextField.getText())); // ���� ���� ����
            }
        });
        delBtn = new JButton("�� ��");
        delBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                delData = new DeleteData(row, tableData);
            }
        });
        exportBtn = new JButton("EXPORT");
        exportBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                   try {
                    export = new ExcelStart(searchResultTable);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                    
            }
        });
        pnMainSouthSouth.add(addBtn);
        pnMainSouthSouth.add(delBtn);
        pnMainSouthSouth.add(exportBtn);

        pnMain.add(pnMainNorth, BorderLayout.NORTH);
        pnMain.add(pnMainCenter, BorderLayout.CENTER);
        pnMain.add(pnMainSouth, BorderLayout.SOUTH);

        this.add(pnMain); // FRAME�� ���� PANEL �ֱ�
        this.setVisible(true);

    }
    
    

    public static void main(String[] args) {
        new SwingUI();

    }

}
