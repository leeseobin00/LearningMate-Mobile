package com.example.learningmate;

public class Library {
    String libraryName;
    String fileName;

    public Library(String libraryName, String fileName){
        this.libraryName = libraryName;
        this.fileName = fileName;
    }

    public String getLibraryNamey() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String libraryName) {
        this.fileName = fileName;
    }
}

