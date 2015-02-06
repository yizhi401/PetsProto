package cn.peterchen.pets.entity;

import cn.peterchen.pets.common.db.Column;
import cn.peterchen.pets.common.db.Table;

/**
 * Created by peter on 15-1-27.
 */
@Table(tableName = "shop_item_table")
public class ShopItem {

    @Column(isPrimaryKey = true)
    private Integer _ID;

    @Column
    private Integer itemId;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private String imgUri;

    @Column
    private String effectsDes;

    @Column
    private Integer effect;

    /**
     * user所具有的数量，不是shopItem的一部分,因此不入库
     */
    private Integer number;

    public ShopItem() {
    }


    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getEffectsDes() {
        return effectsDes;
    }

    public void setEffectsDes(String effectsDes) {
        this.effectsDes = effectsDes;
    }

    public Integer getEffect() {
        return effect;
    }

    public void setEffect(Integer effect) {
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "ShopItem{" +
                "_ID=" + _ID +
                ", itemId=" + itemId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imgUri='" + imgUri + '\'' +
                ", effectsDes='" + effectsDes + '\'' +
                ", effect=" + effect +
                ", number=" + number +
                '}';
    }
}
