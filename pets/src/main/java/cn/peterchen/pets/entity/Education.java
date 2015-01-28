package cn.peterchen.pets.entity;

import java.util.List;

/**
 * 上过的课程
 * <p/>
 * Created by peter on 15-1-27.
 */
public class Education {

    /**
     * 幼儿园学过的课程
     */
    private List<Course> kindergarten;

    /**
     * 小学学过的课程
     */
    private List<Course> primarySchool;

    /**
     * 中学学过的课程
     */
    private List<Course> middleSchool;

    /**
     * 大学学过的课程
     */
    private List<Course> college;

}
