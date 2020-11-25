package com.s1243808733.materialicon.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class OpenSourceLicense implements Parcelable {

    @SerializedName("name")
    private String name;

    @SerializedName("author")
    private String author;

    @SerializedName("license")
    private String license;

    @SerializedName("describes")
    private String describes;

    @SerializedName("url")
    private String url;

    public OpenSourceLicense() {
    }

    protected OpenSourceLicense(final Parcel in) {
        name = in.readString();
        author = in.readString();
        license = in.readString();
        describes = in.readString();
        url = in.readString();
    }

    public OpenSourceLicense setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public OpenSourceLicense setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public OpenSourceLicense setLicense(String license) {
        this.license = license;
        return this;
    }

    public String getLicense() {
        return license;
    }

    public OpenSourceLicense setDescribes(String describes) {
        this.describes = describes;
        return this;
    }

    public String getDescribes() {
        return describes;
    }

    public OpenSourceLicense setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(license);
        dest.writeString(describes);
        dest.writeString(url);
    }

    public static final Creator<OpenSourceLicense> CREATOR = new OpenSourceLicenseCreator();

    public static final class OpenSourceLicenseCreator implements Creator<OpenSourceLicense> {

        @SuppressWarnings("unchecked")
        @Override
        public OpenSourceLicense createFromParcel(Parcel source) {
            return new OpenSourceLicense(source);
        }

        @Override
        public OpenSourceLicense[] newArray(int size) {
            return new OpenSourceLicense[size];
        }

    }

}
