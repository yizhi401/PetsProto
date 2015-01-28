package cn.peterchen.pets.ui.message;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.peterchen.pets.R;

/**
 * Created by peter on 15-1-27.
 */
public class SearchFragment extends DialogFragment {

    public static final String TAG = SearchFragment.class.getName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.status_fragment, container, false);

        return rootView;
    }
}
