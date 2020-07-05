package com.example.orthogonaltable.bean;

//L行数(水平数^因素数）
//水平数:取值的个数
//因素数:变量的个数
public class UniFormTable {
    private int mRuns;//行数，即实验的次数
    private int mFactorLevelCount;//如果等于1，则是等水平正交表
    private int[] mFactors;//因素数
    private int[] mLevels;//水平数
    private String [][] mTableMatrix;//正交矩阵表数据
    private String[] mTableMatrixString;//整块的正交矩阵表数据

    public int getmRuns() {
        return mRuns;
    }

    public void setmRuns(int mRuns) {
        this.mRuns = mRuns;
    }

    public int getmFactorLevelCount() {
        return mFactorLevelCount;
    }

    public void setmFactorLevelCount(int mFactorLevelCount) {
        this.mFactorLevelCount = mFactorLevelCount;
    }

    public int[] getmFactors() {
        return mFactors;
    }

    public void setmFactors(int[] mFactors) {
        this.mFactors = mFactors;
    }

    public int[] getmLevels() {
        return mLevels;
    }

    public void setmLevels(int[] mLevels) {
        this.mLevels = mLevels;
    }

    public String[][] getmTableMatrix() {
        return mTableMatrix;
    }

    public void setmTableMatrix(String[][] mTableMatrix) {
        this.mTableMatrix = mTableMatrix;
    }

    public String[] getmTableMatrixString() {
        return mTableMatrixString;
    }

    public void setmTableMatrixString(String[] mTableMatrixString) {
        this.mTableMatrixString = mTableMatrixString;
    }


}
