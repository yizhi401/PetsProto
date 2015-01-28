package cn.peterchen.pets.global;

import android.app.Application;
import android.content.Context;

import cn.peterchen.pets.entity.Pet;

/**
 * Created by peter on 15-1-27.
 */
public class PetApplication extends Application {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
        initPet();
    }

    private void initPet() {
        Pet.initPet(context);
    }

}
