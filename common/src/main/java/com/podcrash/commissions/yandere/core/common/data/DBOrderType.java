package com.podcrash.commissions.yandere.core.common.data;

public enum DBOrderType {
    DATE_ASCENDING(1, "&aFecha Ascendente"),
    DATE_DESCENDING(-1, "&cFecha Descendente"),
    ALPHABETICAL_ASCENDING(1, "&aAlfabético Ascendente"),
    ALPHABETICAL_DESCENDING(-1, "&cAlfabético Descendente");
    
    private final int order;
    private final String orderName;
    
    DBOrderType(int order, String orderName){
        this.order = order;
        this.orderName = orderName;
    }
    
    public boolean isDate(){
        return this == DATE_ASCENDING || this == DATE_DESCENDING;
    }
    
    public int getOrder(){
        return order;
    }
    
    public String getOrderName(){
        return orderName;
    }
    
    public DBOrderType getNext(){
        switch(this){
            case DATE_ASCENDING:
                return DATE_DESCENDING;
            case ALPHABETICAL_ASCENDING:
                return ALPHABETICAL_DESCENDING;
            case DATE_DESCENDING:
                return DATE_ASCENDING;
            default:
                return ALPHABETICAL_ASCENDING;
        }
    }
    
    public DBOrderType getPrevious(){
        switch(this){
            case DATE_ASCENDING:
                return DATE_DESCENDING;
            case DATE_DESCENDING:
                return DATE_ASCENDING;
            case ALPHABETICAL_ASCENDING:
                return ALPHABETICAL_DESCENDING;
            default:
                return ALPHABETICAL_ASCENDING;
        }
    }
}
