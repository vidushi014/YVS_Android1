package com.example.voice_notes;

import android.widget.ImageView;

public class CategoryModel {

//    variable creation

    private String docID;
    private String name;
    private int noOfTests;
//    private ImageView catIcon;

//    constructor
    public CategoryModel(String docID, String name, int noOfTests) {
        this.docID = docID;
        this.name = name;
        this.noOfTests = noOfTests;
    }

//    getters and setters


    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfTests() {
        return noOfTests;
    }

    public void setNoOfTests(int noOfTests) {
        this.noOfTests = noOfTests;
    }

//    public ImageView getCatIcons() {
//         catIcon.setImageResource(R.drawable.devil);
//         return catIcon;
//    }
}
