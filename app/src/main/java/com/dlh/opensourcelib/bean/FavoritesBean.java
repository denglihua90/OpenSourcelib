package com.dlh.opensourcelib.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/5/10
 */
@DatabaseTable(tableName = "FavoritesBean")
public class FavoritesBean implements Serializable {
    @DatabaseField(columnName = "proID", id = true)
    private int proID;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "desc")
    private String desc;
    @DatabaseField(columnName = "gitHub")
    private String gitHub;
    @DatabaseField(columnName = "thumbFileURL")
    private String thumbFileURL;
    @DatabaseField(columnName = "plunURL")
    private String plunURL;
    @DatabaseField(columnName = "type")
    private int type;
    @DatabaseField(columnName = "packageInfo")
    private String packageInfo;

    public FavoritesBean() {
        // needed by ormlite
    }

    public Integer getProID() {
        return proID;
    }

    public void setProID(Integer proID) {
        this.proID = proID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGitHub() {
        return gitHub;
    }

    public void setGitHub(String gitHub) {
        this.gitHub = gitHub;
    }

    public String getThumbFileURL() {
        return thumbFileURL;
    }

    public void setThumbFileURL(String thumbFileURL) {
        this.thumbFileURL = thumbFileURL;
    }

    public String getPlunURL() {
        return plunURL;
    }

    public void setPlunURL(String plunURL) {
        this.plunURL = plunURL;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
    }
}
