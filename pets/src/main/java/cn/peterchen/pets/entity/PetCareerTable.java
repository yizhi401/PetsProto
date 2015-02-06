package cn.peterchen.pets.entity;

import cn.peterchen.pets.common.db.Column;
import cn.peterchen.pets.common.db.Table;

/**
 * Created by peter on 15-2-5.
 */
@Table(tableName = "pet_career_table")
public class PetCareerTable {
    @Column(isPrimaryKey = true)
    private Integer _ID;

    @Column
    private Long pid;

    @Column
    private Long careerId;


    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getCareerId() {
        return careerId;
    }

    public void setCareerId(Long careerId) {
        this.careerId = careerId;
    }
}
