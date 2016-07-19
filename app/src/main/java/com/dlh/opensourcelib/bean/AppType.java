package com.dlh.opensourcelib.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.bmob.v3.BmobObject;

/**
 * Created by admin on 2016/7/19.Pro_type
 */
@DatabaseTable(tableName = AppType.table)
public class AppType extends BmobObject {
    public static final String table = "Pro_type";
    @DatabaseField(columnName = "type_id", id = true)
    private Integer type_id;
    @DatabaseField(columnName = "type_title")
    private String type_title;
    public AppType() {
        setTableName(table);
    }

    public AppType(String tableName) {
        super(tableName);
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_title() {
        return type_title;
    }

    public void setType_title(String type_title) {
        this.type_title = type_title;
    }
}
