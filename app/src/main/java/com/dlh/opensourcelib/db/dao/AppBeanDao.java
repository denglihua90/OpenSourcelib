package com.dlh.opensourcelib.db.dao;

import com.dlh.opensourcelib.OpensourceLibApplication;
import com.dlh.opensourcelib.bean.AppBean;
import com.dlh.opensourcelib.bean.FavoritesBean;
import com.dlh.opensourcelib.db.SQLiteOrmWithCipherHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/5/30
 */
public class AppBeanDao {

    private SQLiteOrmWithCipherHelper helper = null;
    private Dao<AppBean, Integer> dao;
    private static AppBeanDao favoritesBeanDao = null;

    public static AppBeanDao getDao() {
        if (favoritesBeanDao == null) {
            favoritesBeanDao = new AppBeanDao();
        }
        return favoritesBeanDao;
    }

    public AppBeanDao() {

        helper = SQLiteOrmWithCipherHelper.getHelper(OpensourceLibApplication.application);
        helper.getWritableDatabase(SQLiteOrmWithCipherHelper.DATABASE_PASSWORD);
        try {
            dao = helper.getDao(AppBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(AppBean t) {
        try {
            dao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean getFavoritesBeanByID(int id) {
        boolean isexit = false;
        try {
            AppBean bean = dao.queryForId(id);
            if (bean != null) {
                isexit = true;
            } else {
                isexit = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isexit;
    }

    public int deleteByID(int id) {
        int ID = -1;
        try {
            ID = dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ID;
    }

    public List<AppBean> queryAll() {
        List<AppBean> list = new ArrayList<>();
        try {
            list = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
