package cn.peterchen.pets.entity;

import cn.peterchen.pets.common.db.Column;
import cn.peterchen.pets.common.db.Table;

/**
 * Created by peter on 15-1-27.
 */
@Table(tableName = "course_table")
public class Course {

    @Column(isPrimaryKey = true)
    private Integer _ID;

    /**
     * 课程id
     */
    @Column
    private Integer courseId;

    /**
     * 课程名称
     */
    @Column
    private String name;

    /**
     * 课程说明
     */
    @Column
    private String description;

    /**
     * 课程影响到的能力属性
     */
    @Column
    private Integer level;

    /**
     * 这门课程会影响到的属性
     */
    @Column
    private Integer type;

    /**
     * 影响到的属性值
     */
    @Column
    private Integer effect;


    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getEffect() {
        return effect;
    }

    public void setEffect(Integer effect) {
        this.effect = effect;
    }
}
