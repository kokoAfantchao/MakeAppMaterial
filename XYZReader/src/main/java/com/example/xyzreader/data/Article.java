package com.example.xyzreader.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.xyzreader.data.source.local.ItemsContract;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nestor on 2/27/18.
 */

public class Article implements Parcelable {
    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
    @SerializedName("id")
    private String _ID;
    private String SERVER_ID;
    @SerializedName("title")
    private String TITLE;
    @SerializedName("author")
    private String AUTHOR;
    @SerializedName("body")
    private String BODY;
    @SerializedName("thumb")
    private String THUMB_URL;
    @SerializedName("photo")
    private String PHOTO_URL;
    @SerializedName("aspect_ratio")
    private String ASPECT_RATIO;
    @SerializedName("published_date")
    private String PUBLISHED_DATE;

    public Article() {

    }

    protected Article(Parcel in) {
        _ID = in.readString();
        SERVER_ID = in.readString();
        TITLE = in.readString();
        AUTHOR = in.readString();
        BODY = in.readString();
        THUMB_URL = in.readString();
        PHOTO_URL = in.readString();
        ASPECT_RATIO = in.readString();
        PUBLISHED_DATE = in.readString();
    }

    public static Article valueOf(@NonNull ContentValues contentValues) {


        return null;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getSERVER_ID() {
        return SERVER_ID;
    }

    public void setSERVER_ID(String SERVER_ID) {
        this.SERVER_ID = SERVER_ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getAUTHOR() {
        return AUTHOR;
    }

    public void setAUTHOR(String AUTHOR) {
        this.AUTHOR = AUTHOR;
    }

    public String getBODY() {
        return BODY;
    }

    public void setBODY(String BODY) {
        this.BODY = BODY;
    }

    public String getTHUMB_URL() {
        return THUMB_URL;
    }

    public void setTHUMB_URL(String THUMB_URL) {
        this.THUMB_URL = THUMB_URL;
    }

    public String getPHOTO_URL() {
        return PHOTO_URL;
    }

    public void setPHOTO_URL(String PHOTO_URL) {
        this.PHOTO_URL = PHOTO_URL;
    }

    public String getASPECT_RATIO() {
        return ASPECT_RATIO;
    }

    public void setASPECT_RATIO(String ASPECT_RATIO) {
        this.ASPECT_RATIO = ASPECT_RATIO;
    }

    public String getPUBLISHED_DATE() {
        return PUBLISHED_DATE;
    }

    public void setPUBLISHED_DATE(String PUBLISHED_DATE) {
        this.PUBLISHED_DATE = PUBLISHED_DATE;
    }

    public ContentValues getContentValue() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemsContract.Items.TITLE, this.TITLE);
        contentValues.put(ItemsContract.Items.PUBLISHED_DATE, this.PUBLISHED_DATE);
        contentValues.put(ItemsContract.Items.AUTHOR, this.AUTHOR);
        contentValues.put(ItemsContract.Items.THUMB_URL, this.THUMB_URL);
        contentValues.put(ItemsContract.Items.PHOTO_URL, this.PHOTO_URL);
        contentValues.put(ItemsContract.Items.ASPECT_RATIO, this.ASPECT_RATIO);
        contentValues.put(ItemsContract.Items.BODY, this.BODY);
        return contentValues;
    }

    public void reduceBodyLength() {
        this.BODY = this.BODY.substring(0, 500);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_ID);
        dest.writeString(SERVER_ID);
        dest.writeString(TITLE);
        dest.writeString(AUTHOR);
        dest.writeString(BODY);
        dest.writeString(THUMB_URL);
        dest.writeString(PHOTO_URL);
        dest.writeString(ASPECT_RATIO);
        dest.writeString(PUBLISHED_DATE);
    }

}
