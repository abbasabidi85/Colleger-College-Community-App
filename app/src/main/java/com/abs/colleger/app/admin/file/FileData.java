package com.abs.colleger.app.admin.file;

public class FileData {
    private String pdfTitle, pdfUrl, key, category;

    public FileData(){

    }

    public FileData(String pdfTitle, String pdfUrl, String key, String category) {
        this.pdfTitle = pdfTitle;
        this.pdfUrl = pdfUrl;
        this.key = key;
        this.category=category;
    }

    public String getpdfTitle() {
        return pdfTitle;
    }

    public void setpdfTitle(String pdfTitle) {
        this.pdfTitle = pdfTitle;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
