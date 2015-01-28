package cn.peterchen.pets.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 主人类
 * <p/>
 * Created by peter on 15-1-27.
 */
public class Master {

    private static Master master;
    private long masterId;
    private String name;

    private List<ShopItem> shopItemList;

    public static Master getMaster() {
        if (master == null) {
            synchronized (Master.class) {
                if (master == null) {
                    master = new Master();
                }
            }
        }
        return master;
    }

    private Master() {
        initShopList();
    }


    private void initShopList() {
        shopItemList = new ArrayList<ShopItem>();
        ShopItem item;
        item = new ShopItem();
        item.name = "汉堡";
        item.buyPrice = 31;
        item.sellPrice = 29;
        item.myStorage = 30;
        shopItemList.add(item);

        item = new ShopItem();
        item.name = "沐浴乳";
        item.buyPrice = 58;
        item.sellPrice = 56;
        item.myStorage = 11;
        shopItemList.add(item);

        item = new ShopItem();
        item.name = "玩具";
        item.buyPrice = 50;
        item.sellPrice = 45;
        item.myStorage = 10;
        shopItemList.add(item);

        item = new ShopItem();
        item.name = "药品";
        item.buyPrice = 210;
        item.sellPrice = 200;
        item.myStorage = 15;
        shopItemList.add(item);

    }

    public List<ShopItem> getShopItemList() {
        return shopItemList;
    }
}
