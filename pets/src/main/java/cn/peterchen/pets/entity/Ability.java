package cn.peterchen.pets.entity;

/**
 * 宠物的能力
 * <p/>
 * 产生：宠物诞生时候随机指定
 * <p/>
 * 成长：宠物学习特定的课程会增加特定的能力
 * <p/>
 * 影响：宠物的职业选择和职业等级
 * <p/>
 * 模型：采用霍兰德职业测试的六因素模型
 * <p/>
 * Created by peter on 15-1-27.
 */
public class Ability {

    //RIASEC
    public static final int ABILITY_TYPE_REALISTIC = 1;
    public static final int ABILITY_TYPE_INVESTIGATIVE = 2;
    public static final int ABILITY_TYPE_ARTISTIC = 3;
    public static final int ABILITY_TYPE_SOCIAL = 4;
    public static final int ABILITY_TYPE_ENTERPRISING = 5;
    public static final int ABILITY_TYPE_CONVENTIOAL = 6;

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


    public static Ability restoreAbility(){
        return null;
    }

    public static Ability generateAbility(){
        return null;
    }
}
