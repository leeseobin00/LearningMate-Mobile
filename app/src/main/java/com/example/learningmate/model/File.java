package com.example.learningmate.model;

public class File {
    private String fileId;
    private String uploader;
    private int category;
    private String uploadDate;
    private String fileName;
    private String hashFileName;
    private String fileUrl;
    private String fileSize;

    public File(String fileId, String uploader, int category, String uploadDate, String fileName, String hashFileName, String fileUrl, String fileSize) {
        this.fileId = fileId;
        this.uploader = uploader;
        this.category = category;
        this.uploadDate = uploadDate;
        this.fileName = fileName;
        this.hashFileName = hashFileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHashFileName() {
        return hashFileName;
    }

    public void setHashFileName(String hashFileName) {
        this.hashFileName = hashFileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}