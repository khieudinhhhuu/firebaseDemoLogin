package com.khieuthichien.firebasedemo.model;

public class Photo {
    private String mName;
    private String mImageUrl;
    private String mKey;

    public Photo() {
    }

    public Photo(String mName, String mImageUrl) {
        if (mName.equals("")){
            mName = "";
        }
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
