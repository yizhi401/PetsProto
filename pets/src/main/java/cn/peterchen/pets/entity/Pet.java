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

    private Long pid;

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
    private Relationship relation;


    public static Pet initPet(Context context) {
        Pet pet = new Pet();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SP_PET, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isInit", false)) {
            //为初始化宠物，初始化
            generateNewPet(pet);
        } else {
            restoreMyPet(context, pet);
        }
        return pet;
    }

    private static void restoreMyPet(Context context, Pet pet) {
        pet = new Pet();
        pet.property = Property.restoreProperty(context);
        pet.ability = Ability.restoreAbility();
        pet.education = Education.getEducation(pet.pid);
    }

    private static void generateNewPet(Pet pet) {
        pet = new Pet();
        pet.property = Property.generateProperty();
        pet.ability = Ability.generateAbility();
    }

}
