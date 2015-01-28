package cn.peterchen.pets.entity;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import cn.peterchen.pets.global.Constant;

/**
 * 宠物类，包含了宠物所有必要的属性
 * Created by peter on 15-1-27.
 */
public class Pet {

    private static Pet pet;

    /**
     * 宠物的基础属性
     */
    private Property property;

    /**
     * 宠物的能力属性
     */
    private Ability ability;

    /**
     * 宠物可能从事的职业
     */
    private List<Career> career;

    /**
     * 教育背景
     */
    private Education education;

    /**
     * 现有主人
     */
    private MasterRelationship mrelation;


    public static Pet initPet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SP_PET, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isInit", false)) {
            //为初始化宠物，初始化
            generateNewPet();
        } else {
            restoreMyPet(context);
        }
        return pet;
    }

    private static void restoreMyPet(Context context) {
        pet = new Pet();
        pet.property = Property.restoreProperty(context);
    }

    private static void generateNewPet() {
        pet = new Pet();
        pet.property = Property.generateProperty();
    }

    public static Pet getPet() {
        return pet;
    }

    public void eat(Food food) {
        pet.property.hungry += food.effect;
    }

}
