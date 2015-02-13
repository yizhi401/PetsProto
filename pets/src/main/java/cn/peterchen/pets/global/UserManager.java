package cn.peterchen.pets.global;

import cn.peterchen.pets.entity.User;

/**
 * Created by peter on 15-2-9.
 */
public class UserManager {

    public static UserManager instance;

    private User user;

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
        user = new User();
        if (user.getId() == -1) {
            user = null;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.saveUser();
    }
}
