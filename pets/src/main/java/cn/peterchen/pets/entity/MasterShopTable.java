package cn.peterchen.pets.entity;

import cn.peterchen.pets.common.db.Column;
import cn.peterchen.pets.common.db.Table;

/**
 * Created by peter on 15-2-5.
 */
@Table(tableName = "master_shop_table")
public class MasterShopTable {

    @Column(isPrimaryKey = true)
    private Integer _ID;

    @Column
    private Long mid;

    @Column
    private Long itemId;
    
    @Column
    private Integer number;


    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
