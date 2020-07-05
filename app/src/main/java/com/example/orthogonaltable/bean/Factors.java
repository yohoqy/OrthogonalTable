package com.example.orthogonaltable.bean;

import java.util.ArrayList;
import java.util.List;

//因素类
public class Factors {
    private String mFactorName;
    private List<String> mLevels=new ArrayList<String>();

    public String getmFactorName() {
        return mFactorName;
    }

    public void setmFactorName(String mFactorName) {
        this.mFactorName = mFactorName;
    }

    public List<String> getmLevels() {
        return mLevels;
    }

    public void setmLevels(List<String> mLevels) {
        this.mLevels = mLevels;
    }




}
