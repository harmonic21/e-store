package ru.simple.electronic.store.dto;

public class FiltrationDto {
    private Integer pageNum = 0;
    private Integer pageSize = 10;
    private String keyWord;
    private boolean priceSortAsc;
    private boolean abcSortAsc;
    private boolean priceSortDesc;
    private boolean abcSortDesc;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public boolean isPriceSortAsc() {
        return priceSortAsc;
    }

    public void setPriceSortAsc(boolean priceSortAsc) {
        this.priceSortAsc = priceSortAsc;
    }

    public boolean isAbcSortAsc() {
        return abcSortAsc;
    }

    public void setAbcSortAsc(boolean abcSortAsc) {
        this.abcSortAsc = abcSortAsc;
    }

    public boolean isPriceSortDesc() {
        return priceSortDesc;
    }

    public void setPriceSortDesc(boolean priceSortDesc) {
        this.priceSortDesc = priceSortDesc;
    }

    public boolean isAbcSortDesc() {
        return abcSortDesc;
    }

    public void setAbcSortDesc(boolean abcSortDesc) {
        this.abcSortDesc = abcSortDesc;
    }
}
