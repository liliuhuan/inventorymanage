package com.liliuhuan.com.inventorymanageapp.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by liliuhuan on 2018/2/10.
 */

public class ProductBean extends RealmObject implements Serializable {
    public int id;
    public int sefeid;
    public String name;
    public String number;

    public ProductBean() {
    }

    public ProductBean(int id,int sefeid, String name, String number) {
        this.id = id;
        this.sefeid = sefeid;
        this.name = name;
        this.number = number;
    }
}
