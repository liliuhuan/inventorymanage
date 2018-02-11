package com.liliuhuan.com.inventorymanageapp.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by liliuhuan on 2018/2/10.
 */

public class MainBean  extends RealmObject implements Serializable {
    public int id ;
    public String imagePath;
    public String name ;

    public MainBean() {
    }

    public MainBean(String imagePath, String name) {
        this.imagePath = imagePath;
        this.name = name;
    }

    public MainBean(int id, String imagePath, String name) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
    }
}
