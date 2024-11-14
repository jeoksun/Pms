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
    
    private JTextField itemCodeFilter; // 자재코드 텍스트필드
    private JTextField itemNameFilter; // 자재명 텍스트필드
    private JTextField gtinFilter; // GTIN 텍스트필드
    private JTextField commentFilter; // 비고 텍스트필드
    private JTable searchResultTable;
    private TableData tableData; // 테이블 데이터 가져오는 객체
    private int row; // Table row 추적용 전역 변수
    private AddDialog addDialog; // 자재 추가 시 호출 객체
    private DeleteDiaglog delData; // 자재 삭제 시 호출 객체
    private DetailWithUpdateDialog detailUI; // Row 클릭 시 상세 객체 (수정까지)
    private ExcelExportHandler export; // Export btn 클릭시 호출(엑셀파일 export) 객체
    private CheckBoxRenderer checkBoxRenderer; // CheckBox 랜더링
    private CheckBoxEditor checkBoxEditor; // CheckBox 편집
    
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
        this.setTitle("PMS 자재 관리 시스템");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pnMain = new JPanel(new BorderLayout(10, 10)); // 여백을 추가하여 컴포넌트 간 간격을 만들기

        /**
         * 상단 필터 영역
         * 상단 Layout 지정
         */
        JPanel pnMainNorth = new JPanel(new BorderLayout(10, 10));
        JPanel pnMainNorthNorth = new JPanel(new BorderLayout());
        JPanel pnMainNorthCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        /**
         * 상단 메인 타이틀 Label
         * pnMainNorthNorth 에는 메인 타이틀 **Label**만 들어감
         */
        JLabel mainTitleLabel = new JLabel("자재 관리 시스템");
        mainTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainTitleLabel.setForeground(new Color(0, 51, 102)); // 타이틀 색상
        pnMainNorthNorth.add(mainTitleLabel, BorderLayout.NORTH);

        /**
         * pnMainNorthCenter
         * 조회 라벨 및 TextField, 이벤트
         */
        pnMainNorthCenter.add(new JLabel("자재코드:"));
        itemCodeFilter = new JTextField(15);
        SwingStyle.textFieldStyleChanger(itemCodeFilter);
        itemCodeFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(itemCodeFilter);

        pnMainNorthCenter.add(new JLabel("자재명:"));
        itemNameFilter = new JTextField(15);
        SwingStyle.textFieldStyleChanger(itemNameFilter);
        itemNameFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(itemNameFilter);

        pnMainNorthCenter.add(new JLabel("GTIN:"));
        gtinFilter = new JTextField(15);
        SwingStyle.textFieldStyleChanger(gtinFilter);
        gtinFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(gtinFilter);

        pnMainNorthCenter.add(new JLabel("비고:"));
        commentFilter = new JTextField(15);
        SwingStyle.textFieldStyleChanger(commentFilter);
        commentFilter.addKeyListener(createKeyListener());
        pnMainNorthCenter.add(commentFilter);

        pnMainNorth.add(pnMainNorthNorth, BorderLayout.NORTH);
        pnMainNorth.add(pnMainNorthCenter, BorderLayout.CENTER);

        // 중앙 - 테이블 영역
        JPanel pnMainCenter = new JPanel(new BorderLayout(10, 10));

        JPanel pnMainCenterNorth = new JPanel(new BorderLayout());
        JLabel searchResultLabel = new JLabel("결과 조회 테이블");
        searchResultLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        pnMainCenterNorth.add(searchResultLabel, BorderLayout.NORTH);

        pnMainCenter.add(pnMainCenterNorth, BorderLayout.NORTH);

        /**
         * 테이블 데이터 초기화 후 테이블 생성
         */
        tableData = new TableData();
        searchResultTable = new JTable(tableData);

        // 테이블에 각 열 크기 및 체크박스 설정 유지
        searchResultTable.setRowHeight(30); // 각 행 높이 조정
        searchResultTable.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(40); // 체크박스 width 크기
        for (int i = 1; i < searchResultTable.getColumnCount(); i++) {// 다른컬럼 width 크기
            searchResultTable.getColumnModel().getColumn(i).setPreferredWidth(300);
        }

        /**
         * CheckBox관련 렌터러 에디터 초기화
         * 0번째 컬럼 {""} 에게 체크박스 부여
         */
        checkBoxRenderer = new CheckBoxRenderer();
        checkBoxEditor = new CheckBoxEditor(tableData);
        searchResultTable.getColumnModel().getColumn(0).setCellRenderer(checkBoxRenderer);
        searchResultTable.getColumnModel().getColumn(0).setCellEditor(checkBoxEditor);

        /**
         * 체크박스 외 셀 클릭시 이벤트
         */
        searchResultTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cellClickEvent();
            }
        });

        /**
         * 테이블 스크롤팬으로 감싸기
         */
        JScrollPane scrTable = new JScrollPane(searchResultTable);
        pnMainCenter.add(scrTable, BorderLayout.CENTER);

        /**
         * 하단 버튼 영역
         */
        JPanel pnMainSouth = new JPanel(new BorderLayout(10, 10));
        JPanel pnMainSouthSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        /**
         * 추가 이벤트
         */
        JButton addBtn = new JButton("추 가");
        SwingStyle.btnStyleChanger(addBtn);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowAddEvent();
            }
        });

        /**
         * 삭제 이벤트
         */
        JButton delBtn = new JButton("삭 제");
        SwingStyle.btnStyleChanger(delBtn);
        delBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delData = new DeleteDiaglog(tableData);
            }
        });

        /**
         * 엑셀 export 이벤트
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
         * 버튼 추가
         */
        pnMainSouthSouth.add(addBtn);
        pnMainSouthSouth.add(delBtn);
        pnMainSouthSouth.add(exportBtn);

        pnMainSouth.add(pnMainSouthSouth, BorderLayout.CENTER); // 메인인 하단 메인인 south에 버튼넣기
        /**
         * 메인에 노스, 센터, 사우스 담기
         */
        pnMain.add(pnMainNorth, BorderLayout.NORTH);
        pnMain.add(pnMainCenter, BorderLayout.CENTER);
        pnMain.add(pnMainSouth, BorderLayout.SOUTH);

        /**
         * 위에서 노스, 센터, 사우스가 담긴 main을 frame에 추가 시키면서 완성
         */
        this.add(pnMain);
        this.setVisible(true);
        /************
         * 렌더링 끝*
         ***********/
    }

    /**
     * CheckBox 제외 다른 컬럼 셀 클릭시 발생 이벤트
     */
    private void cellClickEvent() {

        if (searchResultTable.getSelectedColumn() == 0) { // 클릭한 셀의 컬럼이 0(체크박스 열)일 경우 이벤트 처리하지 않음
            return; // 체크박스를 클릭한 경우 바로 리턴시켜서 아무일도없게
        }

        row = searchResultTable.getSelectedRow(); // default가 -1, checkbox가 0
        if (row != -1) {
            /**
             * 각각의 row에 값을 가져와서 저장 후 detail객체에 뿌림
             */
            String itemCode = tableData.getValueAt(row, 1).toString();
            String itemName = tableData.getValueAt(row, 2).toString();
            String itemGTIN = tableData.getValueAt(row, 3).toString();
            String itemComment = tableData.getValueAt(row, 4).toString();

            detailUI = new DetailWithUpdateDialog(itemCode, itemName, itemGTIN, itemComment, row, tableData);
        }
    }

    /**
     * 로우 추가 이벤트
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
     * 텍스트필드에 입력할때마다 이벤트 발생
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
     * 키입력 이벤트 발생의 주체
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
         * andFilter에 List를 파라미터로 보내 모든컬럼 인덱스와  
         * 입력한값에 대해서 일치하는 데이터를 반환하고 보여줌
         */
        RowFilter<TableModel, Object> rowFilter = RowFilter.andFilter(filters);
        sorter.setRowFilter(rowFilter);
    }

    /**
     * 트림 처서 공백 없애서 리턴값을 조회 텍스트 필드에 적용
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
