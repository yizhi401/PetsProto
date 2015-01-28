package cn.peterchen.pets.entity;

import android.content.Context;

/**
 * 宠物的基础属性，均为1-100
 * Created by peter on 15-1-27.
 */
public class Property {

    /**
     * 宠物等级，1-100级
     */
    public int level;

    /**
     * 成长，相当于等级的细化
     */
    public int growth;

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


    public static Property generateProperty() {
        return new Property();
    }

    public static Property restoreProperty(Context context) {
        return new Property();
    }
}
