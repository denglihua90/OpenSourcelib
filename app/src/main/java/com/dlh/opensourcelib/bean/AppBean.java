package com.dlh.opensourcelib.bean;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/4/16
 */
@DatabaseTable(tableName = AppBean.table)
public class AppBean extends BmobObject {
    public static final String table = "Pro_table";
    @DatabaseField(columnName = "proID", id = true)
    private Integer proID;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "desc")
    private String desc;
    @DatabaseField(columnName = "gitHub")
    private String gitHub;
    private BmobFile thumbFile;
    private BmobFile plun;
    @DatabaseField(columnName = "thumbFileURL")
    private String thumbFileURL;
    @DatabaseField(columnName = "plunURL")
    private String plunURL;
    @DatabaseField(columnName = "type")
    private Integer type;
    @DatabaseField(columnName = "packageInfo")
    private String packageInfo;

    public BmobFile getPlun() {
        return plun;
    }

    public void setPlun(BmobFile plun) {
        this.plun = plun;
    }

    public Integer getProID() {
        return proID;
    }

    public void setProID(Integer proID) {
        this.proID = proID;
    }

    public AppBean() {
        setTableName(table);
    }

    public AppBean(String tableName) {
        super(tableName);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public BmobFile getThumbFile() {
        return thumbFile;
    }

    public void setThumbFile(BmobFile thumbFile) {
        this.thumbFile = thumbFile;
    }

    public String getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
