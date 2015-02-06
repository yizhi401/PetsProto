package cn.peterchen.pets.entity;

import cn.peterchen.pets.common.db.Column;
import cn.peterchen.pets.common.db.Table;

/**
 * Created by peter on 15-1-27.
 */
@Table(tableName = "career_table")
public class Career {

    @Column(isPrimaryKey = true)
    private Integer _ID;

    /**
     * 职业id
     */
    @Column
    private Integer careerId;

    /**
     * 职业名称
     */
    @Column
    private String name;

    /**
     * 职业等级
     */
    @Column
    private Integer level;

    /**
     * 职业收入
     */
    @Column
    private Integer salary;

    /**
     * 职业描述
     */
    @Column
    private String description;


    public Integer getCareerId() {
        return careerId;
    }

    public void setCareerId(Integer careerId) {
        this.careerId = careerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
