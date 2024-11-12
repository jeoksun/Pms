package net.metabiz.pms.practice.crud;

public class TableBeansBuilder {
    private TableBeans transcation;
    
    public TableBeansBuilder(TableBeans transaction) {
        this.transcation = transaction;
    }
        
    public TableBeansBuilder itemCode(String itemCode) {
        transcation.setItemCode(itemCode);
        return this;
    }
    
    public TableBeansBuilder itemName(String itemName) {
        transcation.setItemName(itemName);
        return this;
    }
    
    public TableBeansBuilder itemGTIN(long gtin) {
        transcation.setGtin(gtin);
        return this;
    }
    
    public TableBeansBuilder itemComment(String comment) {
        transcation.setItemComment(comment);
        return this;
    }
    public TableBeansBuilder checkStatus(boolean checkStatus) {
        transcation.setCheckStatus(checkStatus);;
        return this;
    }
    
    public TableBeans transcation() {
        return transcation;
    }
}
