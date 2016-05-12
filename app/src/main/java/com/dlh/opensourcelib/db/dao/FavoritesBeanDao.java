package com.dlh.opensourcelib.db.dao;

import android.content.Context;

import com.dlh.opensourcelib.OpensourceLibApplication;
import com.dlh.opensourcelib.bean.FavoritesBean;
import com.dlh.opensourcelib.db.SQLiteOrmWithCipherHelper;
import com.j256.ormlite.dao.Dao;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/5/10
 */
public class FavoritesBeanDao {
    private SQLiteOrmWithCipherHelper helper = null;
    private Dao<FavoritesBean, Integer> dao;
    private static FavoritesBeanDao favoritesBeanDao = null;

    public static FavoritesBeanDao getDao() {
        if (favoritesBeanDao == null) {
            favoritesBeanDao = new FavoritesBeanDao();
        }
        return favoritesBeanDao;
    }

    public FavoritesBeanDao() {

        helper = SQLiteOrmWithCipherHelper.getHelper(OpensourceLibApplication.application);
        helper.getWritableDatabase(SQLiteOrmWithCipherHelper.DATABASE_PASSWORD);
        try {
            dao = helper.getDao(FavoritesBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(FavoritesBean t) {
        try {
            dao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean getFavoritesBeanByID(int id) {
        boolean isexit = false;
        try {
            FavoritesBean bean = dao.queryForId(id);
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

    public List<FavoritesBean> queryAll() {
        List<FavoritesBean> list = new ArrayList<>();
        try {
            list = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
