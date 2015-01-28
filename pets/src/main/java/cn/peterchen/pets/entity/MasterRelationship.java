package cn.peterchen.pets.entity;

/**
 *
 * 主人与宠物的关系
 * Created by peter on 15-1-27.
 */
public class MasterRelationship {

    /**
     * 关系id
     */
    private long relationshipId;
    /**
     * 主人
     */
    private Master master;

    /**
     * 与主人建立关系的时间
     */
    private long relationshipTime;


    /**
     * 亲密度
     */
    private int affinity;

}
