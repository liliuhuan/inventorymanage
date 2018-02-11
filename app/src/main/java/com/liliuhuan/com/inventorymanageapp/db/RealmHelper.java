package com.liliuhuan.com.inventorymanageapp.db;


import com.liliuhuan.com.inventorymanageapp.bean.MainBean;
import com.liliuhuan.com.inventorymanageapp.bean.ProductBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Description: RealmHelper
 * Creator: yxc
 * date: 2016/9/21 17:46
 */

public class RealmHelper {

    public static final String DB_NAME = "teacherDb";
    private Realm mRealm;
    private static RealmHelper instance;

    private RealmHelper() {
    }

    public static RealmHelper getInstance() {
        if (instance == null) {
            synchronized (RealmHelper.class) {
                if (instance == null)
                    instance = new RealmHelper();
            }
        }
        return instance;
    }


    protected Realm getRealm() {
        if (mRealm == null || mRealm.isClosed())
            mRealm = Realm.getDefaultInstance();
        return mRealm;
    }


    //--------------------------------------------------标签相关----------------------------------------------------

    /**
     * 增加标签
     *
     * @param bean
     * @param maxSize 保存最大数量
     */
    public void insertTag(TagsBean bean, int maxSize) {
        if (maxSize != 0) {
            RealmResults<TagsBean> results = getRealm().where(TagsBean.class).findAllSorted("time", Sort.DESCENDING);
            if (results.size() >= maxSize) {
                for (int i = maxSize - 1; i < results.size(); i++) {
                    deleteTag(results.get(i).id);
                }
            }
        }
        getRealm().beginTransaction();
        getRealm().copyToRealm(bean);
        getRealm().commitTransaction();
    }


    /**
     * 删除 记录
     *
     * @param
     */
    public void deleteTag(String name) {
        TagsBean data = getRealm().where(TagsBean.class).equalTo("name", name).findFirst();
        getRealm().beginTransaction();
        data.deleteFromRealm();
        getRealm().commitTransaction();
    }

    /**
     * 查询 记录
     *
     * @param name
     * @return
     */
    public boolean queryTagname(String name) {
        RealmResults<TagsBean> results = getRealm().where(TagsBean.class).findAll();
        for (TagsBean item : results) {
            if (item.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 查询 记录
     *
     * @param name
     * @return
     */
    public TagsBean queryTagByname(String name) {
        RealmResults<TagsBean> results = getRealm().where(TagsBean.class).findAll();
        for (TagsBean item : results) {
            if (item.name.equals(name)) {
                return item;
            }
        }
        return null;
    }
    /**
     * 修改
     */
    public void updateTag(String id, String favarote) {
        getRealm().beginTransaction();
       // getRealm().where(TagsBean.class).equalTo("id", id).findFirst().setFavorite(Boolean.valueOf(favarote));
        getRealm().commitTransaction();
    }
    public List<TagsBean> getTagsList() {
        //使用findAllSort ,先findAll再result.sort排序
        RealmResults<TagsBean> results = getRealm().where(TagsBean.class).findAllSorted("time", Sort.ASCENDING);
        return getRealm().copyFromRealm(results);
    }

    /**
     * 清空历史
     */
    public void deleteAllRecord() {
        getRealm().beginTransaction();
        getRealm().delete(TagsBean.class);
        getRealm().commitTransaction();
    }


    //--------------------------------------------------首页----------------------------------------------------

    /**
     * 增加标签
     * @param bean
     */
    public void insertMainTag(MainBean bean) {
        getRealm().beginTransaction();
        getRealm().copyToRealm(bean);
        getRealm().commitTransaction();
    }


    /**
     * 删除 记录
     * @param
     */
    public void deleteMainTag(String name) {
        TagsBean data = getRealm().where(TagsBean.class).equalTo("name", name).findFirst();
        getRealm().beginTransaction();
        data.deleteFromRealm();
        getRealm().commitTransaction();
    }

    /**
     * 查询 记录
     *
     * @param name
     * @return
     */
    public boolean queryMainName(String name) {
        RealmResults<TagsBean> results = getRealm().where(TagsBean.class).findAll();
        for (TagsBean item : results) {
            if (item.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 修改
     */
    public void updateMainName(int id,String name) {
        getRealm().beginTransaction();
        getRealm().where(MainBean.class).equalTo("id", id).findFirst().name = name;
        getRealm().commitTransaction();
    }
    /**
     * 修改
     */
    public void updateMainImagePath(int id,String imagePath) {
        getRealm().beginTransaction();
        getRealm().where(MainBean.class).equalTo("id", id).findFirst().imagePath = imagePath;
        getRealm().commitTransaction();
    }
    /**
     * 修改
     */
    public void updateMainNameImagePath(int id,String name,String imagePath) {
        getRealm().beginTransaction();
        MainBean mainBean = getRealm().where(MainBean.class).equalTo("id", id).findFirst();
        mainBean.imagePath = imagePath;
        mainBean.name = name;
        getRealm().commitTransaction();
    }
    public List<MainBean> getMainTagsList() {
        //使用findAllSort ,先findAll再result.sort排序
        RealmResults<MainBean> results = getRealm().where(MainBean.class).findAll();
        return getRealm().copyFromRealm(results);
    }
    //--------------------------------------------------产品----------------------------------------------------

    /**
     * 增加标签
     * @param bean
     */
    public void insertProductBean(ProductBean bean) {
        getRealm().beginTransaction();
        getRealm().copyToRealm(bean);
        getRealm().commitTransaction();
    }


    /**
     * 删除 记录
     * @param
     */
  public void deleteProductBean(int id,int sefId) {
      RealmResults<ProductBean> productBeenList = getRealm().where(ProductBean.class).equalTo("id", id).findAll();
      getRealm().beginTransaction();
       if (productBeenList !=null){
           for (ProductBean bean: productBeenList) {
               if (bean.sefeid == sefId){
                   bean.deleteFromRealm();
               }
           }
       }
        getRealm().commitTransaction();
    }

    /**
     * 查询 记录
     *
     * @param name
     * @return
     */
    public boolean queryProductBean(String name) {
        RealmResults<TagsBean> results = getRealm().where(TagsBean.class).findAll();
        for (TagsBean item : results) {
            if (item.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 修改
     */
    public void updateProductBean(int id,int sefId,String name,String number) {
        getRealm().beginTransaction();
        RealmResults<ProductBean> productBeanRealmResults = getRealm().where(ProductBean.class).equalTo("id", id).findAll();
        if (productBeanRealmResults != null){
            for (ProductBean bean: productBeanRealmResults) {
                if (bean.sefeid == sefId){
                    bean.number = number;
                    bean.name = name;
                }
            }
        }
        getRealm().commitTransaction();
    }
    public List<ProductBean> getProductBeanList(int id) {
        //使用findAllSort ,先findAll再result.sort排序
        RealmResults<ProductBean> results = getRealm().where(ProductBean.class).equalTo("id", id).findAll();
        return getRealm().copyFromRealm(results);
    }

}
