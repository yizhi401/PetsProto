package cn.peterchen.pets.entity;

/**
 * 用户类
 * Created by peter on 15-1-27.
 */
public class User {

    private long id;

    public String username;
    private String password;

    private Master master;
    private Pet pet;


    public User(String userName, String password) {
        this.username = userName;
        this.password = password;
    }
}
