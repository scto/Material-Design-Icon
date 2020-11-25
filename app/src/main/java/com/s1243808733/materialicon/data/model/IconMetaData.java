package com.s1243808733.materialicon.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IconMetaData implements Parcelable,Comparable<IconMetaData>, Serializable {

	private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("codepoint")
    private String codepoint;

    @SerializedName("aliases")
    private List<String> aliases;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("author")
    private String author;

    @SerializedName("version")
    private String version;

    public IconMetaData() {
    }

    protected IconMetaData(final Parcel in) {
        id = in.readString();
        name = in.readString();
        codepoint = in.readString();
        in.readStringList(aliases = new ArrayList<>());
        in.readStringList(tags = new ArrayList<>());
        author = in.readString();
        version = in.readString();
    }

    public IconMetaData setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public IconMetaData setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public IconMetaData setCodepoint(String codepoint) {
        this.codepoint = codepoint;
        return this;
    }

    public String getCodepoint() {
        return codepoint;
    }

    public IconMetaData setAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public IconMetaData setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public IconMetaData setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public IconMetaData setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getVersion() {
        return version;
    }

	@Override
    public int compareTo(IconMetaData object) {
        return name.compareTo(object.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof IconMetaData)) return false;
        IconMetaData rhs = (IconMetaData)obj;
        if (id != null && !id.equals(rhs.id)) return false;
        if (name != null && !name.equals(rhs.name)) return false;
        if (codepoint != null && !codepoint.equals(rhs.codepoint)) return false;
        if (aliases != null && !aliases.equals(rhs.aliases)) return false;
        if (tags != null && !tags.equals(rhs.tags)) return false;
        if (author != null && !author.equals(rhs.author)) return false;
        if (version != null && !version.equals(rhs.version)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.hashCode());
        result = 31 * result + (name == null ? 0 : name.hashCode());
        result = 31 * result + (codepoint == null ? 0 : codepoint.hashCode());
        result = 31 * result + (aliases == null ? 0 : aliases.hashCode());
        result = 31 * result + (tags == null ? 0 : tags.hashCode());
        result = 31 * result + (author == null ? 0 : author.hashCode());
        result = 31 * result + (version == null ? 0 : version.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "IconMetaData{"
			+ "id=" + id
			+ ", name=" + name
			+ ", codepoint=" + codepoint
			+ ", aliases=" + aliases
			+ ", tags=" + tags
			+ ", author=" + author
			+ ", version=" + version 
			+ "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(codepoint);
        dest.writeStringList(aliases);
        dest.writeStringList(tags);
        dest.writeString(author);
        dest.writeString(version);
    }

    public static final Creator<IconMetaData> CREATOR = new IconMetaDataCreator();

    public static final class IconMetaDataCreator implements Creator<IconMetaData> {

        @SuppressWarnings("unchecked")
        @Override
        public IconMetaData createFromParcel(Parcel source) {
            return new IconMetaData(source);
        }

        @Override
        public IconMetaData[] newArray(int size) {
            return new IconMetaData[size];
        }

    }

}
