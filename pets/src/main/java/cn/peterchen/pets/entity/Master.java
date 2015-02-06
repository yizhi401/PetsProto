package cn.peterchen.pets.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.peterchen.pets.common.db.Column;

/**
 * 主人类
 * <p/>
 * Created by peter on 15-1-27.
 */
public class Master {
    @Column(isPrimaryKey = true)
    private Integer _ID;

    @Column
    private long mid;
    @Column
    private long rid;
    @Column
    private long uid;
    @Column
    private String name;
    @Column
    private long money;

    private Map<Integer, Integer> shopItemList;


    private Master() {
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public List<ShopItem> getShopItemList() {
        return null;
    }
}


