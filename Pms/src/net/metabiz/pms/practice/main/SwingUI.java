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
        this.setTitle("PMS 자재 관리 시스템");
        this.setDefaultCloseOperation(2);
        pnMain = new JPanel(new BorderLayout()); // 메인패널 BorderLayout

        pnMainNorth = new JPanel(new BorderLayout()); // 메인패널에 NORTH로 들어갈 하위 메인패널(검은색으로 색칠)
        pnMainNorthNorth = new JPanel(new BorderLayout());

        pnMainNorthNorth.setPreferredSize(new Dimension(200, 200));
        JLabel mainTitleLabel = new JLabel("자재 관리 시스템");
        mainTitleLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
        pnMainNorthNorth.add(mainTitleLabel, BorderLayout.NORTH);

        pnMainNorthCenter = new JPanel(new FlowLayout());

        searchTextField = new JTextField("");
        searchTextField.setPreferredSize(new Dimension(200, 31));
        searchTextField.addKeyListener(new KeyAdapter() {
            /*
             * 조회 입력시 바로 적용되는 조회 데이터 이벤트
             */
            public void keyReleased(KeyEvent e) {
                String searchData = searchTextField.getText();
                TableRowSorter<AbstractTableModel> trs = new TableRowSorter<>(tableData);
                searchResultTable.setRowSorter(trs);
                trs.setRowFilter(RowFilter.regexFilter(searchData));                //조회필드에 입력한 값만 나오도록 출력

            }
        });

        pnMainNorthCenter.add(searchTextField);

        searchBtn = new JButton("조회");
        searchBtn.setPreferredSize(new Dimension(60, 30));
        pnMainNorthCenter.add(searchBtn);

        // BorderLayour.North 에 North(Label), Center(TextField,Btn) 담기
        pnMainNorth.add(pnMainNorthNorth, BorderLayout.NORTH);
        pnMainNorth.add(pnMainNorthCenter, BorderLayout.CENTER);

        pnMainCenter = new JPanel(new BorderLayout());
        pnMainCenterNorth = new JPanel(new BorderLayout());
        pnMainCenterCenter = new JPanel(new BorderLayout());

        JLabel searchResultLabel = new JLabel("결과 조회 테이블");
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
                System.out.println("로우인덱스 : " + row);
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

        // 추가, 삭제 Btn 위치 조정
        pnMainSouth = new JPanel(new BorderLayout());
        pnMainSouth.setPreferredSize(new Dimension(100, 100));
        pnMainSouthSouth = new JPanel(new FlowLayout());
        pnMainSouthSouth.setPreferredSize(new Dimension(300, 300));
        pnMainSouth.add(pnMainSouthSouth, BorderLayout.EAST);
        addBtn = new JButton("추 가");
        addBtn.addActionListener(new ActionListener() {
            /*
             * 추가 버튼 클릭 시
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
//                trs.setRowFilter(RowFilter.regexFilter(searchTextField.getText())); // 기존 필터 유지
            }
        });
        delBtn = new JButton("삭 제");
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

        this.add(pnMain); // FRAME에 메인 PANEL 넣기
        this.setVisible(true);

    }
    
    

    public static void main(String[] args) {
        new SwingUI();

    }

}
