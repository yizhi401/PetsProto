package cn.peterchen.pets.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 上过的课程
 * <p/>
 * Created by peter on 15-1-27.
 */
public class Education {

    private static final int LEVEL_KINDERGARTEN = 1;
    private static final int LEVEL_PRIMARY = 2;
    private static final int LEVEL_MIDDLE = 3;
    private static final int LEVEL_COLLEGE = 4;

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


    public static Education getEducation(Long pid) {
        Map<Integer, Course> allCourses = EntityManager.getInstance().getCourses();
        List<PetCourseTable> learntCourse = new ArrayList<PetCourseTable>();

        Education education = new Education();
        List<Course> kinder = new ArrayList<>();
        List<Course> primary = new ArrayList<>();
        List<Course> middle = new ArrayList<>();
        List<Course> university = new ArrayList<>();

        Course tmpCourse;
        for (PetCourseTable tmp : learntCourse) {
            tmpCourse = allCourses.get(tmp.getPid());
            switch (tmpCourse.getLevel()) {
                case LEVEL_KINDERGARTEN:
                    kinder.add(tmpCourse);
                    break;
                case LEVEL_PRIMARY:
                    primary.add(tmpCourse);
                    break;
                case LEVEL_MIDDLE:
                    middle.add(tmpCourse);
                    break;
                case LEVEL_COLLEGE:
                    university.add(tmpCourse);
                    break;
                default:
                    break;
            }
        }

        education.setKindergarten(kinder);
        education.setPrimarySchool(primary);
        education.setMiddleSchool(middle);
        education.setCollege(university);
        return education;
    }


    public List<Course> getKindergarten() {
        return kindergarten;
    }

    public void setKindergarten(List<Course> kindergarten) {
        this.kindergarten = kindergarten;
    }

    public List<Course> getPrimarySchool() {
        return primarySchool;
    }

    public void setPrimarySchool(List<Course> primarySchool) {
        this.primarySchool = primarySchool;
    }

    public List<Course> getMiddleSchool() {
        return middleSchool;
    }

    public void setMiddleSchool(List<Course> middleSchool) {
        this.middleSchool = middleSchool;
    }

    public List<Course> getCollege() {
        return college;
    }

    public void setCollege(List<Course> college) {
        this.college = college;
    }
}
