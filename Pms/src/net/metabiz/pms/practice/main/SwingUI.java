package net.metabiz.pms.practice.main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import net.metabiz.pms.practice.crud.AddItemDialog;
import net.metabiz.pms.practice.crud.DeleteData;
import net.metabiz.pms.practice.crud.DetailUI;
import net.metabiz.pms.practice.crud.TableData;
import net.metabiz.pms.practice.excel.ExcelStart;
import net.metabiz.pms.practice.uirenderer.CheckBoxEditor;
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
    private CheckBoxRenderer checkBoxRenderer;
    private CheckBoxEditor checkBoxEditor;

    private JTextField itemCodeFilter;
    private JTextField itemNameFilter;
    private JTextField gtinFilter;
    private JTextField commentFilter;

    public SwingUI() {
        init();
    }

    public void init() {
        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);
        this.setTitle("PMS 자재 관리 시스템");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pnMain = new JPanel(new BorderLayout(10, 10)); // 여백을 추가하여 컴포넌트 간 간격을 만들기

        // 상단 - 필터 영역
        pnMainNorth = new JPanel(new BorderLayout(10, 10));
        pnMainNorthNorth = new JPanel(new BorderLayout());
        pnMainNorthCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel mainTitleLabel = new JLabel("자재 관리 시스템");
        mainTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainTitleLabel.setForeground(new Color(0, 51, 102)); // 타이틀 색상
        pnMainNorthNorth.add(mainTitleLabel, BorderLayout.NORTH);

        pnMainNorthCenter.add(new JLabel("자재코드:"));
        itemCodeFilter = new JTextField(15);
        styleTextField(itemCodeFilter);
        itemCodeFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(itemCodeFilter);

        pnMainNorthCenter.add(new JLabel("자재명:"));
        itemNameFilter = new JTextField(15);
        styleTextField(itemNameFilter);
        itemNameFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(itemNameFilter);

        pnMainNorthCenter.add(new JLabel("GTIN:"));
        gtinFilter = new JTextField(15);
        styleTextField(gtinFilter);
        gtinFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(gtinFilter);

        pnMainNorthCenter.add(new JLabel("비고:"));
        commentFilter = new JTextField(15);
        styleTextField(commentFilter);
        commentFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(commentFilter);

        pnMainNorth.add(pnMainNorthNorth, BorderLayout.NORTH);
        pnMainNorth.add(pnMainNorthCenter, BorderLayout.CENTER);

        // 중앙 - 테이블 영역
        pnMainCenter = new JPanel(new BorderLayout(10, 10));
        pnMainCenterNorth = new JPanel(new BorderLayout());
        JLabel searchResultLabel = new JLabel("결과 조회 테이블");
        searchResultLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        pnMainCenterNorth.add(searchResultLabel, BorderLayout.NORTH);
        pnMainCenter.add(pnMainCenterNorth, BorderLayout.NORTH);

        tableData = new TableData();
        searchResultTable = new JTable(tableData);
        
        // 테이블에 각 열 크기 및 체크박스 설정 유지
        searchResultTable.setRowHeight(30); // 각 행 높이 조정
        searchResultTable.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(40); // 체크박스 열 크기
        for (int i = 1; i < searchResultTable.getColumnCount(); i++) {
            searchResultTable.getColumnModel().getColumn(i).setPreferredWidth(300); // 다른 열 크기
        }

        // 체크박스 렌더러와 에디터 설정을 그대로 유지
        checkBoxRenderer = new CheckBoxRenderer();
        checkBoxEditor = new CheckBoxEditor(tableData);
        searchResultTable.getColumnModel().getColumn(0).setCellRenderer(checkBoxRenderer);
        searchResultTable.getColumnModel().getColumn(0).setCellEditor(checkBoxEditor);

        // 클릭 이벤트 처리
        searchResultTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 클릭한 셀의 컬럼이 0(체크박스 열)일 경우 이벤트 처리하지 않음
                if (searchResultTable.getSelectedColumn() == 0) {
                    return; // 체크박스를 클릭한 경우 이벤트 무시
                }

                row = searchResultTable.getSelectedRow();
                if (row != -1) {
                    String itemCode = tableData.getValueAt(row, 1).toString();
                    String itemName = tableData.getValueAt(row, 2).toString();
                    String itemGTIN = tableData.getValueAt(row, 3).toString();
                    String itemComment = tableData.getValueAt(row, 4).toString();

                    // 상세 정보를 보여주는 UI 호출
                    detailUI = new DetailUI(itemCode, itemName, itemGTIN, itemComment, row, tableData);
                }
            }
        });

        JScrollPane scrTable = new JScrollPane(searchResultTable);
        pnMainCenter.add(scrTable, BorderLayout.CENTER);

        // 하단 - 버튼 영역
        pnMainSouth = new JPanel(new BorderLayout(10, 10));
        pnMainSouthSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        addBtn = new JButton("추 가");
        styleButton(addBtn);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDialog = new AddItemDialog(tableData);
                tableData.fireTableDataChanged();
                int lastIdx = searchResultTable.getRowCount() - 1;
                if (0 <= lastIdx) {
                    searchResultTable.scrollRectToVisible(searchResultTable.getCellRect(lastIdx, 0, true));
                }
            }
        });

        delBtn = new JButton("삭 제");
        styleButton(delBtn);
        delBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delData = new DeleteData(tableData);
            }
        });

        exportBtn = new JButton("EXPORT");
        styleButton(exportBtn);
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

        pnMainSouth.add(pnMainSouthSouth, BorderLayout.CENTER);
        pnMain.add(pnMainNorth, BorderLayout.NORTH);
        pnMain.add(pnMainCenter, BorderLayout.CENTER);
        pnMain.add(pnMainSouth, BorderLayout.SOUTH);

        this.add(pnMain);
        this.setVisible(true);
    }

    private KeyListener createKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilter();
            }
        };
    }

    private void applyFilter() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableData);
        searchResultTable.setRowSorter(sorter);

        // 순서 상관없이 텍스트 필드에 입력하는 값 을 += 해준다는 느낌.
        List<RowFilter<TableModel, Object>> filters = new ArrayList<>();
        filters.add(RowFilter.regexFilter(getItemCodeFilter(), 1));
        filters.add(RowFilter.regexFilter(getItemNameFilter(), 2));
        filters.add(RowFilter.regexFilter(getGtinFilter(), 3));
        filters.add(RowFilter.regexFilter(getCommentFilter(), 4));

        RowFilter<TableModel, Object> rowFilter = RowFilter.andFilter(filters);
        sorter.setRowFilter(rowFilter);
    }

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

    private void styleTextField(JTextField textField) {
        textField.setBackground(new Color(240, 240, 240));
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(92, 179, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(72, 159, 235));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(92, 179, 255));
            }
        });
    }

    public static void main(String[] args) {
        new SwingUI();
    }
}
