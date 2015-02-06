package cn.peterchen.pets.global;

import android.content.Context;

import cn.peterchen.pets.entity.User;

/**
 * Created by peter on 15-2-5.
 */
public class UserManager {

    private static UserManager instance;

    private User user;

    private Context context;


    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    private UserManager() {
        this.context = PetApplication.getInstance().getBaseContext();
    }

    private void saveUserToSP() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
