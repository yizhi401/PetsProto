package cn.peterchen.pets.entity;

import cn.peterchen.pets.common.db.Column;
import cn.peterchen.pets.common.db.Table;

/**
 * 主人与宠物的关系
 * Created by peter on 15-1-27.
 */
@Table(tableName = "relationship_table")
public class Relationship {

    @Column(isPrimaryKey = true)
    private Integer _ID;

    /**
     * 关系id
     */
    @Column
    private Long rid;

    /**
     * 主人
     */
    @Column
    private Master master;

    @Column
    private Long mid;

    private Pet pet;

    @Column
    private Long pid;

    /**
     * 与主人建立关系的时间
     */
    @Column
    private Long buildTime;

    /**
     * 亲密度
     */
    @Column
    private Integer affinity;


    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Long buildTime) {
        this.buildTime = buildTime;
    }

    public Integer getAffinity() {
        return affinity;
    }

    public void setAffinity(Integer affinity) {
        this.affinity = affinity;
    }
}
