package cn.peterchen.pets.entity;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.peterchen.pets.global.PetApplication;

/**
 * This class is singeleton.
 * manages the item data from table, including shopitems, courses, careers;
 * Created by peter on 15-2-5.
 */
public class EntityManager {

    private static EntityManager instance;
    private Map<Integer, ShopItem> shopItems;
    private Map<Integer, Course> courses;
    private Map<Integer, Career> careers;
    private Context context;

    public static EntityManager getInstance() {
        if (instance == null) {
            synchronized (EntityManager.class) {
                if (instance == null) {
                    instance = new EntityManager();
                }
            }
        }
        return instance;
    }

    private EntityManager() {
        this.context = PetApplication.getInstance().getBaseContext();
        initShopList();
        initCourseList();
        initCareerList();
    }

    private void initCareerList() {
        careers = new HashMap<>();

    }


    private void initCourseList() {
        courses = new HashMap<>();
    }

    private void initShopList() {

        shopItems = new HashMap<>();
    }

    public Map<Integer, ShopItem> getShopItems() {
        return shopItems;
    }

    public Map<Integer, Course> getCourses() {
        return courses;
    }

    public Map<Integer, Career> getCareers() {
        return careers;
    }
}
