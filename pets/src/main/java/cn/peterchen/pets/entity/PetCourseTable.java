package cn.peterchen.pets.entity;

import cn.peterchen.pets.common.db.Column;
import cn.peterchen.pets.common.db.Table;

/**
 * Created by peter on 15-2-5.
 */
@Table(tableName = "pet_course_table")
public class PetCourseTable {

    @Column(isPrimaryKey = true)
    private Integer _ID;

    @Column
    private Long pid;

    @Column
    private Long courseId;


    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
