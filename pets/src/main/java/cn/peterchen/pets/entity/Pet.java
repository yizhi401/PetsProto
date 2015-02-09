package cn.peterchen.pets.entity;

import android.content.Context;

import java.util.List;

/**
 * 宠物类，包含了宠物所有必要的属性
 * Created by peter on 15-1-27.
 */
public class Pet {
    //RIASEC
    public static final int ABILITY_TYPE_REALISTIC = 1;
    public static final int ABILITY_TYPE_INVESTIGATIVE = 2;
    public static final int ABILITY_TYPE_ARTISTIC = 3;
    public static final int ABILITY_TYPE_SOCIAL = 4;
    public static final int ABILITY_TYPE_ENTERPRISING = 5;
    public static final int ABILITY_TYPE_CONVENTIOAL = 6;


    private Long id;

    /**
     * 宠物等级，1-100级
     */
    public int level;

    /**
     * 成长，相当于等级的细化
     */
    public int experience;

    /**
     * 成长速度，隐藏属性，
     * 决定了宠物学习、成长、打工的效率，
     * 是一个系数。
     * 取决于宠物的心情、和主人亲密度、疲劳、健康、以及宝石加速
     */
    public int speed;

    /**
     * 饥饿度
     */
    public int hungry;

    /**
     * 疲劳度
     */
    public int tired;

    /**
     * 心情
     */
    public int mood;

    /**
     * 健康
     */
    public int health;

    /**
     * 清洁
     */
    public int cleaness;


    /**
     * Helpers
     */
    private int social;

    /**
     * Creators
     */
    private int artistic;

    /**
     * Persuaders
     */
    private int enterprising;

    /**
     * doers
     */
    private int realistic;

    /**
     * organizers
     */
    private int conventional;

    /**
     * thinkers
     */
    private int investigative;


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


    public static Pet restoreMyPet(Context contet, Long pid) {
//        pet = new Pet();
//        pet.education = Education.getEducation(pet.id);
        return new Pet();
    }

    public void saveMyPetToSP(Context context) {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Career> getCareer() {
        return career;
    }

    public void setCareer(List<Career> career) {
        this.career = career;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public Relationship getRelation() {
        return relation;
    }

    public void setRelation(Relationship relation) {
        this.relation = relation;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHungry() {
        return hungry;
    }

    public void setHungry(int hungry) {
        this.hungry = hungry;
    }

    public int getTired() {
        return tired;
    }

    public void setTired(int tired) {
        this.tired = tired;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getCleaness() {
        return cleaness;
    }

    public void setCleaness(int cleaness) {
        this.cleaness = cleaness;
    }

    public int getSocial() {
        return social;
    }

    public void setSocial(int social) {
        this.social = social;
    }

    public int getArtistic() {
        return artistic;
    }

    public void setArtistic(int artistic) {
        this.artistic = artistic;
    }

    public int getEnterprising() {
        return enterprising;
    }

    public void setEnterprising(int enterprising) {
        this.enterprising = enterprising;
    }

    public int getRealistic() {
        return realistic;
    }

    public void setRealistic(int realistic) {
        this.realistic = realistic;
    }

    public int getConventional() {
        return conventional;
    }

    public void setConventional(int conventional) {
        this.conventional = conventional;
    }

    public int getInvestigative() {
        return investigative;
    }

    public void setInvestigative(int investigative) {
        this.investigative = investigative;
    }
}
