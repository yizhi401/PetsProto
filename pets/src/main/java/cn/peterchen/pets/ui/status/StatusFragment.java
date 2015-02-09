package cn.peterchen.pets.ui.status;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import cn.peterchen.pets.R;
import cn.peterchen.pets.entity.Pet;
import cn.peterchen.pets.global.UserManager;

/**
 * Created by peter on 15-1-27.
 */
public class StatusFragment extends DialogFragment {

    public static final String TAG = StatusFragment.class.getName();

    private ProgressBar experienceBar;
    private ProgressBar hungryBar;
    private ProgressBar tiredBar;
    private ProgressBar moodBar;
    private ProgressBar healthBar;
    private ProgressBar cleanessBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.status_fragment, container, false);
        experienceBar = (ProgressBar) rootView.findViewById(R.id.experience_progress);
        hungryBar = (ProgressBar) rootView.findViewById(R.id.hungry_progress);
        tiredBar = (ProgressBar) rootView.findViewById(R.id.tired_progress);
        moodBar = (ProgressBar) rootView.findViewById(R.id.mood_progress);
        healthBar = (ProgressBar) rootView.findViewById(R.id.health_progress);
        cleanessBar = (ProgressBar) rootView.findViewById(R.id.cleaness_progress);

        Pet myPet = UserManager.getInstance().getUser().getPet();

        experienceBar.setProgress(myPet.experience);
        hungryBar.setProgress(myPet.hungry);
        tiredBar.setProgress(myPet.tired);
        moodBar.setProgress(myPet.mood);
        healthBar.setProgress(myPet.health);
        cleanessBar.setProgress(myPet.cleaness);

        return rootView;
    }
}
