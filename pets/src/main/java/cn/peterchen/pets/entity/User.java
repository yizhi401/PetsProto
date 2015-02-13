package cn.peterchen.pets.entity;

import android.content.Context;
import android.content.SharedPreferences;

import cn.peterchen.pets.global.Constant;
import cn.peterchen.pets.global.PetApplication;

/**
 * 用户类
 * Created by peter on 15-1-27.
 */
public class User {

    private Context context;

    private Long id;

    public String username;

    private String password;

    private Long mid;

    private Master master;

    private Long pid;

    private Pet pet;

    public User() {
        initUser();
    }

    private void initUser() {
        this.context = PetApplication.getInstance().getBaseContext();
        getUserFromSP();
        restorePetAndMaster();
    }

    public void saveUser() {
        saveUserToSP();
        pet.saveMyPetToSP(context);
        master.saveMasterToSP(context);
    }

    private void saveUserToSP() {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("id", getId());
        editor.putLong("pid", getPid());
        editor.putLong("mid", getMid());
        editor.putString("username", getUsername());
        editor.putString("password", getPassword());
        editor.apply();
    }


    private boolean getUserFromSP() {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_USER, Context.MODE_PRIVATE);
        setId(sp.getLong("id", -1));
        setPid(sp.getLong("pid", -1));
        setMid(sp.getLong("mid", -1));
        setUsername(sp.getString("username", ""));
        setPassword(sp.getString("password", ""));
        if (getId() == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void restorePetAndMaster() {
        setPet(Pet.restoreMyPet(context, pid));
        setMaster(Master.restoreMaster(context, mid));
    }


    private void generateNewMaster() {
        master = new Master();
        master.setId(getId());
    }

//    private void generateNewPet() {
//        RequestParams params = new RequestParams();
//        params.put("uid", getId());
//        HttpUtil.jsonRequest(context, URLConfig.GET_NEW_PET, params, new TypeToken<Result<Pet>>() {
//        }, new VolleyRequestListenerImp<Pet>() {
//            @Override
//            public void onSuccess(Pet response) {
//                setPet(response);
//            }
//        });
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getMid() {
        if (mid == null)
            return (long) -1;
        else
            return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.mid = master.getId();
        this.master = master;
    }

    public Long getPid() {
        if (pid == null) {
            return (long) -1;
        } else {
            return pid;
        }
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pid = pet.getId();
        this.pet = pet;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mid=" + mid +
                ", pid=" + pid +
                '}';
    }


}
